package com.qh.pay.config;

import java.util.List;

import javax.annotation.PostConstruct;

import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;

import com.qh.pay.api.constenum.UserType;
import com.qh.pay.dao.MerchantMapper;
import com.qh.pay.dao.PayAcctBalMapper;
import com.qh.pay.dao.PayConfigCompanyDao;
import com.qh.pay.dao.PayPropertyDao;
import com.qh.pay.domain.Merchant;
import com.qh.pay.domain.PayAcctBal;
import com.qh.pay.domain.PayConfigCompanyDO;
import com.qh.pay.domain.PayPropertyDO;
import com.qh.pay.service.MerchantService;
import com.qh.redis.RedisConstants;
import com.qh.redis.service.RedisUtil;
import com.qh.redis.service.RedissonLocker;
import com.qh.system.dao.ConfigDao;
import com.qh.system.dao.UserDao;
import com.qh.system.domain.ConfigDO;
import com.qh.system.domain.UserDO;
import com.qh.system.service.UserService;

/**
 * @ClassName PayConfig
 * @Description 支付相关配置
 * @author chenyuezhi
 * @Date 2017年11月6日 下午12:01:19
 * @version 1.0.0
 */
@Configuration
@Order(2)
public class PayConfig {
	
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
	@Autowired
	private RedissonLocker redissonLocker;
	
	@Autowired
	private PayAcctBalMapper payAcctBalMapper;
	
	@Autowired
	private ConfigDao configDao;
	
	@Autowired
	private PayPropertyDao payPropertyDao;
	
	@Autowired
	private MerchantMapper merchantMapper;
	
	@Autowired
	private PayConfigCompanyDao payConfigCompanyDao;
	
	@Autowired
	private UserDao userDao;
	
	@PostConstruct
	public void init(){
		
		RLock lock = redissonLocker.getLock(RedisConstants.lock_bal_foundAcct);
		if(lock.tryLock()){
			try {
				PayAcctBal acctBal =  (PayAcctBal) redisTemplate.opsForValue().get(RedisConstants.cache_bal_foundAcct);
				if(acctBal == null){
					redisTemplate.opsForValue().set(RedisConstants.cache_bal_foundAcct,payAcctBalMapper.singleByType(UserType.foundAcct.id()));
				}
			} finally {
				lock.unlock();
			}
		}
		
		List<ConfigDO> configs = configDao.list(null);
		for (ConfigDO configDO : configs) {
			RedisUtil.syncConfig(configDO, false);
		}
		
		List<PayPropertyDO> payPropertyDOs = payPropertyDao.list(null);
		for (PayPropertyDO payPropertyDO : payPropertyDOs) {
			RedisUtil.syncPayConfig(payPropertyDO);
		}
		
		List<Merchant> merchants = merchantMapper.list(null);
		PayAcctBal acctBal = null;
		for (Merchant merchant : merchants) {
			RedisUtil.getRedisTemplate().opsForHash().put(RedisConstants.cache_merchant, merchant.getMerchNo(), merchant);
			lock = redissonLocker.getLock(RedisConstants.lock_bal_merch + merchant.getMerchNo());
			if(lock.tryLock()){
				try {
					acctBal = (PayAcctBal) RedisUtil.getRedisTemplate().opsForHash().get(RedisConstants.cache_bal_merch, merchant.getMerchNo());
					if(acctBal == null){
						RedisUtil.getRedisTemplate().opsForHash().put(RedisConstants.cache_bal_merch, merchant.getMerchNo(), MerchantService.createPayAcctBal(merchant));
					}
				} finally {
					lock.unlock();
				}
			}
		}
		
		List<UserDO> users = userDao.listByUserType(UserType.agent.id());
		for (UserDO userDO : users) {
			lock = redissonLocker.getLock(RedisConstants.lock_bal_agent + userDO.getUsername());
			if(lock.tryLock()){
				try {
					acctBal = (PayAcctBal) RedisUtil.getRedisTemplate().opsForHash().get(RedisConstants.cache_bal_agent, userDO.getUsername());
					if(acctBal == null){
						RedisUtil.getRedisTemplate().opsForHash().put(RedisConstants.cache_bal_agent, userDO.getUsername(), UserService.createAgentBalFromUser(userDO));
					}
				} finally {
					lock.unlock();
				}
			}
		}
		
		List<PayConfigCompanyDO> configCompanyDOs = payConfigCompanyDao.list(null);
		for (PayConfigCompanyDO payCfgCmp : configCompanyDOs) {
			RedisUtil.getRedisTemplate().opsForHash().put(RedisConstants.cache_payConfigCompany + payCfgCmp.getOutChannel(),
					payCfgCmp.getCompany() + RedisConstants.link_symbol + payCfgCmp.getPayMerch(), payCfgCmp);
		}
		
	}

}
