package com.mjs.delong.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mjs.delong.common.R;
import com.mjs.delong.entity.User;
import com.mjs.delong.service.UserService;
import com.mjs.delong.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 发送手机短信验证码
     * @param user
     * @param session
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session){
        //获取手机号
        String phone = user.getPhone();
        System.out.println(phone);
        if (StringUtils.isNotEmpty(phone)){
            //生成随机的4位验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("code={}",code);

            //发送短信
            //SMSUtils.sendMessage("瑞吉外卖","",phone);

            //将生成的验证码存放到Session中
//            session.setAttribute("code",code);
            //将生成的验证码存放在redis中 , 设置过期时间
            redisTemplate.opsForValue().set(phone,code,5, TimeUnit.MINUTES);
            log.info("产生的code为:{}",code);
            return R.success("发送手机短信验证码成功");
        }

        return R.error("短息发送失败");

    }


    /**
     * 移动端用户登录
     * @param map
     * @param session
     * @return
     */
    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session){
        log.info(map.toString());
        //获取手机号
        String phone = map.get("phone").toString();
        //获取验证码
        String code = map.get("code").toString();
        //从Session中获取保存的验证码
//        Object codeInSession = session.getAttribute("code");
        //从redis中获取保存的验证码
        String codeInRedis = (String)redisTemplate.opsForValue().get(phone);
        log.info(codeInRedis.toString());
        //进行验证码的比对(页面提交的验证码和Session中保存的验证码比对)
        if (codeInRedis != null && codeInRedis.equals(code)){
            //如果能够比对成功, 说明登录成功

            LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(User::getPhone,phone);
            //查看是否是已经注册的用户
            User user = userService.getOne(lambdaQueryWrapper);
            if (user == null){
                //该用户是新用户, 保存用户
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            session.setAttribute("user",user.getId());
            //登录成功,redis中的验证码已经没有用了
            return R.success(user);
        }
        return R.error("登录失败");
    }
}
