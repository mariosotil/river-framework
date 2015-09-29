package org.riverframework.wrapper.com.couchbase.lite;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.riverframework.River;
import org.riverframework.RiverException;
import org.riverframework.core.DefaultField;
import org.riverframework.core.Field;
import org.riverframework.utils.Converter;
import org.riverframework.wrapper.Document;

import com.couchbase.lite.CouchbaseLiteException;
// import java.util.logging.Logger;

/**
 * Loads an IBM Notes document
 * <p>
 * This is a javadoc test
 * 
 * @author mario.sotil@gmail.com
 * @version 0.0.x
 */
class DefaultDocument extends AbstractBaseCouchbaseLite<com.couchbase.lite.Document> implements org.riverframework.wrapper.Document<com.couchbase.lite.Document> {
	// private static final Logger log = River.LOG_WRAPPER_ORG_OPENNTF_DOMINO;

	protected DefaultDocument(org.riverframework.wrapper.Session<com.couchbase.lite.Manager> _session, com.couchbase.lite.Document __native) {
		super(_session, __native);
	}

	@Override
	public String calcObjectId(com.couchbase.lite.Document __doc) {
		String objectId = "";
		if (__doc != null) {
			com.couchbase.lite.Database __database = __doc.getDatabase();

			StringBuilder sb = new StringBuilder();
			sb.append("LOCAL");
			sb.append(River.ID_SEPARATOR);
			sb.append(__database.getPath());
			sb.append(River.ID_SEPARATOR);
			sb.append(__doc.getId());

			objectId = sb.toString();
		}

		return objectId;
	}

	@Override
	public Document<com.couchbase.lite.Document> setBinder(String binder) {
		setField("RIVER_BINDER", binder);
		return this;
	}

	@Override
	public String getBinder() {
		return (String) __native.getProperty("RIVER_BINDER");
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Document setField(String field, Object value) {
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.putAll(__native.getProperties());
		properties.put(field, value);
		try {
			__native.putProperties(properties);
		} catch (CouchbaseLiteException e) {
			throw new RiverException(e);
		}

		return this;
	}

	@Override
	public Document<com.couchbase.lite.Document> recalc() {
		// Do nothing?
		return this;
	}

	@Override
	public Field getField(String field) {
		Object property = __native.getProperty(field);

		Field value = new DefaultField();
		value.add(property);

		return value;
	}

	@Override
	public String getFieldAsString(String field) {
		Object property = __native.getProperty(field);
		String result = Converter.getAsString(property);

		return result;
	}

	@Override
	public int getFieldAsInteger(String field) {
		Object property = __native.getProperty(field);
		int result = Converter.getAsInteger(property);

		return result;
	}

	@Override
	public long getFieldAsLong(String field) {
		Object property = __native.getProperty(field);
		long result = Converter.getAsLong(property);

		return result;
	}

	@Override
	public double getFieldAsDouble(String field) {
		Object property = __native.getProperty(field);
		double result = Converter.getAsDouble(property);

		return result;
	}

	@Override
	public Date getFieldAsDate(String field) {
		Object property = __native.getProperty(field);
		Date result = Converter.getAsDate(property);

		return result;
	}

	@Override
	public boolean isFieldEmpty(String field) {
		Object property = __native.getProperty(field);
		boolean result = property == null;
		return result;
	}

	@Override
	public boolean hasField(String field) {
		boolean result;

		Object property = __native.getProperty(field);
		result = property != null;

		return result;
	}

	@Override
	public Map<String, Field> getFields() {
		Map<String, Field> result = new HashMap<String, Field>();
		Map<String, Object> properties = new HashMap<String, Object>();
		
		properties.putAll(__native.getProperties());

		for(Map.Entry<String, Object> property : properties.entrySet()) {
			Field field = new DefaultField();
			field.add(property.getValue());
			result.put(property.getKey(), field);
		}

		return result;
	}

	@Override
	public boolean isOpen() {		
		return (__native != null);
	}

	@Override
	public boolean isNew() {
		boolean result;

		result = false; // How couchbase implements this feature?

		return result;
	}

	@Override
	public Document<com.couchbase.lite.Document> delete() {
		if (__native != null) {
			try {
				__native.delete();
			} catch (CouchbaseLiteException e) {
				throw new RiverException(e);
			}
			__native = null;
		}

		return this;
	}

	@Override
	public Document<com.couchbase.lite.Document> save() {
		// Nothing to do?
		return this;
	}

	@Override
	public void close() {
		__native = null;
	}	

	@Override
	public String toString() {
		return getClass().getName() + "(" + objectId + ")";
	}

}
