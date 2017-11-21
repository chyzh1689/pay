package com.qh.pay.api.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @ClassName DateUtil
 * @Description 时间操作工具类
 * @author chenyuezhi
 * @Date 2017年11月3日 下午3:12:20
 * @version 1.0.0
 */
public class DateUtil {
	/**** 时间格式--yyyyMMddHHmmss **/
	public static final String date_fmt_yyyyMMddHHmmss = "yyyyMMddHHmmss";
	/**** 时间格式--yyyyMMdd **/
	public static final String date_fmt_yyyyMMdd = "yyyyMMdd";
	/*** 时间格式--yyyy-MM-dd HH:mm:ss */
	public static final String date_fmt_yyMMddHH_mm_ss = "yyyy-MM-dd HH:mm:ss";
	/***日期格式 yyyy-MM-dd ***/
	public static final String date_fmt_yyyy_MM_dd = "yyyy-MM-dd";
	/**时间格式 HH:mm***/
	public static final String time_fmt_HH_mm = "HH:mm";
	/**时间格式 HH:mm:ss***/
	public static final String time_fmt_HH_mm_ss = "HH:mm:ss";
	
	private static SimpleDateFormat sdf = new SimpleDateFormat(date_fmt_yyMMddHH_mm_ss);
	private static SimpleDateFormat sdfDate = new SimpleDateFormat(date_fmt_yyyyMMdd);
	private static SimpleDateFormat sdfNumber = new SimpleDateFormat(date_fmt_yyyyMMddHHmmss);
	private static SimpleDateFormat sdf_Date = new SimpleDateFormat(date_fmt_yyyy_MM_dd);
	private static SimpleDateFormat sdfTime = new SimpleDateFormat(time_fmt_HH_mm);
	private static SimpleDateFormat sdfTimeSec = new SimpleDateFormat(time_fmt_HH_mm_ss);
	
	/**
	 * 
	 * @Description 日期格式 yyyy-MM-dd
	 * @param source
	 * @return
	 */
	public static Date parseDate(String source){
		try {
			return sdf_Date.parse(source);
		} catch (ParseException e) {
		}
		return null;
	}
	/**
	 * @Description 返回时间格式 HH:mm
	 * @param source
	 * @return
	 */
	public static Date parseTime(String source) {
		try {
			return sdfTime.parse(source);
		} catch (ParseException e) {
		}
		return null;
	}
	/**
	 * @Description 返回时间秒格式 HH:mm:ss
	 * @param source
	 * @return
	 */
	public static Date parseTimeSec(String source) {
		try {
			return sdfTimeSec.parse(source);
		} catch (ParseException e) {
		}
		return null;
	}
	
	/**
	 * @Description 返回时间秒格式 HH:mm:ss
	 * @param source
	 * @return
	 */
	public static Date parseDateTime(String source){
		try {
			return sdf.parse(source);
		} catch (ParseException e) {
		}
		return null;
	}
	/**
	 * 
	 * @Description 返回标准时间格式 yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String getCurrentStr() {
		return sdf.format(new Date());
	}

	/**
	 * 
	 * @Description 返回标准时间格式
	 * @param date
	 * @return
	 */
	public static String getCurrentStr(Date date) {
		return sdf.format(date);
	}

	/**
	 * 
	 * @Description 返回日期格式
	 * @return
	 */
	public static String getCurrentDateStr() {
		return sdfDate.format(new Date());
	}

	/**
	 * 
	 * @Description 返回日期格式
	 * @param date
	 * @return
	 */
	public static String getCurrentDateStr(Date date) {
		return sdfDate.format(date);
	}

	/**
	 * 
	 * @Description 返回纯数字格式
	 * @return
	 */
	public static String getCurrentNumStr() {
		return sdfNumber.format(new Date());
	}

	/**
	 * 
	 * @Description 返回纯数字格式
	 * @param date
	 * @return
	 */
	public static String getCurrentNumStr(Date date) {
		return sdfNumber.format(date);
	}

	/**
	 * 
	 * @Description 获取倒计时
	 * @param date
	 * @return
	 */
	public static Integer getCountdown(long time, Date date) {
		if (date.getTime() < time) {
			return 0;
		}
		return (int) ((date.getTime() - time) / 1000);
	}

