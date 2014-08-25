package org.zhouli.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
	
	public static final String getNowDate(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy��MM��dd��");
		Date dates = new Date(System.currentTimeMillis());
		String dateStr = sdf.format(dates);
		return dateStr;
	}
	
	//获取当前日期
	public static Integer getNowTimes(){
		SimpleDateFormat sdf = new SimpleDateFormat("HH");
		return Integer.valueOf(sdf.format(new Date()));
	}

}
