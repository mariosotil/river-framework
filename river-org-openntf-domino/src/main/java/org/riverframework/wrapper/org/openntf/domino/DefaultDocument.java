package org.riverframework.wrapper.org.openntf.domino;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
// import java.util.logging.Logger;


import org.openntf.domino.DateTime;
import org.openntf.domino.Item;
import org.riverframework.River;
import org.riverframework.RiverException;
import org.riverframework.core.DefaultField;
import org.riverframework.core.Field;
import org.riverframework.utils.Converter;
import org.riverframework.wrapper.Document;

/**
 * Loads an IBM Notes document
 * <p>
 * This is a javadoc test
 * 
 * @author mario.sotil@gmail.com
 * @version 0.0.x
 */
class DefaultDocument extends AbstractBaseOrgOpenntfDomino<org.openntf.domino.Document> implements org.riverframework.wrapper.Document<org.openntf.domino.Document> {
	// private static final Logger log = River.LOG_WRAPPER_ORG_OPENNTF_DOMINO;

	private final String FRAGMENTED_FIELD_ID = "{{RIVER_FRAGMENTED_FIELD}}";
	private final String FRAGMENT_FIELD_NAME_SEPARATOR = "$";
	private final int MAX_FIELD_SIZE = 32 * 1024 - 1;

	protected DefaultDocument(org.riverframework.wrapper.Session<org.openntf.domino.Session> _session, org.openntf.domino.Document __native) {
		super(_session, __native);
	}

	@Override
	public String calcObjectId(org.openntf.domino.Document __doc) {
		String objectId = "";
		if (__doc != null) {
			org.openntf.domino.Database __database = __doc.getParentDatabase();

			StringBuilder sb = new StringBuilder();
			sb.append(__database.getServer());
			sb.append(River.ID_SEPARATOR);
			sb.append(__database.getFilePath());
			sb.append(River.ID_SEPARATOR);
			sb.append(__doc.getUniversalID());

			objectId = sb.toString();
		}

		return objectId;
	}

	@Override
	public Document<org.openntf.domino.Document> setBinder(String table) {
		__native.replaceItemValue("Form", table);
		return this;
	}

