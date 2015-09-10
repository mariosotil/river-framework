package org.riverframework.wrapper.org.openntf.domino;

import org.riverframework.wrapper.AbstractBase;
import org.riverframework.wrapper.Session;

abstract class AbstractBaseOpenNTF<N> extends AbstractBase<N, org.openntf.domino.Session, org.openntf.domino.Base<?>> {

	protected AbstractBaseOpenNTF(Session<org.openntf.domino.Session> _session, N __native) {
		super(_session, __native);
	}

}
