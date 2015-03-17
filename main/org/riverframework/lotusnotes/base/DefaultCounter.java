package org.riverframework.lotusnotes.base;

import org.riverframework.RiverException;
import org.riverframework.lotusnotes.Counter;
import org.riverframework.lotusnotes.Database;
import org.riverframework.lotusnotes.Document;
import org.riverframework.lotusnotes.Session;
import org.riverframework.lotusnotes.View;

public class DefaultCounter implements Counter {
	protected final static String FORM_NAME = Session.PREFIX + "Counter";
	protected final static String FIELD_ID = Session.PREFIX + "Id";
	protected final static String FIELD_COUNT = Session.PREFIX + "Count";

	protected View index = null;
	protected Database database = null;

	protected DefaultCounter(Database d) {
		database = d;
		index = d.getView(DefaultView.class, Session.PREFIX + "Counter_Index");
		if (!index.isOpen())
			throw new RiverException("There is no a index view '" + Session.PREFIX + "Counter_Index'");
	}

	@Override
	public long generateId(String key) {
		// TODO: synchronize this
		index.refresh();
		Document doc = index.getDocumentByKey(DefaultDocument.class, key);

		long n = 0;

		if (doc.isOpen()) {
			n = doc.getFieldAsInteger(FIELD_COUNT) + 1;
			doc.setField(FIELD_COUNT, n).save();

		} else {
			n = 1;
			doc = database.createDocument(DefaultDocument.class);
			doc.setForm(FORM_NAME)
					.setField(FIELD_ID, key)
					.setField(FIELD_COUNT, 1)
					.save();

		}
		return n;
	}
}
