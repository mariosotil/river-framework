package org.riverframework.core;

import java.util.ArrayList;
import java.util.Collection;

import org.riverframework.Field;

public class DefaultField extends ArrayList<Object> implements Field {
	private static final long serialVersionUID = 331527408098154987L;

	public DefaultField() {
		super();
	}

	public DefaultField(Collection<? extends Object> c) {
		super(c);
	}

	public DefaultField(int initialCapacity) {
		super(initialCapacity);
	}
}
