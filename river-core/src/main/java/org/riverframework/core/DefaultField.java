package org.riverframework.core;

import java.util.ArrayList;
import java.util.Collection;

public class DefaultField extends ArrayList<Object> implements Field {
	private static final long serialVersionUID = 5873581772813746498L;

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
