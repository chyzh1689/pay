package com.qh.pay.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.qh.pay.dao.MerchUserSignDao;
import com.qh.pay.domain.MerchUserSignDO;
import com.qh.pay.service.MerchUserSignService;



@Service
public class MerchUserSignServiceImpl implements MerchUserSignService {
	@Autowired
	private MerchUserSignDao merchUserSignDao;
	
	@Override
	public MerchUserSignDO get(String merchNo){
		return merchUserSignDao.get(merchNo);
	}
	
	@Override
	public List<MerchUserSignDO> list(Map<String, Object> map){
		return merchUserSignDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return merchUserSignDao.count(map);
	}
	
	@Override
	public int save(MerchUserSignDO merchUserSign){
		return merchUserSignDao.save(merchUserSign);
	}
	
	@Override
	public int update(MerchUserSignDO merchUserSign){
		return merchUserSignDao.update(merchUserSign);
	}
	
	@Override
	public int remove(String merchNo){
		return merchUserSignDao.remove(merchNo);
	}
	
	@Override
	public int batchRemove(String[] merchNos){
		return merchUserSignDao.batchRemove(merchNos);
	}
	
}
