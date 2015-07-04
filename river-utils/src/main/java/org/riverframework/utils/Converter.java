package org.riverframework.utils;

import java.util.Calendar;
import java.util.Date;

import org.joda.time.LocalDateTime;

/**
 * Provides converters from any type to String, Integer, Double and Date. If the converter can not make the conversion
 * it returns "", 0, 0, and null, respectively. This class is useful in org.riverframework.wrapper.* because makes simple
 * to recover data in Java format.
 *
 * @author mario.sotil@gmail.com
 *
 */

public class Converter {
	private static final Calendar calendar = Calendar.getInstance();
	
	/**
	 * Converts the value parameter to a String. If the value is null, it returns an empty string.
	 * @param value It could be any object.
	 * @return a String
	 */
	public static String getAsString(Object value) {
		return value == null ? "" : value.toString();
	}

	/**
	 * Converts the value parameter to an Integer. If the value is null or invalid, it returns zero.
	 * @param value Accepts String or any Number
	 * @return an Integer
	 */
	public static Integer getAsInteger(Object value) {
		Integer result = null;

		try {
			if (value instanceof String) {
				result = Integer.valueOf((String) value);
			} else if (value instanceof Number) {
				result = ((Number) value).intValue();
			} else {
				result = null;
			}
		} catch (Exception e) {
			result = null;
		}

		return result == null ? 0 : result;
	}

	/**
	 * Converts the value parameter to a Long. If the value is null or invalid, it returns zero.
	 * @param value Accepts String or any Number
	 * @return a Long
	 */
	public static Long getAsLong(Object value) {
		Long result = null;

		try {
			if (value instanceof String) {
				result = Long.valueOf((String) value);
			} else if (value instanceof Number) {
				result = ((Number) value).longValue();
			} else {
				result = null;
			}
		} catch (Exception e) {
			result = null;
		}

		return result == null ? 0 : result;
	}

	/**
	 * Converts the value parameter to a Double. If the value is null or invalid, it returns zero.
	 * @param value Accepts String or any Number
	 * @return a Double
	 */
	public static Double getAsDouble(Object value) {
		Double result = null;

		try {
			if (value instanceof String) {
				result = Double.valueOf((String) value);
			} else if (value instanceof Number) {
				result = ((Number) value).doubleValue();
			} else {
				result = null;
			}
		} catch (Exception e) {
			result = null;
		}

		return result == null ? 0 : result;
	}

	/**
	 * Converts the value parameter to a Date. If the value is null or invalid, it returns zero. If the value is a String, 
	 * it has to have one of the following syntax (from http://joda-time.sourceforge.net/apidocs/org/joda/time/format/ISODateTimeFormat.html#localDateOptionalTimeParser%28%29):
	 * <p>
	 * <ul>
	 * <li>datetime          = date-element ['T' time-element]</li>
	 * <li>date-element      = std-date-element | ord-date-element | week-date-element</li>
	 * <li>std-date-element  = yyyy ['-' MM ['-' dd]]</li>
	 * <li>ord-date-element  = yyyy ['-' DDD]</li>
	 * <li>week-date-element = xxxx '-W' ww ['-' e]</li>
	 * <li>time-element      = HH [minute-element] | [fraction]</li>
	 * <li>minute-element    = ':' mm [second-element] | [fraction]</li>
	 * <li>second-element    = ':' ss [fraction]</li>
	 * <li>fraction          = ('.' | ',') digit+</li>
	 * </ul> 
	 * <p>
	 * If the value is a Long number, it has to have the time in milliseconds.
	 * 
	 * @param value Accepts String, Long and Date
	 * @return an Integer
	 */
	public static Date getAsDate(Object value) {
		Date result = null;

		try {
			if (value instanceof String) {
				result = LocalDateTime.parse((String) value).toDate();
			} else if (value instanceof Long) {
				calendar.setTimeInMillis((Long) value);
				result = calendar.getTime();
			} else if (value instanceof Date) {
				result = (Date) value;
			} else {
				result = null;
			}
		} catch (Exception e) {
			result = null;
		}

		return result;
	}
}
