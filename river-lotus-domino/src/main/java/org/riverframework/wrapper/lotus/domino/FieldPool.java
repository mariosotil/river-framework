package org.riverframework.wrapper.lotus.domino;

import java.lang.reflect.Field;

import org.riverframework.RiverException;
import org.riverframework.utils.AbstractPool;

class FieldPool extends AbstractPool<lotus.domino.Base, Field> {
	@Override
	protected Field calcValue(lotus.domino.Base key) {
		Field field = null;

		try {
			field = key.getClass().getSuperclass().getDeclaredField("isdeleted");
			field.setAccessible(true);
		} catch (Exception e) {
			throw new RiverException(e);
		}

		return field;
	}
	
	@Override
	protected String calcId(lotus.domino.Base key) {
		String id = key.getClass().getSuperclass().getName();
		return id;
	}

}
