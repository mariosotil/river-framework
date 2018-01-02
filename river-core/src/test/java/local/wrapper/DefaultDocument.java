package local.wrapper;

import local.mock.Item;
import local.mock.DatabaseException;
import org.riverframework.River;
import org.riverframework.RiverException;
import org.riverframework.core.DefaultField;
import org.riverframework.core.Field;
import org.riverframework.utils.Converter;
import org.riverframework.wrapper.Document;

import java.util.*;

// import java.util.logging.Logger;

/**
 * Loads an IBM Notes document
 * 
 * @author mario.sotil@gmail.com
 * @version 0.0.x
 */
class DefaultDocument extends AbstractBaseNoSQL<local.mock.Document> implements org.riverframework.wrapper.Document<local.mock.Document> {
	private final String FRAGMENTED_FIELD_ID = "{{RIVER_FRAGMENTED_FIELD}}";
	private final String FRAGMENT_FIELD_NAME_SEPARATOR = "$";
	private final int MAX_FIELD_SIZE = 32 * 1024 - 1;

	protected DefaultDocument(org.riverframework.wrapper.Session<local.mock.Session> _session, local.mock.Document __native) {
		super(_session, __native);
	}

	@Override
	public boolean isRecycled() {
		return AbstractBaseNoSQL.isObjectRecycled(__native);
	}

	String getDocumentId() {
		if (_factory.getIsRemoteSession()) {
			String id;
			try {
				id = __native.getUniversalID();
			} catch (DatabaseException e) {
				throw new RiverException(e);
			}
			return id; 
		} else {
			return String.valueOf(AbstractBaseNoSQL.getCpp(__native));
		}
	}

	public String calcObjectId(local.mock.Document __doc) {
		String objectId = "";
		if (__doc != null) {
			String documentId = getDocumentId();

			if(isRecycled()) {
				throw new RiverException("The object " + documentId + " was recycled!");
			} else {
				try {
					local.mock.Database __database = __doc.getParentDatabase();

					StringBuilder sb = new StringBuilder(512);
					sb.append(__database.getServer());
					sb.append(River.ID_SEPARATOR);
					sb.append(__database.getFilePath());
					sb.append(River.ID_SEPARATOR);
					sb.append(documentId);

					objectId = sb.toString();

				} catch (DatabaseException e) {
					throw new RiverException(e);
				}
			}
		}

		return objectId;
	}

	@Override
	public Document<local.mock.Document> setBinder(String table) {
		setField("Form", table);
		return this;
	}

