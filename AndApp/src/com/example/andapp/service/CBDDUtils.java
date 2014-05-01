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

	public static String getThemeCodeForPosition(int selectedItemPosition,String[] codeArray) {
    	return codeArray[selectedItemPosition];
	}

	public static int getPositionForThemeCode(String widgetThemeCode,String[] stringArray) {
		int index=0;
		int i=0;
		for(String code : stringArray){
			if(code.equals(widgetThemeCode)){
				index=i;
				break;
			}
			i++;
		}
		return index;
	}

	
}
