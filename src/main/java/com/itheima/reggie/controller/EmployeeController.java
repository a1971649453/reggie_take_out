package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Employee;
import com.itheima.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author 金宗文
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Resource
    private EmployeeService employeeService;


    /**
     * 员工登陆
     * @param request
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
        /**
         * 处理逻辑如下：
         * ①. 将页面提交的密码password进行md5加密处理, 得到加密后的字符串
         * ②. 根据页面提交的用户名username查询数据库中员工数据信息
         * ③. 如果没有查询到, 则返回登录失败结果
         * ④. 密码比对，如果不一致, 则返回登录失败结果
         * ⑤. 查看员工状态，如果为已禁用状态，则返回员工已禁用结果
         * ⑥. 登录成功，将员工id存入Session, 并返回登录成功结果
         */
        //1.将页面提交的密码镜像MD5加密
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        //2.根据页面提交的用户名username查询数据库中员工数据信息
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);

        //3.如果没有查询到, 则返回登录失败结果
        if (emp == null){
            return R.error("登陆失败");
        }

        //4.密码比对，如果不一致, 则返回登录失败结果
        if (!emp.getPassword().equals(password)){
            return R.error("登陆失败");
        }

        //5.查看员工状态，如果为已禁用状态，则返回员工已禁用结果
        if (emp.getStatus() == 0){//0表示禁用
            return R.error("账号已禁用");
        }
        //6.登录成功，将员工id存入Session, 并返回登录成功结果
        request.getSession().setAttribute("employee",emp.getId());
        return R.success(emp);
    }


    /**
     * 员工退出
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        //清理session中保存的员工id
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }


}
