package com.example.andapp.service;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class CBDDUtils {

	public static Integer getSleepsNrToYearMonthDay(int year, int month, int dayOfMonth) {
		Calendar currentCal = new GregorianCalendar();
		Calendar eventCal = new GregorianCalendar(year,month,dayOfMonth);
		Long gapInMillis = eventCal.getTimeInMillis()-currentCal.getTimeInMillis();
		if(gapInMillis<=0){
			return 0;
		}
		//returns days
		return (int) Math.ceil(gapInMillis/1000.0/60.0/60.0/24.0);
	}

	
}