	@Override
	public String getBinder() {
		return __native.getItemValueString("Form");
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Document setField(String field, Object value) {
		java.util.Vector temp = null;

		if (value instanceof String) {
			String str = (String) value;
			if (str.length() >= MAX_FIELD_SIZE) {
				// Fragment the text in parts with size less than MAX_FIELD_SIZE
				boolean finished = false;
				int size = str.length();
				int block = 0;

				while (!finished) {
					int start = block * (MAX_FIELD_SIZE);
					int end = start + (MAX_FIELD_SIZE);
					if (end > size) { 
						end = size;
						finished = true;
					}
					String substr = str.substring(start, end);
					block++;

					String fieldName = field + FRAGMENT_FIELD_NAME_SEPARATOR + block;
					Item __item = __native.replaceItemValue(fieldName, substr);
					__item.setSummary(false);
				}				

				// Saving the size as number of blocks 
				str = FRAGMENTED_FIELD_ID + "|" + block;		
			}

			temp = new Vector(1);
			temp.add(str);
		} else if (value instanceof java.util.Vector) {
			temp = (Vector) ((java.util.Vector) value).clone();
		} else if (value instanceof java.util.Collection) {
			temp = new Vector((java.util.Collection) value);
		} else if (value instanceof String[]) {
			temp = new Vector(Arrays.asList((Object[]) value));
		} else {
			temp = new Vector(1);
			temp.add(value);
		}

		if (temp.get(0) instanceof java.util.Date) {
			for (int i = 0; i < temp.size(); i++) {
				// Always save as org.openntf.domino.DateTime
				org.openntf.domino.Session __session;
				org.openntf.domino.DateTime _date;

				__session = _session.getNativeObject(); //	__doc.getParentDatabase().getParent();
				_date = __session.createDateTime((java.util.Date) temp.get(i));

				temp.set(i, _date);
			}
		}

		__native.replaceItemValue(field, temp);

		return this;
	}

	@Override
	public Document<org.openntf.domino.Document> recalc() {
		__native.computeWithForm(true, false);
		return this;
	}

	@Override
	public Field getField(String field) {
		Vector<?> temp = __native.getItemValue(field);
		Field value = temp == null ? new DefaultField() : new DefaultField(temp);

		if (!value.isEmpty()) {
			if (value.get(0) instanceof org.openntf.domino.DateTime) {
				for (int i = 0; i < value.size(); i++) {
					value.set(i, ((org.openntf.domino.DateTime) value.get(i)).toJavaDate());
				}
			}
		}

		return value;
	}

	@Override
	public String getFieldAsString(String field) {
		Vector<?> value = __native.getItemValue(field);
		String result = value.size() > 0 ? Converter.getAsString(value.get(0)) : "";

		if (result.startsWith(FRAGMENTED_FIELD_ID)) {
			String[] params = result.split("\\|");
			int blockNum = Integer.valueOf(params[1]);
			StringBuilder sb = new StringBuilder(MAX_FIELD_SIZE * blockNum);
			for(int i = 1; i <= blockNum; i++) {
				String fragment = getFieldAsString(field + FRAGMENT_FIELD_NAME_SEPARATOR + i );
				sb.append(fragment);
			}

			result = sb.toString();
		}
		return result;
	}

	@Override
	public int getFieldAsInteger(String field) {
		Vector<?> value = __native.getItemValue(field);
		int result = value.size() > 0 ? Converter.getAsInteger(value.get(0)) : 0;

		return result;
	}

	@Override
	public long getFieldAsLong(String field) {
		Vector<?> value = __native.getItemValue(field);
		long result = value.size() > 0 ? Converter.getAsLong(value.get(0)) : 0;

		return result;
	}

	@Override
	public double getFieldAsDouble(String field) {
		Vector<?> value = __native.getItemValue(field);
		double result = value.size() > 0 ? Converter.getAsDouble(value.get(0)) : 0;

		return result;
	}

	@Override
	public Date getFieldAsDate(String field) {
		Date result;

		Vector<?> value = null;
		value = __native.getItemValue(field);
		Object temp = value.size() > 0 ? value.get(0) : null;  
		if (temp != null && temp.getClass().getName().endsWith("DateTime")) {
			temp = ((DateTime) temp).toJavaDate();
		}

		result = Converter.getAsDate(temp);

		return result;
	}

	@Override
	public boolean isFieldEmpty(String field) {
		boolean result = true;
		if (__native.hasItem(field)) {
			org.openntf.domino.Item __item = __native.getFirstItem(field);
			if (__item != null) {
				if (__item.getType() == org.openntf.domino.Item.RICHTEXT) {
					if (!__native.getEmbeddedObjects().isEmpty()) {
						for (@SuppressWarnings("unchecked")
						Iterator<org.openntf.domino.EmbeddedObject> i = __native.getEmbeddedObjects()
						.iterator(); i.hasNext();) {
							org.openntf.domino.EmbeddedObject eo = i.next();
							if (eo.getType() != 0) {
								result = false;
								break;
							}
						}
					}
				}

				if (result && __item.getText() != "")
					result = false;

				try {
					// __item.recycle(); <== Very bad idea? 
				} catch (Exception e) {
					throw new RiverException(e);
				} finally {
					__item = null;
				}
			}
		}

		return result;
	}

	@Override
	public boolean hasField(String field) {
		boolean result;

		result = __native.hasItem(field);

		return result;
	}

	@Override
	public Map<String, Field> getFields() {
		Map<String, Field> result = null;

		Vector<org.openntf.domino.Item> items = null;
		items = __native.getItems();

		result = new HashMap<String, Field>(items.size());

		for (org.openntf.domino.Item __item : items) {
			String name = __item.getName();
			int type = __item.getType();

			Field values = null;

			if (type == Item.DATETIMES
					|| type == Item.NAMES
					|| type == Item.NUMBERS
					|| type == Item.READERS
					|| type == Item.RICHTEXT
					|| type == Item.TEXT) {
				Vector<Object> temp = __item.getValues();
				values = temp == null ? new DefaultField() : new DefaultField(temp);
			} else {
				values = new DefaultField();
			}

			// __item.recycle(); <== Very bad idea? 

			if (values.isEmpty()) {
				values.add("");
			}

			if (!values.isEmpty()) {
				if (values.get(0) instanceof org.openntf.domino.DateTime) {
					for (int i = 0; i < values.size(); i++) {
						values.set(i, ((org.openntf.domino.DateTime) values.get(i)).toJavaDate());
					}
				}
			}

			result.put(name, values);
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

		result = __native.isNewNote();

		return result;
	}

	@Override
	public Document<org.openntf.domino.Document> delete() {
		if (__native != null) {
			__native.removePermanently(true);
			__native = null;
		}

		return this;
	}

	@Override
	public Document<org.openntf.domino.Document> save() {
		__native.save(true, false);

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
