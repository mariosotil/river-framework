package org.riverframework.wrapper.lotus.domino;

import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import lotus.domino.NotesException;

import org.riverframework.River;
import org.riverframework.wrapper.Base;
import org.riverframework.wrapper.NativeReferenceMap;

public class DominoReferenceMap extends NativeReferenceMap<DominoReference> {

	private static final Logger log = River.LOG_WRAPPER_LOTUS_DOMINO;
	private static final long serialVersionUID = -883737839334160489L;

	synchronized void getObject(Base wrapper) {
		if (wrapper != null && wrapper.isOpen()) {
			String id = wrapper.getObjectId();
			DominoReference ref = get(id);
			if (ref == null) {
				if (log.isLoggable(Level.FINEST)) {
					lotus.domino.Base nativeObject = (lotus.domino.Base) wrapper.getNativeObject();
					log.finest("Registering object: id=" + id +  "wrapper=" + wrapper.getClass().getName() + " (" + wrapper.hashCode() 
							+ ") native=" + nativeObject.getClass().getName() + " (" + nativeObject.hashCode() + ")");
				}

				put(id, new DominoReference(wrapper));
			} else {
				Base old = wrapper;
				wrapper = ref.getWrapperObject();

				if (log.isLoggable(Level.FINEST)) {
					log.finest("Changing wrapper for the registered one: id=" + id + " wrapper=" + wrapper.getClass().getName() + " (" + wrapper.hashCode() + 
							")  Destroying old=" + old.getClass().getName() + " (" + old.hashCode() + ")");
				}
				
				old = null;
			}
		}
	}
	
	synchronized void recycleObject(String key) {
		DominoReference ref = get(key);
		
		if (ref != null && ref.getWrapperObject() == null) {
			lotus.domino.Base obj = ref.getNativeObject();
			try {
				obj.recycle();
			} catch (NotesException e) {
				log.warning("There was a problem recycling the object with id=" + key);
			}
			obj = null;

			remove(key);
		}
	}
	
	synchronized void close() {
		log.fine("Recycling all registered objects");

		Iterator<String> keys = keySet().iterator();
		while (keys.hasNext()) {
			String key = keys.next();
			recycleObject(key);
		}
	}
}