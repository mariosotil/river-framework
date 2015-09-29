package org.riverframework.wrapper.lotus.domino;

import java.lang.reflect.Field;

import org.riverframework.RiverException;
import org.riverframework.wrapper.AbstractBase;
import org.riverframework.wrapper.Session;

abstract class AbstractBaseLotusDomino<N> extends AbstractBase<N, lotus.domino.Session, lotus.domino.Base> {
	protected AbstractBaseLotusDomino(Session<lotus.domino.Session> _session, N __native) {
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
			isDeleted = lotus.domino.local.NotesBase.class.getDeclaredField("isdeleted");
			isDeleted.setAccessible(true);

			weakObject = lotus.domino.local.NotesBase.class.getDeclaredField("weakObject");
			weakObject.setAccessible(true);

			Class<?> clazz = Class.forName("lotus.domino.local.NotesWeakReference");
			cpp = clazz.getDeclaredField("cpp_object");
			cpp.setAccessible(true);

			// For remote connections
			remoteDatabaseDeleted = lotus.domino.cso.Database.class.getDeclaredField("deleted");
			remoteDatabaseDeleted.setAccessible(true);

			remoteDocumentDeleted = lotus.domino.cso.Document.class.getDeclaredField("deleted");
			remoteDocumentDeleted.setAccessible(true);

			mRecycler = lotus.domino.cso.Base.class.getDeclaredField("m_recycler");
			mRecycler.setAccessible(true);

			clazzRecycleThread = Class.forName("lotus.domino.cso.RecycleThread");
			mRunning = clazzRecycleThread.getDeclaredField("m_running");
			mRunning.setAccessible(true);

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

	static boolean isObjectRecycled(lotus.domino.Base __native) {
		boolean result = false;

		try {
			if(__native instanceof lotus.domino.local.NotesBase) {
				result = isDeleted.getBoolean((lotus.domino.local.NotesBase) __native) || getCpp((lotus.domino.local.NotesBase) __native) == 0;

			} else if(__native instanceof lotus.domino.cso.Database) {
				result = remoteDatabaseDeleted.getBoolean((lotus.domino.cso.Database) __native);
				
			} else if(__native instanceof lotus.domino.cso.Document) {
				result = remoteDocumentDeleted.getBoolean((lotus.domino.cso.Document) __native);
				
			} else if(__native instanceof lotus.domino.cso.Base) {
				Object m_recycler = mRecycler.get((lotus.domino.cso.Base) __native);
				result = !mRunning.getBoolean(clazzRecycleThread.cast(m_recycler));

			} 

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

}
