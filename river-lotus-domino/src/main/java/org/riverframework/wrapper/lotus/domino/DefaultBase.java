package org.riverframework.wrapper.lotus.domino;

import java.lang.reflect.Field;

import org.riverframework.RiverException;
import org.riverframework.wrapper.Base;

abstract class DefaultBase implements Base<lotus.domino.Base> {

	static Object getFieldValue(Object __obj, String name) {
		Class<?> clazz = __obj.getClass();
		Field field = null;
		Object result = null;

		try {
			while(!clazz.getSimpleName().equals("Object") && field == null) {
				try {
					field = clazz.getDeclaredField(name);
				} catch (NoSuchFieldException e) {
					// Do nothing
				}
				clazz = clazz.getSuperclass();
			}

			
			if (field != null) {
				field.setAccessible(true);				
				result = field.get(__obj);
			}
		} catch (Exception e) {
			throw new RiverException(e);
		}
		return result;
	}

	static boolean isRecycled(lotus.domino.Base __native) {
		boolean result = (Boolean) getFieldValue(__native, "isdeleted");
		return result;
	}
}
