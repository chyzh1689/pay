package com.qh.pay.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import com.qh.pay.api.Order;
import com.qh.pay.dao.PayOrderAcpDao;
import com.qh.pay.dao.PayOrderDao;

/**
 * @ClassName OrderAcpServiceTest
 * @Description 订单测试
 * @author chenyuezhi
 * @Date 2017年11月20日 下午4:17:50
 * @version 1.0.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ComponentScan("com.qh.pay")
public class OrderServiceTest {
	 private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(MerchantServiceTest.class);
	 
	@Autowired
	private PayOrderAcpDao payOrderAcpDao;
	@Autowired
	private PayOrderDao payOrderDao;
	
	public final static String merchNo = "QH0000";
	
	@Test
	public void save_order(){
		LOGGER.info("保存order");
		Order order = new Order();
		String reqTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		order.setOrderNo(reqTime + new Random().nextInt(10000));
		order.setMerchNo(merchNo);
		payOrderDao.save(order);
	}
	
	@Test
	public void save_order_acp(){
		LOGGER.info("保存acp_order");
		Order order = new Order();
		String reqTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		order.setOrderNo(reqTime + new Random().nextInt(10000));
		order.setMerchNo(merchNo);
		payOrderAcpDao.save(order);
	}
	
}
