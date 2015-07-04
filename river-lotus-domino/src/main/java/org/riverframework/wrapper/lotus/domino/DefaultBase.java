package org.riverframework.wrapper.lotus.domino;

import java.lang.reflect.Field;

import lotus.domino.local.NotesBase;

import org.riverframework.RiverException;
import org.riverframework.wrapper.Base;

abstract class DefaultBase implements Base<lotus.domino.Base> {
	private static Field isDeleted = null;
	private static Field weakObject = null;
	private static Field cpp = null;
			
	static {
		try {
			isDeleted = NotesBase.class.getDeclaredField("isdeleted");
			isDeleted.setAccessible(true);

			weakObject = NotesBase.class.getDeclaredField("weakObject");
			weakObject.setAccessible(true);

			Class<?> clazz = Class.forName("lotus.domino.local.NotesWeakReference");
			cpp = clazz.getDeclaredField("cpp_object");
			cpp.setAccessible(true);
		} catch (Exception e) {
			throw new RiverException(e);
		}
	}
	
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
		boolean result = false;
		
		try {
			result = isDeleted.getBoolean((NotesBase) __native);
		} catch (Exception e) {
			throw new RiverException(e);
		}

		return result;
	}
	
	static long getCpp(lotus.domino.Base __native) {
		long result = 0;

		try {
			Object __wo = weakObject.get(__native);
			result = cpp.getLong(__wo);
		} catch (Exception e) {
			throw new RiverException(e);
		}

		return result;
	}
}
