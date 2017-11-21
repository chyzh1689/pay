package com.qh.pay.service;

import com.qh.pay.domain.PayAcctBal;

import java.util.List;
import java.util.Map;

/**
 * 账号余额表
 * 
 * @author chyzh
 * @email 3048427407@qq.com
 * @date 2017-11-06 11:41:35
 */
public interface PayAcctBalService {
	
	
	List<PayAcctBal> list(Map<String, Object> map);

	/**
	 * @Description (TODO这里用一句话描述这个方法的作用)
	 * @param map
	 * @return
	 */
	int count(Map<String, Object> map);
}
