package com.itheima.reggie.utils;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * @author 金宗文
 * @version 1.0
 */
public class WebUtils {
    //定义一个文件上传的路径常量
    public static String FURN_IMG_DIRECTORY = "assets/images/product-image";
    /**
     * 判断请求是否为一个ajax请求
     * @param request
     * @return
     */
    public static boolean iaAjaxRequest(HttpServletRequest request){
        return  "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
    }
    public static String getYearMonthDay(){
        //第三代日期类
        LocalDateTime ldt = LocalDateTime.now();
        int year = ldt.getYear();
        int monthValue = ldt.getMonthValue();
        int dayOfMonth = ldt.getDayOfMonth();

        String yearMonthDay = year + "/" + monthValue + "/"
                + dayOfMonth + "/";

        return yearMonthDay;
    }
}
