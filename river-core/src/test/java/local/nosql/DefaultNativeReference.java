package local.nosql;

import org.riverframework.River;
import org.riverframework.wrapper.AbstractNativeReference;
import org.riverframework.wrapper.Base;

import java.lang.ref.ReferenceQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

class DefaultNativeReference extends AbstractNativeReference<local.mock.Base> {
	private static final Logger log = River.LOG_WRAPPER_LOTUS_DOMINO;

	public DefaultNativeReference(Base<local.mock.Base> referent,
			ReferenceQueue<Base<local.mock.Base>> q) {
		super(referent, q);
	}

	@Override
	public void close() {  
		if (__native != null) {
			
			try {
				__native.recycle();
			} catch (Exception e) {
				log.log(Level.WARNING, "Exception while recycling object " + id, e);

			} finally {
				__native = null;
			}

			log.finest("Recycled: id=" + id);  
		}
	}
}

