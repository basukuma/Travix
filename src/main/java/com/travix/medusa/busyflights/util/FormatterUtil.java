package com.travix.medusa.busyflights.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for date format and number format
 * 
 * @author BSukumar
 */
public class FormatterUtil {

	/**
	 * This method is to convert ISO_INSTANT format to ISO_DATE_TIME format
	 * 
	 * @param isoInstant
	 *            date in ISO_INSTANT format
	 * @return String in ISO_DATE_TIME
	 */
	public static String instantToIsoDate(String isoInstant) {
		Instant instant = Instant.parse(isoInstant);
		// Convert instant to LocalDateTime, no timezone, add a zero offset
		// UTC+0
		LocalDateTime ldt = LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
		String formatDateTime = ldt.format(DateTimeFormatter.ISO_DATE_TIME);
		return formatDateTime;
	}

	/**
	 * This method is to convert ISO_LOCAL_DATE_TIME format to ISO_DATE_TIME
	 * format
	 * 
	 * @param isoLocalDateTime
	 *            date in ISO_LOCAL_DATE_TIME format
	 * @return String in ISO_DATE_TIME
	 */
	public static String localToIsoDateTime(String isoLocalDateTime) {
		return LocalDateTime.parse(isoLocalDateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
				.format(DateTimeFormatter.ISO_DATE_TIME);
	}

	/**
	 * This method is to round up the given number to the given decimal places
	 * 
	 * @param value
	 *            value to be rounded up
	 * @param places
	 *            Number of decimal places
	 * @return double rounded number in double
	 */
	public static double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		BigDecimal bd = new BigDecimal(Double.toString(value));
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

}