	@Override
	public String getBinder() {
		return getFieldAsString("Form");
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Document<local.mock.Document> setField(String field, Object value) {
	    /*
		Vector temp = null;

		if (value instanceof String) {
			String str = (String) value;
			if (str.length() >= MAX_FIELD_SIZE) {
				// Fragment the text in parts with size less than MAX_FIELD_SIZE
				boolean finished = false;
				int size = str.length();
				int block = 0;
				boolean remote = _factory.getIsRemoteSession();

				while (!finished) {
					int start = block * (MAX_FIELD_SIZE);

					// For some reason, the replaceItemValue lost two bytes
					// when it is a remote session
					if (remote && start > 0)
						start -= block* 2;

					int end = start + (MAX_FIELD_SIZE);
					if (end > size) {
						end = size;
						finished = true;
					}
					String substr = str.substring(start, end);
					block++;

					try {
						String fieldName = field + FRAGMENT_FIELD_NAME_SEPARATOR + block;
						Item __item = __native.replaceItemValue(fieldName, substr);
						__item.setSummary(false);
						if(!_factory.getIsRemoteSession())  __item.recycle();
					} catch (DatabaseException e) {
						throw new RiverException(e);
					}
				}

				// Saving the size as number of blocks
				str = FRAGMENTED_FIELD_ID + "|" + block;
			}

			temp = new Vector(1);
			temp.add(str);

		} else if (value instanceof Vector) {
			temp = (Vector) ((Vector) value).clone();

		} else if (value instanceof java.util.Collection) {
			temp = new Vector((java.util.Collection) value);

		} else if (value instanceof String[]) {
			temp = new Vector(Arrays.asList((Object[]) value));

		} else {
			temp = new Vector(1);
			temp.add(value);
		}

		if (temp.get(0) instanceof Date) {
			for (int i = 0; i < temp.size(); i++) {
				// Always save as org.riverframework.mock.DateTime
				local.mock.Session __session;
				local.mock.DateTime __date;
				try {
					__session = _session.getNativeObject(); //	__doc.getParentDatabase().getParent();
					__date = __session.createDateTime((Date) temp.get(i));
				} catch (DatabaseException e) {
					throw new RiverException(e);
				}
				temp.set(i, __date);
			}
		}
*/
		try {
			__native.replaceItemValue(field, value);
		} catch (DatabaseException e) {
			throw new RiverException(e);
		}

		return this;
	}

	@Override
	public Document<local.mock.Document> recalc() {
		try {
			__native.computeWithForm(true, false);
		} catch (DatabaseException e) {
			throw new RiverException(e);
		}
		return this;
	}

	@Override
	public Field getField(String field) {
		Field value = null;

		try {
			Vector<?> temp = null;
			temp = __native.getItemValue(field);
			value = temp == null ? new DefaultField() : new DefaultField(temp);
		} catch (DatabaseException e) {
			throw new RiverException(e);
		}

		return value;
	}

	@Override
	public String getFieldAsString(String field) {
		String result = null;
		try {			
			Vector<?> value = null;
			value = __native.getItemValue(field);  // We must not use getItemValueString(field) because it does not converts from the other types to string 
			result = value.size() > 0 ? Converter.getAsString(value.get(0)) : "";

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

		} catch (DatabaseException e) {
			throw new RiverException(e);
		}

		return result;
	}

	@Override
	public int getFieldAsInteger(String field) {
		int result = 0;
		try {
			Vector<?> value = null;
			value = __native.getItemValue(field);
			result = value.size() > 0 ? Converter.getAsInteger(value.get(0)) : 0;
		} catch (DatabaseException e) {
			throw new RiverException(e);
		}
		return result;
	}

	@Override
	public long getFieldAsLong(String field) {
		long result = 0;
		try {
			Vector<?> value = null;
			value = __native.getItemValue(field);
			result = value.size() > 0 ? Converter.getAsLong(value.get(0)) : 0;
		} catch (DatabaseException e) {
			throw new RiverException(e);
		}
		return result;
	}

	@Override
	public double getFieldAsDouble(String field) {
		double result;
		try {
			Vector<?> value = null;
			value = __native.getItemValue(field);
			result = value.size() > 0 ? Converter.getAsDouble(value.get(0)) : 0;
		} catch (DatabaseException e) {
			throw new RiverException(e);
		}
		return result;
	}

	@Override
	public Date getFieldAsDate(String field) {
		Date result;
		try {
			Vector<?> value = null;
			value = __native.getItemValue(field);
			result = value.size() > 0 ? ((Date) value.get(0)) : null;
		} catch (DatabaseException e) {
			throw new RiverException(e);
		}

		return result;
	}

	@Override
	public boolean isFieldEmpty(String field) {
		boolean result = true;
		try {
			if (__native.hasItem(field)) {
				local.mock.Item __item = __native.getFirstItem(field);
				if (__item != null) {
					if (__item.getType() == local.mock.Item.RICHTEXT) {
						if (!__native.getEmbeddedObjects().isEmpty()) {
							for (@SuppressWarnings("unchecked")
								 Iterator<local.mock.EmbeddedObject> i = __native.getEmbeddedObjects()
							.iterator(); i.hasNext();) {
								local.mock.EmbeddedObject eo = i.next();
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
		} catch (DatabaseException e) {
			throw new RiverException(e);
		}

		return result;
	}

	@Override
	public boolean hasField(String field) {
		boolean result;
		try {
			result = __native.hasItem(field);
		} catch (DatabaseException e) {
			throw new RiverException(e);
		}
		return result;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Map<String, Field> getFields() {
		Map<String, Field> result = null;

		try {
			// logWrapper.debug("getFields: " + _doc.getUniversalID());
			// logWrapper.debug("getFields: loading items");

			Vector<local.mock.Item> items = __native.getItems();

			// logWrapper.debug("getFields: found " + items.size());
			result = new HashMap<String, Field>();

			for (local.mock.Item __item : items) {
				String name = __item.getName();
				int type = __item.getType();
				// logWrapper.debug("getFields: item=" + name + ", type=" + type);

				Field values = null;

				if (type == Item.TEXT
						|| type == Item.NUMBERS
						|| type == Item.NAMES
						|| type == Item.DATETIMES
						|| type == Item.READERS
						|| type == Item.RICHTEXT) 
				{
					Vector<Object> temp = __item.getValues();
					values = temp == null ? new DefaultField() : new DefaultField(temp);
				} else 
				{
					values = new DefaultField();
				}

				// __item.recycle(); // <== Very bad idea? 

				if (values.isEmpty()) {
					// logWrapper.debug("getFields: it's empty");
					values.add("");
				}

				// logWrapper.debug("getFields: saving into the map");
				result.put(name, values);
			}
		} catch (DatabaseException e) {
			throw new RiverException(e);
		}

		return result;
	}

	@Override
	public boolean isOpen() {		
		return (__native != null && !isRecycled()); 
	}

	@Override
	public boolean isNew() {
		boolean result;
		try {
			result = __native.isNewNote();
		} catch (DatabaseException e) {
			throw new RiverException(e);
		}
		return result;
	}

	@Override
	public Document<local.mock.Document> delete() {
		// synchronized (_session){  <== necessary?
		if (__native != null) {
			try {
				__native.removePermanently(true);
			} catch (DatabaseException e) {
				throw new RiverException(e);
			} finally {
				__native = null;
			}
		}

		return this;
		// }
	}

	@Override
	public Document<local.mock.Document> save() {
		try {
			__native.save(true, false);
		} catch (DatabaseException e) {
			throw new RiverException(e);
		}
		return this;
	}

	@Override
	@Deprecated
	public void close() {
		// Don't recycle or close it. Let the server do that.
	}	

	@Override
	public String toString() {
		return getClass().getName() + "(" + objectId + ")";
	}
}
