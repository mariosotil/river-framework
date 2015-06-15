package org.riverframework.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
	
	private static final Map<String, String> DATE_FORMAT_REGEXPS = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;
		{
			put("^\\d{8}$", "yyyyMMdd");
			put("^\\d{1,2}-\\d{1,2}-\\d{4}$", "dd-MM-yyyy");
			put("^\\d{4}-\\d{1,2}-\\d{1,2}$", "yyyy-MM-dd");
			put("^\\d{1,2}/\\d{1,2}/\\d{4}$", "MM/dd/yyyy");
			put("^\\d{4}/\\d{1,2}/\\d{1,2}$", "yyyy/MM/dd");
			put("^\\d{1,2}\\s[a-z]{3}\\s\\d{4}$", "dd MMM yyyy");
			put("^\\d{1,2}\\s[a-z]{4,}\\s\\d{4}$", "dd MMMM yyyy");
			put("^\\d{12}$", "yyyyMMddHHmm");
			put("^\\d{8}\\s\\d{4}$", "yyyyMMdd HHmm");
			put("^\\d{1,2}-\\d{1,2}-\\d{4}\\s\\d{1,2}:\\d{2}$", "dd-MM-yyyy HH:mm");
			put("^\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{1,2}:\\d{2}$", "yyyy-MM-dd HH:mm");
			put("^\\d{1,2}/\\d{1,2}/\\d{4}\\s\\d{1,2}:\\d{2}$", "MM/dd/yyyy HH:mm");
			put("^\\d{4}/\\d{1,2}/\\d{1,2}\\s\\d{1,2}:\\d{2}$", "yyyy/MM/dd HH:mm");
			put("^\\d{1,2}\\s[a-z]{3}\\s\\d{4}\\s\\d{1,2}:\\d{2}$", "dd MMM yyyy HH:mm");
			put("^\\d{1,2}\\s[a-z]{4,}\\s\\d{4}\\s\\d{1,2}:\\d{2}$", "dd MMMM yyyy HH:mm");
			put("^\\d{14}$", "yyyyMMddHHmmss");
			put("^\\d{8}\\s\\d{6}$", "yyyyMMdd HHmmss");
			put("^\\d{1,2}-\\d{1,2}-\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$", "dd-MM-yyyy HH:mm:ss");
			put("^\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{1,2}:\\d{2}:\\d{2}$", "yyyy-MM-dd HH:mm:ss");
			put("^\\d{1,2}/\\d{1,2}/\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$", "MM/dd/yyyy HH:mm:ss");
			put("^\\d{4}/\\d{1,2}/\\d{1,2}\\s\\d{1,2}:\\d{2}:\\d{2}$", "yyyy/MM/dd HH:mm:ss");
			put("^\\d{1,2}\\s[a-z]{3}\\s\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$", "dd MMM yyyy HH:mm:ss");
			put("^\\d{1,2}\\s[a-z]{4,}\\s\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$", "dd MMMM yyyy HH:mm:ss");
		}
	};

	/**
	 * Determine SimpleDateFormat pattern matching with the given date string. Returns null if
	 * format is unknown. You can simply extend DateUtil with more formats if needed.
	 * 
	 * @param dateString
	 *            The date string to determine the SimpleDateFormat pattern for.
	 * @return The matching SimpleDateFormat pattern, or null if format is unknown.
	 * @see SimpleDateFormat
	 * @author http://stackoverflow.com/users/157882/balusc
	 *         (http://stackoverflow.com/questions/3389348/parse-any-date-in-java)
	 */
	public static String determineDateFormat(String dateString) {
		for (String regexp : DATE_FORMAT_REGEXPS.keySet()) {
			if (dateString.toLowerCase().matches(regexp)) {
				return DATE_FORMAT_REGEXPS.get(regexp);
			}
		}
		return null; // Unknown format.
	}

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
	 * it has to have one of the following formats:
	 * <p>
	 * <ul>
	 * <li>yyyyMMdd</li>
	 * <li>dd-MM-yyyy</li>
	 * <li>yyyy-MM-dd</li>
	 * <li>MM/dd/yyyy</li>
	 * <li>yyyy/MM/dd</li>
	 * <li>dd MMM yyyy</li>
	 * <li>dd MMMM yyyy</li>
	 * <li>yyyyMMddHHmm</li>
	 * <li>yyyyMMdd HHmm</li>
	 * <li>dd-MM-yyyy HH:mm</li>
	 * <li>yyyy-MM-dd HH:mm</li>
	 * <li>MM/dd/yyyy HH:mm</li>
	 * <li>yyyy/MM/dd HH:mm</li>
	 * <li>dd MMM yyyy HH:mm</li>
	 * <li>dd MMMM yyyy HH:mm</li>
	 * <li>yyyyMMddHHmmss</li>
	 * <li>yyyyMMdd HHmmss</li>
	 * <li>dd-MM-yyyy HH:mm:ss</li>
	 * <li>yyyy-MM-dd HH:mm:ss</li>
	 * <li>MM/dd/yyyy HH:mm:ss</li>
	 * <li>yyyy/MM/dd HH:mm:ss</li>
	 * <li>dd MMM yyyy HH:mm:ss</li>
	 * <li>dd MMMM yyyy HH:mm:ss</li>
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
				String format = determineDateFormat((String) value);
				DateFormat df = new SimpleDateFormat(format);
				result = df.parse((String) value);
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
