package local.wrapper;

import org.riverframework.RiverException;
import org.riverframework.wrapper.AbstractBase;
import org.riverframework.wrapper.Session;

import java.lang.reflect.Field;

abstract class AbstractBaseNoSQL<N> extends AbstractBase<N, local.mock.Session, local.mock.Base> {
	protected AbstractBaseNoSQL(Session<local.mock.Session> _session, N __native) {
		super(_session, __native);
	}
	
	abstract public boolean isRecycled();

	private static Field isDeleted = null;
	private static Field weakObject = null;
	private static Field cpp = null;
	private static Class<?> clazzRecycleThread = null;
	private static Field remoteDatabaseDeleted = null;
	private static Field remoteDocumentDeleted = null;
	private static Field mRecycler = null;
	private static Field mRunning = null;

	private static Field noteIDStr = null;

	static {
		try {			
			// For local connections
			// isDeleted = org.riverframework.mock.local.NotesBase.class.getDeclaredField("isdeleted");
			isDeleted.setAccessible(true);

			// weakObject = org.riverframework.mock.local.NotesBase.class.getDeclaredField("weakObject");
			weakObject.setAccessible(true);

			Class<?> clazz = Class.forName("local.mock.local.NotesWeakReference");
			cpp = clazz.getDeclaredField("cpp_object");
			cpp.setAccessible(true);

			// For remote connections
			// remoteDatabaseDeleted = org.riverframework.mock.cso.Database.class.getDeclaredField("deleted");
			remoteDatabaseDeleted.setAccessible(true);

			//remoteDocumentDeleted = org.riverframework.mock.cso.Document.class.getDeclaredField("deleted");
			remoteDocumentDeleted.setAccessible(true);

			//mRecycler = org.riverframework.mock.cso.Base.class.getDeclaredField("m_recycler");
			mRecycler.setAccessible(true);

			clazzRecycleThread = Class.forName("local.mock.cso.RecycleThread");
			mRunning = clazzRecycleThread.getDeclaredField("m_running");
			mRunning.setAccessible(true);

			//noteIDStr = org.riverframework.mock.cso.Document.class.getDeclaredField("noteIDStr");
			noteIDStr.setAccessible(true);

		} catch (Exception e) {
			throw new RiverException(e);
		}
	}

	static Object getFieldValue(Object __obj, String name) {
		Class<?> clazz = __obj.getClass();
		Field field = null;
		Object result = null;

		while(!clazz.getSimpleName().equals("Object") && field == null) {
			try {
				field = clazz.getDeclaredField(name);

				if (field != null) {
					field.setAccessible(true);				
					result = field.get(__obj);

					return result;
				}
			} catch (NoSuchFieldException e) {
				// Do nothing
			} catch (Exception e1) {
				throw new RiverException(e1);
			}


			clazz = clazz.getSuperclass();
		}

		return result;
	}

	static boolean isObjectRecycled(local.mock.Base __native) {
		boolean result = false;

		try {
		    /*
			if(__native instanceof org.riverframework.mock.local.NotesBase) {
				result = isDeleted.getBoolean((org.riverframework.mock.local.NotesBase) __native) || getCpp((org.riverframework.mock.local.NotesBase) __native) == 0;

			} else if(__native instanceof org.riverframework.mock.cso.Database) {
                result = remoteDatabaseDeleted.getBoolean((org.riverframework.mock.cso.Database) __native);
				
			} else if(__native instanceof org.riverframework.mock.cso.Document) {
				result = remoteDocumentDeleted.getBoolean((org.riverframework.mock.cso.Document) __native);
				
			} else if(__native instanceof org.riverframework.mock.cso.Base) {
				Object m_recycler = mRecycler.get((org.riverframework.mock.cso.Base) __native);
				result = !mRunning.getBoolean(clazzRecycleThread.cast(m_recycler));

			} 
*/
		} catch (Exception e) {
			throw new RiverException(e);
		}

		return result;
	}

	static long getCpp(local.mock.Base __native) {
		long result = 0;

		try {
			Object __wo = weakObject.get(__native);
			result = cpp.getLong(__wo);
		} catch (Exception e) {
			throw new RiverException(e);
		}

		return result;
	}

	static String getNoteIDStr(local.mock.Base __native) {
		String result;

		try {
			result = (String) noteIDStr.get(__native);
		} catch (Exception e) {
			throw new RiverException(e);
		}

		return result;
	}

}
