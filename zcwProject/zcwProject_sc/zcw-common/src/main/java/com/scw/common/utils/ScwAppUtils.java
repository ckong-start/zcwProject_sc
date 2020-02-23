package com.scw.common.utils;

import com.alibaba.fastjson.JSON;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @create 2020-02-11 21:17
 */
public class ScwAppUtils {
    // 正则验证手机号码格式的方法
    public static boolean isMobilePhone(String phone) {
        boolean flag = true;
        String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
        if (phone.length() != 11) {
            flag = false;
        } else {
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(phone);
            flag = m.matches();
        }

        return flag;
    }

    //将对象存入redis中
    public static <T> void saveObj2Redis(T t, String token, StringRedisTemplate srt){
        srt.opsForValue().set(token, JSON.toJSONString(t), 7, TimeUnit.DAYS);
    }

    //从redis中取出对象字符串，然后转化为对象
    public static <T> T redis2Obj(Class<T> type, String token, StringRedisTemplate srt){
        String objStr = srt.opsForValue().get(token);
        return JSON.parseObject(objStr, type);
    }

    //获取时间的方法
    public static String getFormatTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }

    public static String getFormatTime(String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(new Date());
    }

    public static String getFormatTime(String pattern, Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }
}
