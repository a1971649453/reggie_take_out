package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.User;
import com.itheima.reggie.service.UserService;
import com.itheima.reggie.utils.SMSUtils;
import com.itheima.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author 金宗文
 * @version 1.0
 */
@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    @Resource
    private RedisTemplate redisTemplate;


    /**
     * 发送
     * @param user
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session){
        //获取手机号
        String phone = user.getPhone();
        if(StringUtils.isNotEmpty(phone)){
            //生成随机4位验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("验证码为:{}",code);
//            //调用阿里云短信服务发送验证码
//            SMSUtils.sendMessage("阿里云短信测试","SMS_154950909","18512891272",code);

            //将生成的验证码保存到Session中
            session.setAttribute(phone,code);
            //将生成的验证码缓存到Redis中,设置有效期为5分钟
            redisTemplate.opsForValue().set(phone,code,5, TimeUnit.MINUTES);

            return R.success("手机验证码短信发送成功");
        }

        return R.error("短信操作失败");
    }

    /**
     * 移动端用户登录
     * @return
     */
    @PostMapping("/login")
    public R<User> login(@RequestBody Map map,HttpSession session){
        log.info(map.toString());
        //获取手机号
        String phone = map.get("phone").toString();
        //获取验证码
        String code = map.get("code").toString();
        //获取Session中的验证码
//        Object codeInSession = session.getAttribute(phone);

        //从Redis中获取验证码
        Object codeInSession = redisTemplate.opsForValue().get(phone);
        //比较验证码是否一致
        if (codeInSession != null && codeInSession.equals(code)){
            //如果一致,则登录成功,将用户信息保存到Session中
            //如果为新用户,自动完成注册
            LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
            userLambdaQueryWrapper.eq(User::getPhone,phone);
            User user = userService.getOne(userLambdaQueryWrapper);
            if (user == null){
                //新用户
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            //将用户信息保存到Session中
            session.setAttribute("user",user.getId());

            //如果用户登录成功,则删除Redis中的验证码
            redisTemplate.delete(phone);

            
            return R.success(user);
        }
        return R.error("登录失败");
    }
}
