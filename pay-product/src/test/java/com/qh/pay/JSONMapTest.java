package com.qh.pay;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * @ClassName JSONMapTest
 * @Description json转换测试
 * @author chenyuezhi
 * @Date 2017年11月7日 下午2:57:27
 * @version 1.0.0
 */
public class JSONMapTest {

	@Test
	public void strToMap(){
		String str = "{qq:0.002,q:0.002,wx:0.002,wy:0.002,gzh:0.002,wap:0.002,sp:0.002,acp:0.002,ali:0.002,}";
		System.out.println(str.length());
		JSONObject jo = JSON.parseObject(str);
		System.out.println(jo.get("qq"));
	}
}
