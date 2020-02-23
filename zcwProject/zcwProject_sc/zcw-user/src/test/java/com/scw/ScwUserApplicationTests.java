package com.scw;

import com.google.gson.Gson;
import com.scw.user.mapper.TMemberMapper;
import com.scw.user.utils.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@SpringBootTest
@Slf4j
class ScwUserApplicationTests {
	@Autowired
	TMemberMapper memberMapper;
	@Test
	void contextLoads() {
		log.debug("日志测试,debug");

		log.warn("日志测试,warn");
		log.error("日志测试,error");
		long count = memberMapper.countByExample(null);
		log.info("日志测试info,查询到的会员个数是：{}", count);
	}

	@Test
	void testSendMessage(){
		boolean result = false;
		Map<String, String> querys = new HashMap<String, String>();
		querys.put("mobile", "19821269297");
		String code = UUID.randomUUID().toString().replace("-", "").substring(0, 6);
		querys.put("param", "code:"+code);
		querys.put("tpl_id", "TP1711063");


		//可以修改的配置建议提取到配置文件中
		//由用户动态传入的参数 应该提取为形参：手机号码、动态生成的验证码、短信模板
	    String host = "http://dingxin.market.alicloudapi.com";
	    String path = "/dx/sendSms";
	    String method = "POST";
	    String appcode = "75cb9e7f5fc94db9a7b7ac1524d05f6a";
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
	    	log.warn("发送短信的响应体内容：{}", bodyStr);
			Map map = new Gson().fromJson(bodyStr, Map.class);
			log.info("解析响应体json字符串的结果为：{}",map);
			result = "00000".equals(map.get("return_code"));
		} catch (Exception e) {
	    	e.printStackTrace();
	    }
		log.debug("短信发送结果为：{}", result?"成功":"失败");
	}

}
