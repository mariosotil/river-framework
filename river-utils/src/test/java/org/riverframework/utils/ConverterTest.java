package org.riverframework.utils;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

public class ConverterTest {

  @Rule
  public ErrorCollector collector = new ErrorCollector();

  @Test
  public void testToString() {
    Object obj = null;
    String result = null;

    obj = "Hi!";
    result = Converter.getAsString(obj);
    assertTrue("Failed from String to String", result.equals("Hi!"));

    obj = 3;
    result = Converter.getAsString(obj);
    assertTrue("Failed from Integer to String", result.equals("3"));

    obj = 3.1416;
    result = Converter.getAsString(obj);
    assertTrue("Failed from Double to String", result.equals("3.1416"));

    Calendar cal = Calendar.getInstance();
    cal.set(Calendar.YEAR, 2015);
    cal.set(Calendar.MONTH, Calendar.APRIL);
    cal.set(Calendar.DAY_OF_MONTH, 10);
    cal.set(Calendar.HOUR_OF_DAY, 1);
    cal.set(Calendar.MINUTE, 2);
    cal.set(Calendar.SECOND, 3);
    cal.set(Calendar.MILLISECOND, 456);
    obj = cal.getTime();
    result = Converter.getAsString(obj);
    result = result.substring(0, result.length() - 6);
    assertTrue("Failed from Date to String", result.equals("2015-04-10T01:02:03.456"));

    obj = new ArrayList<Object>();
    result = Converter.getAsString(obj);
    assertTrue("Failed from ArrayList to String", result.equals("[]"));
  }

  @Test
  public void testToInteger() {
    Object obj = null;
    Integer result = null;

    obj = "Hi!";
    result = Converter.getAsInteger(obj);
    assertTrue("Failed from String to Integer", result == 0);

    obj = 3;
    result = Converter.getAsInteger(obj);
    assertTrue("Failed from Integer to Integer", result == 3);

    obj = 3.1416;
    result = Converter.getAsInteger(obj);
    assertTrue("Failed from Double to Integer", result == 3);

    Calendar cal = Calendar.getInstance();
    cal.set(Calendar.YEAR, 2015);
    cal.set(Calendar.MONTH, Calendar.APRIL);
    cal.set(Calendar.DAY_OF_MONTH, 10);
    cal.set(Calendar.HOUR, 1);
    cal.set(Calendar.MINUTE, 2);
    cal.set(Calendar.SECOND, 3);
    obj = cal.getTime();
    result = Converter.getAsInteger(obj);
    assertTrue("Failed from Date to Integer", result == 0);

    obj = new ArrayList<Object>();
    result = Converter.getAsInteger(obj);
    assertTrue("Failed from ArrayList to String", result == 0);
  }

  @Test
  public void testToLong() {
    Object obj = null;
    Long result = null;

    obj = "Hi!";
    result = Converter.getAsLong(obj);
    assertTrue("Failed from String to Long", result == 0);

    obj = 3;
    result = Converter.getAsLong(obj);
    assertTrue("Failed from Integer to Long", result == 3);

    obj = 3.1416;
    result = Converter.getAsLong(obj);
    assertTrue("Failed from Double to Long", result == 3);

    Calendar cal = Calendar.getInstance();
    cal.set(Calendar.YEAR, 2015);
    cal.set(Calendar.MONTH, Calendar.APRIL);
    cal.set(Calendar.DAY_OF_MONTH, 10);
    cal.set(Calendar.HOUR, 1);
    cal.set(Calendar.MINUTE, 2);
    cal.set(Calendar.SECOND, 3);
    obj = cal.getTime();
    result = Converter.getAsLong(obj);
    assertTrue("Failed from Date to Long", result == 0);

    obj = new ArrayList<Object>();
    result = Converter.getAsLong(obj);
    assertTrue("Failed from ArrayList to String", result == 0);
  }

  @Test
  public void testToDouble() {
    Object obj = null;
    Double result = null;

    obj = "Hi!";
    result = Converter.getAsDouble(obj);
    assertTrue("Failed from String to Double", result == 0);

    obj = 3;
    result = Converter.getAsDouble(obj);
    assertTrue("Failed from Integer to Double", result == 3);

    obj = 3.1416;
    result = Converter.getAsDouble(obj);
    assertTrue("Failed from Double to Double", result == 3.1416);

    Calendar cal = Calendar.getInstance();
    cal.set(Calendar.YEAR, 2015);
    cal.set(Calendar.MONTH, Calendar.APRIL);
    cal.set(Calendar.DAY_OF_MONTH, 10);
    cal.set(Calendar.HOUR, 1);
    cal.set(Calendar.MINUTE, 2);
    cal.set(Calendar.SECOND, 3);
    obj = cal.getTime();
    result = Converter.getAsDouble(obj);
    assertTrue("Failed from Date to Double", result == 0);

    obj = new ArrayList<Object>();
    result = Converter.getAsDouble(obj);
    assertTrue("Failed from ArrayList to String", result == 0);
  }

  @Test
  public void testToDate() {
    Object obj = null;
    Date result = null;

    obj = 1425962712015L;
    result = Converter.getAsDate(obj);
    assertTrue("Failed from a right Long to Date",
        result != null && result.getTime() == 1425962712015L);

    obj = "2015-03-10";
    result = Converter.getAsDate(obj);
    assertTrue("Failed from a right String to Date",
        result != null && result.getTime() == 1425970800000L);

    obj = "2015-03-10T10:45+06:00";
    result = Converter.getAsDate(obj);
    assertTrue("Failed from a right String to Date",
        result != null && result.getTime() == 1425962700000L);

    obj = "2015-03-10T10:45:12+06:00";
    result = Converter.getAsDate(obj);
    assertTrue("Failed from a right String to Date",
        result != null && result.getTime() == 1425962712000L);

    obj = "2015-03-10T10:45:12.15+06:00";
    result = Converter.getAsDate(obj);
    assertTrue("Failed from a right String to Date",
        result != null && result.getTime() == 1425962712015L);

    obj = "Hi!";
    result = Converter.getAsDate(obj);
    assertTrue("Failed from a wrong String to Date", result == null);

    obj = 3;
    result = Converter.getAsDate(obj);
    assertTrue("Failed from Integer to Date", result == null);

    obj = 3.1416;
    result = Converter.getAsDate(obj);
    assertTrue("Failed from Double to Date", result == null);

    Calendar cal = Calendar.getInstance();
    cal.set(Calendar.YEAR, 2015);
    cal.set(Calendar.MONTH, Calendar.MARCH);
    cal.set(Calendar.DAY_OF_MONTH, 10);
    cal.set(Calendar.HOUR, 1);
    cal.set(Calendar.MINUTE, 2);
    cal.set(Calendar.SECOND, 3);
    cal.set(Calendar.MILLISECOND, 456);
    cal.setTimeZone(TimeZone.getTimeZone("GMT-4"));
    obj = cal.getTime();
    result = Converter.getAsDate(obj);
    assertTrue("Failed from Date to Date", result.getTime() == 1425963723456L);

    obj = new ArrayList<Object>();
    result = Converter.getAsDate(obj);
    assertTrue("Failed from ArrayList to Date", result == null);
  }
}
