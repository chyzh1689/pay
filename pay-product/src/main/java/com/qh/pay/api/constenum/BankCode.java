package com.qh.pay.api.constenum;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName BankCode
 * @Description 银行代码
 * @author chenyuezhi
 * @Date 2017年10月31日 上午11:19:32
 * @version 1.0.0
 */
public enum BankCode {
	jsyh("1004");

	private String code;

	private BankCode(String code) {
		this.code = code;
	}

	public String code() {
		return this.code;
	}

	private static final Map<String, String> descMap = new HashMap<String, String>(32);
	static {
		descMap.put("建设银行", "1004");
		descMap.put("农业银行", "1002");
		descMap.put("工商银行", "1001");
		descMap.put("中国银行", "1003");
		descMap.put("浦发银行", "1014");
		descMap.put("光大银行", "1008");
		descMap.put("平安银行", "1011");
		descMap.put("兴业银行", "1013");
		descMap.put("邮政储蓄银行", "1006");
		descMap.put("中信银行", "1007");
		descMap.put("华夏银行", "1009");
		descMap.put("招商银行", "1012");
		descMap.put("广发银行", "1017");
		descMap.put("北京银行", "1016");
		descMap.put("上海银行", "1025");
		descMap.put("民生银行", "1010");
		descMap.put("交通银行", "1005");
		descMap.put("北京农村商业银行", "1103");
	}
	public static Map<String, String> desc() {
		return descMap;
	}
	
	
}
