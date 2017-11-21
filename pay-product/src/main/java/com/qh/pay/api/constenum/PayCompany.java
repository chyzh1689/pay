package com.qh.pay.api.constenum;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName PayCompany
 * @Description 支付公司
 * @author chenyuezhi
 * @Date 2017年11月9日 上午9:44:10
 * @version 1.0.0
 */
public enum PayCompany {
	tfb,hx,bopay;
	private static final Map<String,String> descMap = new HashMap<>(10);
	
	private static final Map<String,PayCompany> enumMap = new HashMap<>(10);
	
	static{
		descMap.put(tfb.name(), "天付宝");
		enumMap.put(tfb.name(), tfb);
		
		descMap.put(hx.name(), "环迅");
		enumMap.put(hx.name(), hx);
		
		descMap.put(bopay.name(), "bopay");
		enumMap.put(bopay.name(), bopay);
	}
	
	public static PayCompany payCompany(String name){
		return enumMap.get(name);
		
	}
	
	public static Map<String, String> desc() {
		return descMap;
	}
	
}
