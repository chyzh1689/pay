package com.qh.pay.api.constenum;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName FeeType
 * @Description 费用类型
 * @author chenyuezhi
 * @Date 2017年11月14日 下午3:24:48
 * @version 1.0.0
 */
public enum FeeType {
	/****平台下单收入 手续费用***/
	platIn(0),
	/****平台代付收入 手续费用***/
	platAcpIn(1), 
	/****商户下单收入 余额增加***/
	merchIn(2), 
	/****商户代付支出 余额扣减****/
	merchAcpOut(3),
	/****商户代付失败 余额返还**/
	merchAcpFail(4),
	/****商户代付未通过 余额返还**/
	merchAcpNopass(5),
	/****商户代理下单收入 手续费用***/
	agentIn(6), 
	/****商户代理代付收入 手续费用***/
	agentAcpIn(7);

	/**** 费用类型描述 ****/
	private static final Map<Integer, String> descMap = new HashMap<>(8);
	static {
		descMap.put(platIn.id(), "平台下单收入");
		descMap.put(platAcpIn.id(), "平台代付收入");
		descMap.put(merchIn.id(), "商户下单收入");
		descMap.put(merchAcpOut.id(), "商户代付支出");
		descMap.put(merchAcpFail.id(), "商户代付失败返还");
		descMap.put(merchAcpNopass.id(), "商户代付审核返还");
		descMap.put(agentIn.id(), "商户代理下单收入");
		descMap.put(agentAcpIn.id(), "商户代理代付收入");
	}

	private int id;

	private FeeType(int id) {
		this.id = id;
	}

	public int id() {
		return id;
	}

	public static Map<Integer, String> desc() {
		return descMap;
	}
}
