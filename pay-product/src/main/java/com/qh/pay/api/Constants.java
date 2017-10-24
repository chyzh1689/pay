package com.qh.pay.api;

/**
 * 
 * @ClassName Constants
 * @Description 用到的常量类
 * @author chenyuezhi
 * @Date 2017年5月30日 下午3:13:53
 * @version 1.0.0
 */
public class Constants {
	/*** 返回码定义 *****/
	public static final String result_code = "code";
	/*** 返回消息 **/
	public static final String result_msg = "msg";
	/*** 返回成功值：0 ******/
	public static final int result_code_succ = 0;
	/*** 返回失败值：1 *****/
	public static final int result_code_error = 1;
	/*** 返回成功信息 success ***/
	public static final String result_msg_succ = "success";
	/*** 返回数据 data ****/
	public static final String result_data = "data";

	/**** 时间格式--yyyyMMddHHmmss **/
	public static final String date_fmt_yyyyMMddHHmmss = "yyyyMMddHHmmss";
	/**** 时间格式--yyyyMMdd **/
	public static final String date_fmt_yyyyMMdd = "yyyyMMdd";
	/*** 时间格式--yyyy-MM-dd HH:mm:ss */
	public static final String date_fmt_yyMMddHH_mm_ss = "yyyy-MM-dd HH:mm:ss";
}
