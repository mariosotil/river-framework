package org.riverframework.wrapper.org.openntf.domino;

import org.riverframework.wrapper.AbstractWrapperBase;
import org.riverframework.wrapper.Base;
import org.riverframework.wrapper.Session;

abstract class AbstractBase<N> extends AbstractWrapperBase<N, org.openntf.domino.Session, org.openntf.domino.Base<?>> {

	protected AbstractBase(Session<org.openntf.domino.Session> _session, N __native) {
		super(_session, __native);
	}

}
