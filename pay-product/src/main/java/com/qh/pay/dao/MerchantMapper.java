package com.qh.pay.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.qh.pay.domain.Merchant;

@Mapper
public interface MerchantMapper {
    Merchant get(Integer id);
	
    Merchant getByMerchNo(String merchNo);
    
	List<Merchant> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int exist(String merchNo);
	
	int save(Merchant merchant);
	
	int update(Merchant merchant);
	
	int remove(Integer id);
	
	int batchRemove(Integer[] ids);
	
	int removeByMerchNo(String merchNo);
	
	int batchRemoveByMerchNo(String[] merchNos);

}