	/****
	 * 
	 * @Description 获取当前时间的起止 00:00:00
	 * @param args
	 * @throws ParseException
	 */
	public static int getBeginTimeIntZero(Date date) {
		if (date == null) {
			date = new Date();
		}
		try {
			return (int) (sdfDate.parse(sdfDate.format(date)).getTime()/1000);
		} catch (ParseException e) {

		}
		return 0;
	}
	/****
	 * 
	 * @Description 获取当前时间的起止 00:00:00
	 * @param args
	 * @throws ParseException
	 */
	public static int getBeginTimeIntZero(String dateStr) {
		try {
			if (ParamUtil.isNotEmpty(dateStr)) {
				return (int) (sdfDate.parse(dateStr).getTime()/1000);
			}else{
				return (int) (sdfDate.parse(sdfDate.format(new Date())).getTime()/1000);
			}
		} catch (ParseException e) {

		}
		return 0;
	}
	/****
	 * 
	 * @Description 获取当前时间的起止 23:59:59
	 * @param args
	 * @throws ParseException
	 */
	public static int getEndTimeIntLast(String dateStr) {
		return getBeginTimeIntZero(dateStr) + 23*60*60 -1;
	}
	/****
	 * 
	 * @Description 获取当前时间的起止 00:00:00
	 * @param args
	 * @throws ParseException
	 */
	public static int getBeginTimeIntZero() {
		try {
			return (int) (sdfDate.parse(sdfDate.format(new Date())).getTime()/1000);
		} catch (ParseException e) {

		}
		return 0;
	}
	
	/****
	 * 
	 * @Description 获取当前时间的起止 00:00:00
	 * @param args
	 * @throws ParseException
	 */
	public static Date getBeginTimeZero(Date date) {
		if (date == null) {
			date = new Date();
		}
		try {
			return sdfDate.parse(sdfDate.format(date));
		} catch (ParseException e) {

		}
		return null;
	}

	/****
	 * 
	 * @Description 获取当前时间的起止 23:59:59
	 * @param args
	 * @throws ParseException
	 */
	public static int getEndTimeIntLast(Date date) {
		return getBeginTimeIntZero(date) + 23*60*60 -1;
	}
	
	/***
	 * 获取当前时间秒
	 */
	public static int getCurrentTimeInt(){
		return (int) (System.currentTimeMillis()/1000);
	}
	
	
	/****
	 * 
	 * @Description 获取当前时间的起止 23:59:59
	 * @param args
	 * @throws ParseException
	 */
	public static int getEndTimeIntLast(int zeroTime) {
		return zeroTime + 23*60*60 -1;
	}
	
	/****
	 * 
	 * @Description 获取当前时间的起止 23:59:59
	 * @param args
	 * @throws ParseException
	 */
	public static int getEndTimeIntLast() {
		return getBeginTimeIntZero(new Date()) + 23*60*60 -1;
	}
	/****
	 * 
	 * @Description 获取当前时间的起止 23:59:59
	 * @param args
	 * @throws ParseException
	 */
	public static Date getEndTimeLast(Date date) {
		if (date == null) {
			date = new Date();
		}
		try {
			return sdfNumber.parse(sdfNumber.format(date).substring(0, 8) + "235959");
		} catch (ParseException e) {

		}
		return null;
	}

	/**
	 * 
	 * @Description (获取当前分钟)
	 * @return
	 */
	public static int getCurrentMinute(Calendar c) {
		return c.get(Calendar.HOUR_OF_DAY) * 60 + c.get(Calendar.MINUTE);
	}

	/***
	 * 
	 * @Description 当前分钟 在不在 时间段，如果在 返回true,否则 返回false
	 * @param intPeriod 90-150,270-390
	 * @param minute
	 * @return
	 */
	public static boolean ifMinuteInPeriod(String intPeriod, int minute) {
		if(ParamUtil.isEmpty(intPeriod)){
			return true;
		}
		boolean result = false;
		String[] datas = intPeriod.split(",");
		String[] periods;
		for (String data : datas) {
			if (ParamUtil.isNotEmpty(data)) {
				periods = data.split("-");
				if(periods.length == 2) {
					if (minute >= Integer.parseInt(periods[0]) && 
							minute < Integer.parseInt(periods[1])) {
						result = true;
						break;
					}
				}
			}
		}
		return result;
	}
	

