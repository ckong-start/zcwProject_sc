package com.scw.user.controller;

import com.scw.common.bean.AppResponse;
import com.scw.common.utils.ScwAppUtils;
import com.scw.user.bean.TMember;
import com.scw.user.service.UserService;
import com.scw.user.utils.SmsTemplate;
import com.scw.user.vo.UserRegistVo;
import com.scw.user.vo.UserRespVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 *  2020-02-10 23:41
 */
@Api(tags = "用户登录注册模块")
@RestController
@RequestMapping("/user")
public class UserLoginRegistController {

    @Autowired
    SmsTemplate smsTemplate;
    @Autowired
    StringRedisTemplate srt;
    @Autowired
    UserService userService;

    @ApiOperation("发送短信获取手机验证码")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "phoneNum", value = "手机号码",
                    defaultValue = "15012341234", required = true)
    })
    @PostMapping("/sendsms")
    public AppResponse<Object> sendSms(String phoneNum) {
//        1.1验证手机号码格式
        boolean isMobilePhone = ScwAppUtils.isMobilePhone(phoneNum);
        if (!isMobilePhone) {
            return AppResponse.fail("10001", "手机号码格式错误");
        }
//        1.2判断手机号码获取验证码次数是否超过范围24小时内3次
        String codeCountKey = "code:" + phoneNum + ":count";
        String codeCount = srt.opsForValue().get(codeCountKey);
        int count = 0;

        String codeKey = "code:" + phoneNum + ":code";

        if (!StringUtils.isEmpty(codeCount)) {
            // 1.3判断手机号码是否有未过期的验证码
            if (srt.hasKey(codeKey)) {
                // 手机号码验证码还未失效
                return AppResponse.fail("10003", "该手机号码获取验证码过于频繁");
            }
            //表示不是第一次获取验证码
            count = Integer.parseInt(codeCount);
            if (count >= 3) {
                return AppResponse.fail("10002", "该手机号码今天获取验证码次数不足");
            }

        }

//        1.4生成6为验证码
        String code = UUID.randomUUID().toString().replace("-", "").substring(0, 6);
//        1.5调用smsTemplate的方法发送短信
        Map<String, String> querys = new HashMap<>();
        querys.put("mobile", phoneNum);
        querys.put("param", "code:" + code);
        querys.put("tpl_id", "TP1711063");
        boolean result = smsTemplate.sendMessage(querys);

//        1.6如果成功将手机号码和验证码保存在服务器中并设置过期时间
        if (!result) {
            return AppResponse.fail("10004", "手机获取验证码失败");
        }
        srt.opsForValue().set(codeKey, code, 10, TimeUnit.MINUTES);
//        1.7更新该手机号码获取验证码的次数
        if (count == 0) {
            srt.opsForValue().set(codeCountKey, "1", 24, TimeUnit.HOURS);
        } else {
            srt.opsForValue().increment(codeCountKey);
        }
//        1.8给访问者响应结果
        return AppResponse.ok("短信验证码发送成功");
    }


    @ApiOperation("用户注册的方法")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "vo", value = "用户注册时填入的数据")
    })
    @PostMapping("/doRegist")
    public AppResponse<Object> doRegist(UserRegistVo vo){

//        1.检验验证码是否正确	1.1获取reids储存的验证码	1.2比较请求参数中的验证码和redis中获取的是否一致
        String codeKey = "code:" + vo.getLoginacct() + ":key";
        String redisCode = srt.opsForValue().get(codeKey);
        if (StringUtils.isEmpty(redisCode))return AppResponse.fail("10006", "验证码已经失效");
        if (!vo.getCode().equalsIgnoreCase(redisCode))return AppResponse.fail("10007", "验证码错误");

//        2.调用业务层处理注册的业务	自动装配UserService类，调用注册方法，并tryCatch，有异常就返回注册失败的结果
        try {
            userService.doRegist(vo);
        } catch (Exception e) {
            return AppResponse.fail("10008", e.getMessage());
        }

//        3.删除之前的未过期的注册验证码
        srt.delete(codeKey);

//        4.给注册后的响应
        return AppResponse.ok("注册成功");
    }

    @ApiOperation("用户登录的方法")
    @PostMapping("/doLogin")
    public AppResponse<Object> doLogin(String loginacct, String userpswd){
//        1.调用业务层查询登录用户对象	UserService提供根据用户账号和密码查询用户返回用户的方法
        TMember member = userService.doLogin(loginacct, userpswd);
        if (member == null)return AppResponse.fail("10010", "用户名或密码错误");

//        2.将返回的用户对象转为UserRespVO对象;手动生成token，使用uuid;同上使用copy方法将查询到的用户的属性拷贝到UserRespVO对象中
        UserRespVo userRespVo = new UserRespVo();
        BeanUtils.copyProperties(member, userRespVo);
        String uuid = UUID.randomUUID().toString().replace("_", "");
        userRespVo.setAccessToken(uuid);

//        3.将生成的accessToken 和查询到的用户对象存到redis中：保持登录状态
//        将token拼接字符串login：++：token作为key，把查询到的member对象转为json存入redis中
        String memberKey = "login" + uuid + "token";
        ScwAppUtils.saveObj2Redis(member, memberKey, srt);

//        4.响应UserRespVO
        return AppResponse.ok(userRespVo);
    }
}

