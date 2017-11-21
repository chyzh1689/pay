package com.qh.pay.dao;

import com.qh.pay.domain.RecordMerchBalDO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 启晗商户余额流水
 * @author chyzh
 * @email 3048427407@qq.com
 * @date 2017-11-14 11:32:01
 */
@Mapper
public interface RecordMerchBalDao {

	RecordMerchBalDO get(@Param("orderNo")String orderNo,@Param("merchNo")String merchNo,@Param("feeType")String feeType);
	
	List<RecordMerchBalDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(RecordMerchBalDO recordMerchBal);
	
	int update(RecordMerchBalDO recordMerchBal);
	
	int remove(@Param("orderNo")String orderNo,@Param("merchNo")String merchNo,@Param("feeType")String feeType);
	
	int batchRemove(String[] orderNos);
}
