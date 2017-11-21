package com.qh.pay.api.constenum;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName OrderType
 * @Description 订单类型
 * @author chenyuezhi
 * @Date 2017年11月3日 上午10:51:21
 * @version 1.0.0
 */
public enum OrderType {
	pay(0),//支付
	acp(1);//代付

	/**** 支付订单状态描述 ****/
	private static final Map<Integer, String> descMap = new HashMap<>(4);
	static {
		descMap.put(pay.id(), "支付");
		descMap.put(acp.id(), "代付");
	}

	private int id;

	private OrderType(int id) {
		this.id = id;
	}

	public int id() {
		return id;
	}

	public static Map<Integer, String> desc() {
		return descMap;
	}
	
}
