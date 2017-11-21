package com.qh.pay.service;

import java.math.BigDecimal;

import com.alibaba.fastjson.JSONObject;
import com.qh.pay.api.Order;
import com.qh.pay.domain.RecordFoundAcctDO;
import com.qh.pay.domain.RecordMerchBalDO;

/**
 * @ClassName PayFeeService
 * @author chenyuezhi
 * @Date 2017年11月9日 下午10:06:37
 * @version 1.0.0
 */
public interface PayHandlerService {
	/**
	 * @Description 初始化 订单信息
	 * @param order
	 * @param jo
	 * @return
	 */
	String initOrder(Order order, JSONObject jo);

	/**
	 * @Description 商户余额减少
	 * @param order
	 * @param amount
	 * @param feeType
	 * @param orderType
	 * @return
	 */
	RecordMerchBalDO balForMerchSub(Order order, BigDecimal amount, int feeType, int orderType);

	/**
	 * @Description 商户余额增加
	 * @param order
	 * @param amount
	 * @param feeType
	 * @param orderType
	 * @return
	 */
	RecordMerchBalDO balForMerchAdd(Order order, BigDecimal amount, int feeType, int orderType);

	/**
	 * @Description 代理资金减少
	 * @param order
	 * @param amount
	 * @param agentUser
	 * @param feeType
	 * @param orderType
	 * @return
	 */
	RecordFoundAcctDO balForAgentSub(Order order, BigDecimal amount, String agentUser, int feeType, int orderType);

	/**
	 * @Description 代理资金增加
	 * @param order
	 * @param amount
	 * @param agentUser
	 * @param feeType
	 * @param orderType
	 * @return
	 */
	RecordFoundAcctDO balForAgentAdd(Order order, BigDecimal amount, String agentUser, int feeType, int orderType);

	/**
	 * @Description 平台余额减少
	 * @param order
	 * @param amount
	 * @param feeType
	 * @param orderType
	 * @return
	 */
	RecordFoundAcctDO balForPlatSub(Order order, BigDecimal amount, int feeType, int orderType);

	/**
	 * @Description 平台余额增加
	 * @param order
	 * @param amount
	 * @param feeType
	 * @param orderType
	 * @return
	 */
	RecordFoundAcctDO balForPlatAdd(Order order, BigDecimal amount, int feeType, int orderType);


}
