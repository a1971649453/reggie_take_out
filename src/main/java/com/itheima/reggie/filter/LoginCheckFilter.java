package com.itheima.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.itheima.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 金宗文
 * @version 1.0
 * 检查用户是否登录的过滤器
 */
@Slf4j
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
    /**
     * PATH_MATCHER 路径匹配器
     */
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse rep = (HttpServletResponse) servletResponse;
//        A. 获取本次请求的URI
        String requestURI = req.getRequestURI();
        log.info("拦截到请求 {}",requestURI);
        //定义不需要处理的请求路径
        String[] urls = {"/employee/login", "/employee/logout","/backend/**","/front/**"};
//        B. 判断本次请求, 是否需要登录, 才可以访问
        boolean check = check(requestURI, urls);
//        C.如果不需要，则直接放行
        if (check){
            log.info("本次请求 {}不需要处理",requestURI);
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
//        D. 判断登录状态，如果已登录，则直接放行
        if (req.getSession().getAttribute("employee") != null){
            log.info("用户已登录,用户id为{}",req.getSession().getAttribute("employee"));
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        log.info("用户未登录");
//        E. 如果未登录, 则返回未登录结果,通过输出流向客户端页面相应数据
        rep.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
    }

    /**
     * 路径匹配,监测是否需要放行
     * @param requestURI
     * @param urls
     * @return
     */
    public boolean check(String requestURI, String[] urls) {
        for (String url : urls) {
            if (PATH_MATCHER.match(url, requestURI)) {
                return true;
            }
        }
        return false;
    }

}
