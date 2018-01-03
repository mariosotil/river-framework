package local.wrapper;

import local.mock.BaseMock;
import local.mock.SessionMock;
import org.riverframework.wrapper.AbstractBase;
import org.riverframework.wrapper.Session;

abstract class AbstractBaseWrapper<N> extends AbstractBase<N, SessionMock, BaseMock> {
    protected AbstractBaseWrapper(Session<SessionMock> _session, N __native) {
        super(_session, __native);
    }
}
