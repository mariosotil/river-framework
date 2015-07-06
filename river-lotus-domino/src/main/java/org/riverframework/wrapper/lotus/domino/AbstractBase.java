package org.riverframework.wrapper.lotus.domino;

import java.lang.reflect.Field;

import org.riverframework.RiverException;
import org.riverframework.wrapper.Base;

abstract class AbstractBase<N> implements Base<N> {
	private static Field isDeleted = null;
	private static Field weakObject = null;
	private static Field cpp = null;
	private static Field noteIDStr = null;
			
	static {
		try {			
			// For local connections
			isDeleted = lotus.domino.local.NotesBase.class.getDeclaredField("isdeleted");
			isDeleted.setAccessible(true);

			weakObject = lotus.domino.local.NotesBase.class.getDeclaredField("weakObject");
			weakObject.setAccessible(true);

			Class<?> clazz = Class.forName("lotus.domino.local.NotesWeakReference");
			cpp = clazz.getDeclaredField("cpp_object");
			cpp.setAccessible(true);

			// For remote connections
			noteIDStr = lotus.domino.cso.Document.class.getDeclaredField("noteIDStr");
			noteIDStr.setAccessible(true);

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
	
	static boolean isObjectRecycled(lotus.domino.Base __native) {
		boolean result = false;
		
		try {
			result = isDeleted.getBoolean((lotus.domino.local.NotesBase) __native);
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

	static String getNoteIDStr(lotus.domino.Base __native) {
		String result;

		try {
			result = (String) noteIDStr.get(__native);
		} catch (Exception e) {
			throw new RiverException(e);
		}

		return result;
	}
	
	abstract boolean isRecycled();

}
