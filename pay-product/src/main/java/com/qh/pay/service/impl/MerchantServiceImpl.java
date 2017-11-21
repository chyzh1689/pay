package com.qh.pay.service.impl;

import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qh.common.config.CfgKeyConst;
import com.qh.common.config.Constant;
import com.qh.common.utils.ShiroUtils;
import com.qh.pay.api.constenum.UserType;
import com.qh.pay.api.utils.Md5Util;
import com.qh.pay.api.utils.ParamUtil;
import com.qh.pay.api.utils.QhPayUtil;
import com.qh.pay.dao.MerchantMapper;
import com.qh.pay.domain.Merchant;
import com.qh.pay.domain.PayAcctBal;
import com.qh.pay.service.MerchantService;
import com.qh.redis.RedisConstants;
import com.qh.redis.service.RedisUtil;
import com.qh.system.dao.UserDao;
import com.qh.system.dao.UserRoleDao;
import com.qh.system.domain.UserDO;



@Service
public class MerchantServiceImpl implements MerchantService {
	@Autowired
	private MerchantMapper merchantDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private UserRoleDao userRoleDao;
	
	
	/**
	 * 先从缓存中获取，缓存中没有再从数据库中同步
	 */
	@Override
	public Merchant get(String merchNo) {
		Merchant merchant = (Merchant) RedisUtil.getRedisTemplate().opsForHash().get(RedisConstants.cache_merchant, merchNo);
		if(merchant == null){
			merchant = merchantDao.getByMerchNo(merchNo);
			if(merchant != null){
				RedisUtil.getRedisTemplate().opsForHash().put(RedisConstants.cache_merchant, merchNo, merchant);
			}
		}
		return merchant;
	}
	
	@Override
	public List<Merchant> list(Map<String, Object> map){
		List<Merchant> merchants =  merchantDao.list(map);
		//同步缓存中的余额
		for (Merchant merchant : merchants) {
			syncBalanceFromCache(merchant);
		}
		return merchants;
	}
	
	public void syncBalanceFromCache(Merchant merchant){
		PayAcctBal acctBal =  (PayAcctBal) RedisUtil.getRedisTemplate().opsForHash().get(RedisConstants.cache_bal_merch, merchant.getMerchNo());
		if(acctBal != null){
			merchant.setBalance(acctBal.getBalance());
		}
	}
	
	@Override
	public int count(Map<String, Object> map){
		return merchantDao.count(map);
	}
	
	@Override
	@Transactional
	public int save(Merchant merchant){
		if(merchantDao.exist(merchant.getMerchNo()) == 1){
			return Constant.data_exist;
		}
		UserDO user = userDao.getByUserName(merchant.getMerchNo());
		if(user != null){
			return Constant.data_exist;
		}
		if((user = createUserForMerchant(merchant))  != null){
			merchant.setUserId(user.getUserId());
			int count = merchantDao.save(merchant);
			if(count == 1){
				RedisUtil.getRedisTemplate().opsForHash().put(RedisConstants.cache_merchant, merchant.getMerchNo(), merchant);	
				RedisUtil.getRedisTemplate().opsForHash().put(RedisConstants.cache_bal_merch, merchant.getMerchNo(), MerchantService.createPayAcctBal(merchant));
			}
			return count;
		}else{
			return 0;
		}
	}
	
	private UserDO createUserForMerchant(Merchant merchant){
		UserDO user = new UserDO();
		user.setUserIdCreate(ShiroUtils.getUserId());
		user.setUsername(merchant.getMerchNo());
		user.setPassword(Md5Util.MD5(RedisUtil.getSysConfigValue(CfgKeyConst.pass_default_merch)));
		user.setName(merchant.getName());
		String state  = RedisUtil.getSysConfigValue(CfgKeyConst.state_default_merch);
		if(ParamUtil.isNotEmpty(state)){
			user.setStatus(Integer.parseInt(state));
		}
		user.setUserType(UserType.merch.id());
		if(userDao.save(user) > 0){
			return user;
		}else{
			return null;
		}
	}
	
	
	@Override
	public int update(Merchant merchant){
		int count =  merchantDao.update(merchant);
		Merchant redisMerchant = (Merchant) RedisUtil.getRedisTemplate().opsForHash().get(RedisConstants.cache_merchant, merchant.getMerchNo());
		if(redisMerchant != null){
			if(ParamUtil.isNotEmpty(merchant.getName())){
				redisMerchant.setName(merchant.getName());
			}
			if(ParamUtil.isNotEmpty(merchant.getAgentUser())){
				redisMerchant.setAgentUser(merchant.getAgentUser());
			}
			if(ParamUtil.isNotEmpty(merchant.getPublicKey())){
				redisMerchant.setPublicKey(merchant.getPublicKey());
			}
			RedisUtil.getRedisTemplate().opsForHash().put(RedisConstants.cache_merchant, merchant.getMerchNo(), merchant);		
		}
		return count;
	}
	
	@Override
	public int remove(String merchNo){
		RedisUtil.getRedisTemplate().opsForHash().delete(RedisConstants.cache_merchant, merchNo);
		RedisUtil.getRedisTemplate().opsForHash().delete(RedisConstants.cache_bal_merch, merchNo);
		userRoleDao.removeByUsername(merchNo);
		userDao.removeByUsername(merchNo);
		return merchantDao.removeByMerchNo(merchNo);
	}
	
	@Override
	public int batchRemove(String[] merchNos){
		RedisUtil.getRedisTemplate().opsForHash().delete(RedisConstants.cache_merchant, (Object[])merchNos);
		RedisUtil.getRedisTemplate().opsForHash().delete(RedisConstants.cache_bal_merch, (Object[])merchNos);
		userRoleDao.batchRemoveByUsername(merchNos);
		userDao.batchRemoveByUsername(merchNos);
		return merchantDao.batchRemoveByMerchNo(merchNos);
	}

	/* (非 Javadoc)
	 * Description:
	 * @see com.qh.pay.service.MerchantService#defaultMerchantNo()
	 */
	@Override
	public String defaultMerchantNo() {
		String merchNo = QhPayUtil.merchNoPrefix + ParamUtil.generateCode();
		while (exist(merchNo)) {
			merchNo = QhPayUtil.merchNoPrefix + ParamUtil.generateCode();
		}
		return merchNo;
	}

	/* (非 Javadoc)
	 * Description:
	 * @see com.qh.pay.service.MerchantService#exist(java.lang.String)
	 */
	@Override
	public boolean exist(String merchNo) {
		return RedisUtil.getRedisTemplate().boundValueOps(
				RedisConstants.cache_merchant).getOperations().boundValueOps(merchNo).get()!= null;
	}
}
