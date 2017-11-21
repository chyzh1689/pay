package com.qh.pay.api.constenum;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName AuditResult
 * @Description 审核结果
 * @author chenyuezhi
 * @Date 2017年11月16日 下午3:52:05
 * @version 1.0.0
 */
public enum AuditResult {
	/***等待中***/
	init(0),
	/***审核通过***/
	pass(1),
	/***审核未通过**/
	noPass(2);

	/****审核结果描述****/
	private static final Map<Integer,String> descMap = new HashMap<>(8);
	static{
		descMap.put(init.id(), "等待中");
		descMap.put(pass.id(), "通过");
		descMap.put(noPass.id(), "未通过");
	}
	
	private int id;

	public static Map<Integer, String> desc() {
		return descMap;
	}

	private AuditResult(int id) {
		this.id = id;
	}

	public int id() {
		return id;
	}
}
