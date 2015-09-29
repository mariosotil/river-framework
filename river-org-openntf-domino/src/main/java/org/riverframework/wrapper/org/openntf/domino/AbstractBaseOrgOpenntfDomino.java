package org.riverframework.wrapper.org.openntf.domino;

import org.riverframework.wrapper.AbstractBase;
import org.riverframework.wrapper.Session;

abstract class AbstractBaseOrgOpenntfDomino<N> extends AbstractBase<N, org.openntf.domino.Session, org.openntf.domino.Base<?>> {

	protected AbstractBaseOrgOpenntfDomino(Session<org.openntf.domino.Session> _session, N __native) {
		super(_session, __native);
	}

}
