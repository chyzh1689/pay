package com.qh.pay.service.third.impl;


import javax.servlet.http.HttpServletRequest;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qh.common.utils.R;
import com.qh.pay.api.Order;
import com.qh.pay.api.constenum.PayCompany;
import com.qh.pay.service.third.PayBaseService;
import com.qh.pay.service.third.bopay.BopayService;

/**
 * @ClassName PayBaseServiceImpl
 * @Description 对接支付基础类
 * @author chenyuezhi
 * @Date 2017年11月8日 下午5:21:06
 * @version 1.0.0
 */
@Service
public class PayBaseServiceImpl implements PayBaseService{
	
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(PayBaseServiceImpl.class);

	@Autowired
	private BopayService bopayService;

	/* (非 Javadoc)
	 * Description:
	 * @see com.qh.pay.service.third.PayBaseService#order(com.qh.pay.domain.PayConfigCompanyDO, com.qh.pay.api.Order)
	 */
	@Override
	public Object order(Order order) {
		Object result = null;
		switch (PayCompany.payCompany(order.getPayCompany())) {
			case bopay:
				result = bopayService.order(order);
				break;
			default:
				logger.error("未找到支付公司！");
				result = R.error("未找到支付公司！");
				break;
		}
		return result;
	}

	/* (非 Javadoc)
	 * Description:
	 * @see com.qh.pay.service.third.PayBaseService#notify(com.qh.pay.api.Order, javax.servlet.http.HttpServletRequest, java.lang.String)
	 */
	@Override
	public R notify(Order order, HttpServletRequest request, String requestBody) {
		R result = null;
		switch (PayCompany.payCompany(order.getPayCompany())) {
			case bopay:
				result = bopayService.notify(order, requestBody);
				break;
			default:
				logger.error("未找到支付公司！");
				result = R.error("未找到支付公司！");
				break;
		}
		return result;
	}

	/* (非 Javadoc)
	 * Description:
	 * @see com.qh.pay.service.third.PayBaseService#query(com.qh.pay.api.Order)
	 */
	@Override
	public R query(Order order) {
		R result = null;
		switch (PayCompany.payCompany(order.getPayCompany())) {
			case bopay:
				result = bopayService.query(order);
				break;
			default:
				logger.error("未找到支付公司！");
				result = R.error("未找到支付公司！");
				break;
		}
		return result;
		
	}

	/* (非 Javadoc)
	 * Description:
	 * @see com.qh.pay.service.third.PayBaseService#orderAcp(com.qh.pay.api.Order)
	 */
	@Override
	public R orderAcp(Order order) {
		R result = null;
		switch (PayCompany.payCompany(order.getPayCompany())) {
			case bopay:
				result = bopayService.orderAcp(order);
				break;
			default:
				logger.error("未找到支付公司！");
				result = R.error("未找到支付公司！");
				break;
		}
		return result;
	}

	/* (非 Javadoc)
	 * Description:
	 * @see com.qh.pay.service.third.PayBaseService#notifyAcp(com.qh.pay.api.Order, javax.servlet.http.HttpServletRequest, java.lang.String)
	 */
	@Override
	public R notifyAcp(Order order, HttpServletRequest request, String requestBody) {
		R result = null;
		switch (PayCompany.payCompany(order.getPayCompany())) {
			case bopay:
				result = bopayService.notifyAcp(order, requestBody);
				break;
			default:
				logger.error("未找到支付公司！");
				result = R.error("未找到支付公司！");
				break;
		}
		return result;
	}

	/* (非 Javadoc)
	 * Description:
	 * @see com.qh.pay.service.third.PayBaseService#acpQuery(com.qh.pay.api.Order)
	 */
	@Override
	public R acpQuery(Order order) {
		R result = null;
		switch (PayCompany.payCompany(order.getPayCompany())) {
			case bopay:
				result = bopayService.acpQuery(order);
				break;
			default:
				logger.error("未找到支付公司！");
				result = R.error("未找到支付公司！");
				break;
		}
		return result;
	}

}
