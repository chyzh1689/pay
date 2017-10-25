package com.qh.pay.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.qh.pay.entity.Merchant;

@Mapper
public interface MerchantMapper {
    @Select("select * from merchant where id=#{id}")
    public Merchant findMerchantById(Long id);

    public List<Merchant> findAllMerchants();

    @Insert("INSERT INTO merchant(username, name, md5Key, balance) VALUES(#{username}, #{name}, #{md5Key}, #{balance})")
    public int insertMerchant(Merchant merchant);

}
