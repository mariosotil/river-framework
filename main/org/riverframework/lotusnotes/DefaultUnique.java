package org.riverframework.lotusnotes;

import lotus.domino.Document;

import org.riverframework.Session;
import org.riverframework.Unique;
import org.riverframework.development.Relationable;

public class DefaultUnique extends org.riverframework.fw.AbstractUnique<lotus.domino.Document> {
	/**
	 * This view is the Counter documents index
	 */
	public static final String VIEW_INDEX = Session.PREFIX + "CounterIndex";

	protected org.riverframework.lotusnotes.DefaultDatabase rDatabase = null;
	protected org.riverframework.lotusnotes.DefaultView rIndex = null;

	public DefaultUnique(org.riverframework.lotusnotes.DefaultDocument doc) {
		super(doc);
		rDatabase = (DefaultDatabase) doc.getDatabase();
	}

	public org.riverframework.lotusnotes.DefaultView getIndex() {
		// Should objects like this be singletons? Interface "Single"?
		if (rIndex == null) {
			rIndex = rDatabase.getView(DefaultView.class, VIEW_INDEX);
		}
		return rIndex;
	}

	public static String getAsCounterKey(String classDocument, String id) {
		return classDocument.toUpperCase() + "-" + id;
	}

	@Override
	public int getCounter(String key) {
		DefaultUnique.Counter rDoc = null;

		rDoc = (DefaultUnique.Counter) rIndex.getDocumentByKey(key);
		if (!rDoc.isOpen()) {
			rDoc = rDatabase.createDocument(DefaultUnique.Counter.class);
		}

		int counter = rDoc.getCounter();
		return counter;
	}

	@Override
	public String getIdKey() {
		return "";
	}

	@Override
	public String getIdComplete() {
		int c = rDoc.getFieldAsInteger(FIELD_ID_COUNTER);

		return String.format("%06d", c);
	}

	@Override
	public Unique calcId() {
		String currentKey = rDoc.getFieldAsString(FIELD_ID_KEY);

		String newKey = getIdKey();
		String newCode = "";

		if (!currentKey.equals(newKey) || rDoc.isFieldEmpty(FIELD_ID_COUNTER)) {

			// Counter is a Document with its Index View!!
			int newCounter = getCounter(getAsCounterKey(rDoc.getClass().getSimpleName(), newKey));

			rDoc.setField(FIELD_ID_KEY, newKey);
			rDoc.setField(FIELD_ID_COUNTER, newCounter);

			newCode = getIdComplete();
			rDoc.setField(org.riverframework.Unique.FIELD_ID, newCode);
		}

		if (this instanceof Relationable) {
			// Propagate to children
		}

		return this;
	}

	private class Counter extends org.riverframework.lotusnotes.DefaultDocument implements
			org.riverframework.Unique {
		/**
		 * This is the counter that will be used to generate the key
		 */
		public static final String FIELD_ID_COUNTER = Session.PREFIX + "Counter";

		protected Counter(DefaultDatabase d, Document doc) {
			super(d, doc);
		}

		@Override
		protected org.riverframework.lotusnotes.DefaultDocument afterCreate() {
			setField(FIELD_ID_COUNTER, 0);
			return this;
		}

		public int getCounter() {
			// TODO: Optimize for Lotus Notes
			int counter = getFieldAsInteger(FIELD_ID_COUNTER);
			setField(FIELD_ID_COUNTER, ++counter);
			save(true);
			return counter;
		}

		@Override
		public Unique calcId() {
			return this;
		}

		@Override
		public final String getId() {
			return rDoc.getFieldAsString(org.riverframework.Unique.FIELD_ID);
		}

	}
}
