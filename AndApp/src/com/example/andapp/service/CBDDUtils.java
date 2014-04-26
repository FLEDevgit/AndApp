package com.example.andapp.service;

import java.util.Date;

public class CBDDUtils {

	public static Integer getSleepsCountUptoEvent(long eventTimestamp) {
		Date current = new Date();
		Long gapInMillis = eventTimestamp - current.getTime();
		if(gapInMillis<=0){
			return 0;
		}
		//returns days
		return (int) Math.ceil(gapInMillis/1000.0/60.0/60.0/24.0);
	}

	
}
