package com.qh.pay.dao;

import com.qh.pay.domain.MerchUserSignDO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 商户号下的用户签约信息
 * @author chyzh
 * @email 3048427407@qq.com
 * @date 2017-11-02 11:21:44
 */
@Mapper
public interface MerchUserSignDao {

	MerchUserSignDO get(String merchNo);
	
	List<MerchUserSignDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(MerchUserSignDO merchUserSign);
	
	int update(MerchUserSignDO merchUserSign);
	
	int remove(String merch_no);
	
	int batchRemove(String[] merchNos);
}
