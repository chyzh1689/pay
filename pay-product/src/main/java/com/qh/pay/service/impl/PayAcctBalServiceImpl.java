package com.qh.pay.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qh.pay.dao.PayAcctBalMapper;
import com.qh.pay.domain.PayAcctBal;
import com.qh.pay.service.PayAcctBalService;



@Service
public class PayAcctBalServiceImpl implements PayAcctBalService {
	@Autowired
	private PayAcctBalMapper payAcctBalDao;
	
	
	@Override
	public List<PayAcctBal> list(Map<String, Object> map){
		return payAcctBalDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return payAcctBalDao.count(map);
	}
	
}
