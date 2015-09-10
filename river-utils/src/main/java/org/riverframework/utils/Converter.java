package org.riverframework.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	 * Converts the value parameter to a String. If the value is null, it returns an empty string. If the
	 * parameter is a Date object, it will be converted to the ISO 8601 format: yyyy-MM-ddTHH:mm:ss.SSSZ 
	 * @param value It could be any object.
	 * @return a String
	 */
	public static String getAsString(Object value) {
		String result = null;
		if (value == null) {
			result = "";
		} else if (value instanceof Double) {
			double n = (Double) value;
			result = n == (long) n ? String.format("%d",(long) n) : String.format("%s", n);
		} else if (value instanceof Date) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
			String date = df.format((Date) value);

			result = date.substring(0, date.length() - 2) + ":" + date.substring(date.length() - 2, date.length());
		} else {
			result = value.toString();
		}

		return result;
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
	 * Converts the value parameter to a Date. If the value is null or invalid, it returns null. If the value is a String, 
	 * it has to have one of the following syntax:
	 * <p>
	 * <ul>
	 * <li>yyyy-MM-dd</li>
	 * <li>yyyy-MM-dd'T'HH:mmZ</li>
	 * <li>yyyy-MM-dd'T'HH:mm:ssZ</li>
	 * <li>yyyy-MM-dd'T'HH:mm:ss.SSSZ</li>
	 * </ul> 
	 * <p>
	 * If the value is a Long number, it has to have the time in milliseconds.
	 * 
	 * @param value Accepts String, Long and Date
	 * @return an Integer
	 */
	@SuppressWarnings("serial")
	private static final Map<Matcher, SimpleDateFormat> mapDateFormat = new HashMap<Matcher, SimpleDateFormat>() {{
		put(Pattern.compile("^\\d{4}-\\d{1,2}-\\d{1,2}$").matcher(""), new SimpleDateFormat("yyyy-MM-dd"));
		put(Pattern.compile("^\\d{4}-\\d{1,2}-\\d{1,2}T\\d{1,2}:\\d{2}\\+\\d{2}:\\d{2}$").matcher(""), new SimpleDateFormat("yyyy-MM-dd'T'HH:mmZ"));
		put(Pattern.compile("^\\d{4}-\\d{1,2}-\\d{1,2}T\\d{1,2}:\\d{2}:\\d{2}\\+\\d{2}:\\d{2}$").matcher(""), new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ"));
		put(Pattern.compile("^\\d{4}-\\d{1,2}-\\d{1,2}T\\d{1,2}:\\d{2}:\\d{2}\\.\\d{2}[+-]\\d{2}:\\d{2}$").matcher(""), new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ"));
	}};

	public static Date getAsDate(Object value) {
		Date result = null;

		try {
			if (value instanceof String) {
				String date = ((String) value).toUpperCase();
				for (Matcher matcher : mapDateFormat.keySet()) {
					if (matcher.reset(date).matches()) {
						SimpleDateFormat sdf = mapDateFormat.get(matcher);
						if (sdf != null) {
							if ( date.endsWith( "Z" ) ) {
								date = date.substring( 0, date.length() - 1) + "GMT-00:00";

							} else {
								if(date.indexOf('T') > -1) {
									int index = date.indexOf('+');
									index = index > -1 ? index : date.lastIndexOf('-');

									if (index > -1) {
										String left = date.substring( 0, index );
										String right = date.substring( index, date.length() );

										date = left + "GMT" + right;
									}
								}
							}

							sdf.setLenient(false); 
							result = sdf.parse(date);
							break;
						}
					}
				}
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
