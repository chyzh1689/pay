package com.qh.pay.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.qh.pay.domain.PayAcctBal;

/**
 * @ClassName PayAcctBalMapper
 * @Description 账户余额
 * @author chenyuezhi
 * @Date 2017年11月6日 上午11:31:33
 * @version 1.0.0
 */
@Mapper
public interface PayAcctBalMapper {
	
	List<PayAcctBal> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	//根据用户名查询
	PayAcctBal getByUserName(String username);
	
	//平台资金账户
	PayAcctBal singleByType(Integer userType);
	
	//插入
	int save(PayAcctBal payAcctBal);
	
	//更新
	int update(@Param("username") String username, @Param("balance") BigDecimal balance);
	
	//删除
	int remove(String username);
	
	//批量删除
	int batchRemove(String[] usernames);
	
	//资金余额列表 
	List<PayAcctBal> listByType(Integer userType);
}
