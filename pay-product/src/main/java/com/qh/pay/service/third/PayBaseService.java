package com.qh.pay.service.third;

import javax.servlet.http.HttpServletRequest;

import com.qh.common.utils.R;
import com.qh.pay.api.Order;

/**
 * @ClassName PayOrderService
 * @Description 订单支付
 * @author chenyuezhi
 * @Date 2017年11月6日 下午2:45:34
 * @version 1.0.0
 */
public interface PayBaseService {

	/**
	 * @Description 支付调用入口
	 * @param order
	 * @return 
	 */
	Object order( Order order);

	/**
	 * @Description 支付后台回调处理接口
	 * @param order
	 * @param request
	 * @param requestBody
	 * @return
	 */
	R notify(Order order, HttpServletRequest request, String requestBody);

	/**
	 * @Description 支付订单查询
	 * @param order
	 */
	R query(Order order);

	/**
	 * @Description 代付订单
	 * @param order
	 * @return
	 */
	R orderAcp(Order order);


	/**
	 * @Description 代付订单回调通知
	 * @param order
	 * @param request
	 * @param requestBody
	 * @return
	 */
	R notifyAcp(Order order, HttpServletRequest request, String requestBody);

	/**
	 * @Description 代付订单查询
	 * @param order
	 * @return
	 */
	R acpQuery(Order order);


}
