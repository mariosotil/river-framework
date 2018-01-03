package local.wrapper;

import local.mock.BaseMock;
import org.riverframework.River;
import org.riverframework.wrapper.AbstractNativeReference;
import org.riverframework.wrapper.Base;

import java.lang.ref.ReferenceQueue;
import java.util.logging.Logger;

class DefaultNativeReference extends AbstractNativeReference<BaseMock> {
    private static final Logger log = River.LOG_WRAPPER_LOTUS_DOMINO;

    public DefaultNativeReference(Base<BaseMock> referent,
                                  ReferenceQueue<Base<BaseMock>> q) {
        super(referent, q);
    }

    @Override
    public void close() {
    }
}

