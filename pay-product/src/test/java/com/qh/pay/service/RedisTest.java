package com.qh.pay.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSON;
import com.qh.pay.api.Order;
import com.qh.pay.domain.Merchant;
import com.qh.pay.domain.PayAcctBal;
import com.qh.redis.RedisConstants;
import com.qh.redis.service.RedisUtil;
import com.qh.redis.service.RedissonLockUtil;
import com.qh.system.domain.ConfigDO;


/**
  * @ClassName: RedisTest
  * @Description: redis 测试
  * @author chyzh
  * @date 2017年10月26日 下午2:29:47
  *
  */
@RunWith(SpringRunner.class)
@SpringBootTest
@ComponentScan("com.qh.redis")
public class RedisTest {
	
	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(RedisTest.class);
	
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	@Autowired
	private RedissonClient redissonClient;
	@Test
	public void redis_set(){
		Merchant merchant = new Merchant();
        merchant.setMerchNo("chdx");
        merchant.setName("陈大侠");
        merchant.setBalance(new BigDecimal("11111"));
		redisTemplate.boundValueOps("chdx").set(merchant);
	}
	@Test
	public void redis_get(){
		Merchant merchant = (Merchant) redisTemplate.boundValueOps("chdx").get();
		if(merchant != null){
			LOGGER.info("chdx:name:" + merchant.getName());
		}
	}
	
	@Test
	public void set_get_time(){
		long start = System.currentTimeMillis();
		for(int i=0;i<100;i++){
			redisTemplate.opsForHash().put("test", "key" + i, "value" + i);
			redisTemplate.boundHashOps("test").getOperations().boundValueOps("key" + i).expire(20, TimeUnit.SECONDS);
		}
		System.out.println("set_get_time:set" + (System.currentTimeMillis() - start));
		redisTemplate.expire("test", 20, TimeUnit.SECONDS);
		start = System.currentTimeMillis();
		for(int i=0;i<100;i++){
			redisTemplate.opsForHash().get("test", "key" + i);
		}
		System.out.println("set_get_time:get" + (System.currentTimeMillis() - start));
	}
	
	@Test
	public void redis_clientSet(){
		Set<String> myset = redissonClient.getSet("MySet");
        myset.add("陈大侠");
        myset.add("chdx");
	}
	
	@Test
	public void redis_clientGet(){
		Set<String> myset = redissonClient.getSet("MySet");
		LOGGER.info(myset.toString());
	}
	
	
	@Test
	public void redis_lock(){
		RLock lock = RedissonLockUtil.getLock("chdx");
		lock.lock();
	}
	
	@Test
	public void redis_unlock(){
		RLock lock = RedissonLockUtil.getLock("chdx");
		lock.unlock();
	}
	
	@Test
	public void redis_config(){
		BoundValueOperations<String, Object>  sb = redisTemplate.boundValueOps(RedisConstants.cache_config);
		ConfigDO config= (ConfigDO) sb.getOperations().boundValueOps("payCompany_tfb").get();
		LOGGER.info("redis_config:" + config);
		LOGGER.info("redis-hashops:{}",redisTemplate.boundHashOps(RedisConstants.cache_config).get("payCompany_sb"));
		LOGGER.info("redis-hashops:{}",redisTemplate.boundHashOps(RedisConstants.cache_config).get("payCompany_tfb"));
		LOGGER.info("redis-valueops:{}",redisTemplate.boundValueOps(RedisConstants.cache_config).getOperations().boundValueOps("payCompany_sb").get());
	}
	
	@Test
	public void redis_config_del(){
		BoundValueOperations<String, Object>  sb = redisTemplate.boundValueOps(RedisConstants.cache_config);
		sb.getOperations().delete("payCompany_xx");
	}
	
	
	@Test
	public void redis_payFoundAcct(){
		PayAcctBal payAcctBal = RedisUtil.getPayFoundBal();
		LOGGER.info("redis_payFoundAcct_" + payAcctBal.getUsername() + ":" + payAcctBal.getBalance());
		payAcctBal.setBalance(BigDecimal.TEN);
		RedisUtil.setPayFoundBal(payAcctBal);
	}
	
	@Test
	public void redis_publish(){
		long start = System.currentTimeMillis();
		Order order = new Order();
		order.setAcctName("陈大侠");
		order.setMerchNo("QH00000");;
		Map<String,String> result = new HashMap<String,String>();
		result.put("msg", "sb");
		for (int i = 0;i<1;i++) {
			redisTemplate.convertAndSend(RedisConstants.channel_order,JSON.toJSONString(order));
			redisTemplate.convertAndSend(RedisConstants.channel_order_acp, order);
		}
		System.out.println("总耗时间：：：：：：" + (System.currentTimeMillis() - start));
	}
	
	@Test
	public void redis_keyLike(){
	}
}
