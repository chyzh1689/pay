package com.qh.redis.config;

import java.util.concurrent.CountDownLatch;

import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import com.qh.common.config.Constant;
import com.qh.common.utils.R;
import com.qh.pay.service.PayService;
import com.qh.redis.RedisConstants;
import com.qh.redis.service.RedisMsg;


/**
 * @ClassName MessageListener
 * @Description 消息监听
 * @author chenyuezhi
 * @Date 2017年11月10日 下午5:53:26
 * @version 1.0.0
 * @param <M>
 */
public class MessageListenerRedis implements MessageListener{

	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(MessageListenerRedis.class);

	private CountDownLatch latch;
	
	private PayService payService;
	
	/**
	 * @Description T
	 * @param latch
	 */
	public MessageListenerRedis(CountDownLatch latch) {
		this.latch = latch;
	}
	
	public void setPayService(PayService payService){
		
	}

	@Override
	public void onMessage(Message message, byte[] pattern) {
		String channel = new String(message.getChannel());
		String msgKey = new String(message.getBody());
		int index = msgKey.indexOf(RedisConstants.link_symbol);
		String merchNo = msgKey.substring(0,index);
    	String orderNo = msgKey.substring(index+1);
		logger.info("MessageListenerRedis"+ ":" + channel + ":" + merchNo + ":" + orderNo);
		//支付回调
		if(RedisConstants.channel_order_notify.equals(channel)){
			String result = payService.orderNotifyMsg(merchNo,orderNo);
			if(Constant.result_msg_succ.equalsIgnoreCase(result) || Constant.result_msg_ok.equalsIgnoreCase(result)){
				RedisMsg.orderDataMsg(merchNo,orderNo);
			}
		//支付保存数据	
		}else if(RedisConstants.channel_order_data.equals(channel)){
			payService.orderDataMsg(merchNo,orderNo);
		//代付未通过
		}else if(RedisConstants.channel_order_acp_nopass.equals(channel)){
			payService.orderAcpNopassDataMsg(merchNo,orderNo);
		//代付发起
		}else if(RedisConstants.channel_order_acp.equals(channel)){
			R r = payService.orderAcp(merchNo,orderNo);
			if(r != null && R.ifError(r)){
				RedisMsg.orderAcpNotifyMsg(merchNo,orderNo);
			}
		//代付回调	
		}else if(RedisConstants.channel_order_acp_notify.equals(channel)){
			String result = payService.orderAcpNotifyMsg(merchNo,orderNo);
			if(Constant.result_msg_succ.equalsIgnoreCase(result) || Constant.result_msg_ok.equalsIgnoreCase(result)){
				RedisMsg.orderAcpDataMsg(merchNo,orderNo);
			}
		//代付保存数据	
		}else if(RedisConstants.channel_order_acp_data.equals(channel)){
			payService.orderAcpDataMsg(merchNo,orderNo);
		}
		latch.countDown();
	}

}
