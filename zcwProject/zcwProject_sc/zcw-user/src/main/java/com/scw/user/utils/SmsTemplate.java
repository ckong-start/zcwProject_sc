package com.scw.user.utils;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @create 2020-02-10 23:21
 */
@Slf4j
@Data
public class SmsTemplate {

    private String host;
    private String path;
    private String method;
    private String appcode;

    public boolean sendMessage(Map<String, String> querys){

        boolean result = false;
        Map<String, String> headers = new HashMap<String, String>();
//	    //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> bodys = new HashMap<String, String>();

        try {
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
//	    	//System.out.println(response.toString());
//	    	//获取response的body
//	    	//System.out.println(EntityUtils.toString(response.getEntity()));
//	    	// 响应报文： 响应首行、响应头、响应空行、响应体[json、html页面内容]
//	    	//response.getStatusLine();//获取相应报文首行
            String bodyStr = EntityUtils.toString(response.getEntity());
            Map map = JSON.parseObject(bodyStr, Map.class);
            log.info("解析响应体json字符串的结果为：{}",map);
            //根据map中的字段给调用方法的请求响应
            result = "00000".equals(map.get("return_code"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.debug("短信发送结果为：{}", result?"成功":"失败");
        //true代表短信发送成功
        return result;
    }
}
