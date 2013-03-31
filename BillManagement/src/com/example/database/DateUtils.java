package com.example.database;

import java.security.InvalidParameterException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
	
	private static DateFormat df = new SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH);
	
	public static Date parse(String dateString) throws InvalidParameterException {
	    try {
		    return df.parse(dateString);
	    } catch (ParseException e) {
	    	throw new InvalidParameterException("Date [" + dateString + "] cannot be parsed.");
	    }
	}
	
	public static String format(Date date) {
		return df.format(date);
	}
}
