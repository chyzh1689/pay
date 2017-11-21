package com.qh.pay.api.constenum;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName CardType
 * @Description 银行卡类型
 * @author chenyuezhi
 * @Date 2017年10月31日 上午11:06:36
 * @version 1.0.0
 */
public enum CardType {
	savings(0),
	credit(1);
	private static final Map<Integer,String> descMap = new HashMap<>(4);
	static{
		descMap.put(savings.id(), "储蓄卡");
		descMap.put(credit.id(), "信用卡");
	}
	private int id;

	public static Map<Integer, String> desc() {
		return descMap;
	}

	private CardType(int id) {
		this.id = id;
	}

	public int id() {
		return id;
	}
	
}
