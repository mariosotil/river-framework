package org.riverframework.wrapper;

public abstract class AbstractBase<N, S, F> implements Base<N> {
    protected org.riverframework.wrapper.Session<S> _session;
    protected org.riverframework.wrapper.Factory<F> _factory;
    protected volatile N __native;
    protected String objectId;

    @SuppressWarnings("unchecked")
    protected AbstractBase(org.riverframework.wrapper.Session<S> _session, N __native) {
        this.__native = __native;
        this._session = _session;
        _factory = _session == null ? null : (Factory<F>) _session.getFactory();
        objectId = calcObjectId(__native);
    }

    public abstract String calcObjectId(N __native);

    @Override
    public final N getNativeObject() {
        return __native;
    }

    @Override
    public final String getObjectId() {
        return objectId;
    }
}
