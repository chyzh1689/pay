package com.qh.pay.api.constenum;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName AcctType
 * @Description 账户性质
 * @author chenyuezhi
 * @Date 2017年10月31日 上午11:20:35
 * @version 1.0.0
 */
public enum AcctType {
	pri(0),pub(1);
	private static final Map<Integer,String> descMap = new HashMap<>(4);
	static{
		descMap.put(pri.id(), "对私");
		descMap.put(pub.id(), "对公");
	}
	public static Map<Integer, String> desc() {
		return descMap;
	}
	
	private int id;

	private AcctType(int id) {
		this.id = id;
	}

	public int id() {
		return id;
	}
}