	/**
	 * 
	 * @Description 时间字符串转化为分钟 01:30-->90
	 * @param timeStr
	 * @return
	 */
	public static int timeToInt(String timeStr) {
		int minute = 0;
		if (ParamUtil.isNotEmpty(timeStr)) {
			String[] timeStrs = timeStr.split(":");
			if (timeStrs.length == 1) {
				return Integer.parseInt(timeStrs[0]) * 60;
			} else if (timeStrs.length == 2) {
				return Integer.parseInt(timeStrs[0]) * 60 + Integer.parseInt(timeStrs[1]);
			}
		}
		return minute;
	}

	/**
	 * @Description 时间格式转int格式 01:30-02:30,04:30-06:30-->90-150,270-390
	 * @param timePeriod
	 * @return
	 */
	public static String timeFormatToInt(String timePeriod) {
		String[] datas = timePeriod.split(",");
		String[] periods;
		int start;
		int end;
		String intPeriod = "";
		for (String data : datas) {
			start = 0;
			if (ParamUtil.isNotEmpty(data)) {
				periods = data.split("-");
				if (periods.length == 2) {
					start = timeToInt(periods[0]);
					end = timeToInt(periods[1]);
					intPeriod += start + "-" + end + ",";
				}
			}
		}
		if(ParamUtil.isNotEmpty(intPeriod)){
			intPeriod = intPeriod.substring(0, intPeriod.length()-1);
		}
		return intPeriod;
	}
	
	/**
	 * 
	 * @Description 整数分钟转换位str 90---->01:30
	 * @param minuteToStr
	 * @return
	 */
	public static String intToTime(String intStr) {
		int minute = 0;
		String minuteStr = "";
		if (ParamUtil.isNotEmpty(intStr)) {
			minute = Integer.parseInt(intStr);
			int hour = minute/60;
			minute = minute%60;
			minuteStr += hour<10?"0"+hour:String.valueOf(hour);
			minuteStr += ":" + (minute<10?"0"+minute:String.valueOf(minute));
		}
		return minuteStr;
	}
	/**
	 * @Description 整数分钟转str格式   90-150,270-390-->01:30-02:30,04:30-06:30
	 * @param timePeriod
	 * @return
	 */
	public static String intFormatToTime(String timePeriod) {
		String[] datas = timePeriod.split(",");
		String[] periods;
		String start;
		String end;
		String intPeriod = "";
		for (String data : datas) {
			if (ParamUtil.isNotEmpty(data)) {
				periods = data.split("-");
				if (periods.length == 2) {
					start = intToTime(periods[0]);
					end = intToTime(periods[1]);
					intPeriod += start + "-" + end + ",";
				}
			}
		}
		if(ParamUtil.isNotEmpty(intPeriod)){
			intPeriod = intPeriod.substring(0, intPeriod.length()-1);
		}
		return intPeriod;
	}
	
	/**
	 * 
	 * @Description (获取当前星期)
	 * @return
	 */
	public static int getCurrentWeekDay(Calendar c) {
		return c.get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * 
	 * @Description (获取当前星期)
	 * @return
	 */
	public static int getCurrentWeekDay() {
		return Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
	}

	/***
	 * 
	 * @Description 解析字符串位时间
	 * @param source
	 * @return
	 */
	public static Date parseDateSource(String source){
		if(source.matches("^((?:19|20)\\d\\d)-(((0[13578]|(10|12))-(0[1-9]|[1-2][0-9]|3[0-1]))|(02-(0[1-9]|[1-2][0-9]))|((0[469]|11)-(0[1-9]|[1-2][0-9]|30)))$")){
    		return DateUtil.parseDate(source);
    	}else if(source.matches("^((?:19|20)\\d\\d)-(((0[13578]|(10|12))-(0[1-9]|[1-2][0-9]|3[0-1]))|(02-(0[1-9]|[1-2][0-9]))|((0[469]|11)-(0[1-9]|[1-2][0-9]|30)))\\s(([1-9]{1})|([0-1][0-9])|([1-2][0-3])):([0-5][0-9]:[0-5][0-9])$")){
    		return DateUtil.parseDateTime(source);
    	}else if(source.matches("^(([1-9]{1})|([0-1][0-9])|([1-2][0-3])):([0-5][0-9])$")){
    		return DateUtil.parseTime(source);
    	}else if(source.matches("^(([1-9]{1})|([0-1][0-9])|([1-2][0-3])):([0-5][0-9]:[0-5][0-9])$")){
    		return DateUtil.parseTimeSec(source);
    	}
		return null;
	}
	
}
