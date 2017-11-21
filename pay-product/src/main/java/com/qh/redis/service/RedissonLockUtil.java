package com.qh.redis.service;

import org.redisson.api.RLock;

import com.qh.redis.RedisConstants;

public class RedissonLockUtil {
	
    private static RedissonLocker redissLock;
    
    public static void setLocker(RedissonLocker locker) {
        redissLock = locker;
    }
    
    public static RLock getLock(String lockKey){
		return redissLock.getLock(lockKey);
	}
    
    public static RLock getOrderLock(String lockKey){
		return redissLock.getLock(RedisConstants.lock_order + lockKey);
	}
    public static RLock getOrderLock(String merchNo, String orderNo){
		return redissLock.getLock(RedisConstants.lock_order + merchNo + RedisConstants.link_symbol + orderNo);
	}
    
    public static RLock getOrderAcpLock(String lockKey){
		return redissLock.getLock(RedisConstants.lock_order_acp + lockKey);
	}
    public static RLock getOrderAcpLock(String merchNo, String orderNo){
		return redissLock.getLock(RedisConstants.lock_order_acp + merchNo + RedisConstants.link_symbol + orderNo);
	}
    
    public static RLock getBalMerchLock(String lockKey){
		return redissLock.getLock(RedisConstants.lock_bal_merch + lockKey);
	}
    
    public static RLock getBalFoundAcctLock(String lockKey){
		return redissLock.getLock(RedisConstants.lock_bal_foundAcct + lockKey);
	}
    
    public static RLock getBalFoundAcctLock(){
		return redissLock.getLock(RedisConstants.lock_bal_foundAcct);
	}
}