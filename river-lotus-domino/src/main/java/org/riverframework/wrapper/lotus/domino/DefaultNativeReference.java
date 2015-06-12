package org.riverframework.wrapper.lotus.domino;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.riverframework.River;
import org.riverframework.RiverException;
import org.riverframework.wrapper.AbstractNativeReference;
import org.riverframework.wrapper.Base;

class DefaultNativeReference extends AbstractNativeReference<lotus.domino.Base> {
	private static final Logger log = River.LOG_WRAPPER_LOTUS_DOMINO;

	public DefaultNativeReference(Base<lotus.domino.Base> referent,
			ReferenceQueue<Base<lotus.domino.Base>> q) { 
		super(referent, q);
	}

	//	private String getFields(Object obj) {
	//		Class<?> objClass = obj.getClass();
	//		StringBuilder sb = new StringBuilder();
	//
	//		while(!objClass.getSimpleName().equals("Object")) {
	//			try {
	//				Field[] fields = objClass.getDeclaredFields();
	//				for(Field field : fields) {
	//					//if(! field.isEnumConstant()) {
	//					String name = field.getName();
	//					field.setAccessible(true);
	//					Object value = field.get(obj);
	//
	//					sb.append(name + ": " + (value == null ? "[null]" : value.toString()) + "\n");
	//					//}
	//				}
	//			} catch (Exception e) {
	//				throw new RiverException(e);
	//			}
	//			objClass = objClass.getSuperclass();
	//		}
	//		return sb.toString();
	//	}

	private Object getFieldValue(Object __obj, String name) {
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

	@Override
	public void close() {  
		if (__native != null) {
			Class<?> clazz = __native.getClass();
			String nativeClass = clazz.getName();
			String hc = String.valueOf(__native.hashCode());

			Object wr = ((Reference<?>) getFieldValue(__native, "weakObject"));
			long cpp = (Long) getFieldValue(wr, "cpp_object");

			if (cpp == 0) 
				log.finest("It's a trap!: id=" + id + " native=" + nativeClass + " (" + hc + ")");
			
			else {
				try {
					Method method = clazz.getMethod("recycle");
					method.invoke(__native);
					
				} catch (Exception e) {
					log.log(Level.WARNING, "Exception while recycling object " + id, e);
					
				} finally {
					__native = null;
				}
				
				log.finest("Recycled: id=" + id + " native=" + nativeClass + " cpp=" + cpp + " (" + hc + ")");
			}
		}
	}
}

