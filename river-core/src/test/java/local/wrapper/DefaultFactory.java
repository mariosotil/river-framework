package local.wrapper;

import local.mock.*;
import org.riverframework.RiverException;
import org.riverframework.wrapper.*;
import org.riverframework.wrapper.Base;

import java.lang.ref.Reference;
import java.util.logging.Level;

public class DefaultFactory extends AbstractFactory<BaseMock> {

    private static DefaultFactory instance = null;
    private boolean isRemoteSession = false;

    protected DefaultFactory(Class<? extends AbstractNativeReference<BaseMock>> nativeReferenceClass) {
        super(nativeReferenceClass);
    }

    public static DefaultFactory getInstance() {
        if (instance == null) {
            // Thread Safe. Might be costly operation in some case
            synchronized (DefaultFactory.class) {
                if (instance == null) {
                    instance = new DefaultFactory(DefaultNativeReference.class);
                }
            }
        }
        return instance;
    }

    @Override
    public boolean getIsRemoteSession() {
        return isRemoteSession;
    }

    @Override
    protected boolean isValidNativeObject(BaseMock __native) {
        return __native != null; // && !DefaultBase.isRecycled(__native);
    }

    @SuppressWarnings("unchecked")
    public Session<SessionMock> getSession(Object... parameters) {
        SessionMock __obj = new SessionMock();

        _session = getWrapper(DefaultSession.class, SessionMock.class, __obj);
        return (Session<SessionMock>) _session;
    }

    @Override
    public <U extends BaseMock> Database<DatabaseMock> getDatabase(U __obj) {
        return getWrapper(DefaultDatabase.class, DatabaseMock.class, __obj);
    }

    @Override
    public Document<DocumentMock> getDocument(String objectId) {
        return getWrapper(DefaultDocument.class, DocumentMock.class, objectId);
    }

    @Override
    public <U extends BaseMock> Document<DocumentMock> getDocument(U __obj) {
        return getWrapper(DefaultDocument.class, DocumentMock.class, __obj);
    }

    @Override
    public <U extends BaseMock> View<ViewMock> getView(U __obj) {
        return getWrapper(DefaultView.class, ViewMock.class, __obj);
    }

    @Override
    public <U extends BaseMock> DocumentIterator<BaseMock, DocumentMock> getDocumentIterator(U __obj) {
        DocumentIterator<BaseMock, DocumentMock> _iterator = null;

        if (__obj instanceof DocumentCollectionMock) {
            _iterator = getWrapper(DefaultDocumentIterator.class, DocumentCollectionMock.class, __obj);

        } else {
            throw new RiverException("Expected an object org.riverframework.mock.View, DocumentCollection, or ViewEntryCollection.");
        }

        return _iterator;
    }

    @Override
    public void cleanUp(Base<? extends BaseMock>... except) {
        Reference<? extends Base<BaseMock>> ref = null;

        boolean cleaning = false;
        long start = 0;
        String exceptId = null;

        // If there is no a session created, return
        if (_session == null) return;


        if (except.length > 0) {
            // If the object to be keeped is not a document, return
            if (!(except[0] instanceof DocumentMock)) return;

            // Otherwise, we save its Object Id
            exceptId = except[0].getObjectId();
        } else {
            exceptId = "";
        }

        while ((ref = wrapperQueue.poll()) != null) {
            if (!cleaning) {
                start = System.nanoTime();
                cleaning = true;
            }

            // synchronized (_session){  <== necessary?
            AbstractNativeReference<BaseMock> nat = nativeReferenceClass.cast(ref);
            BaseMock __native = nat.getNativeObject();

            if (__native instanceof DocumentMock ||
                    __native instanceof DocumentMock) {
                String id = nat.getObjectId();

                if (exceptId.equals(id)) {
                    if (log.isLoggable(Level.FINEST)) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("NO Recycling: id=");
                        sb.append(id);
                        sb.append(" native=");
                        sb.append(__native == null ? "<null>" : __native.getClass().getName());
                        sb.append(" (");
                        sb.append(__native == null ? "<null>" : __native.hashCode());
                        sb.append(")");

                        log.finest(sb.toString());
                    }

                } else {
                    if (log.isLoggable(Level.FINEST)) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("Recycling: id=");
                        sb.append(id);
                        sb.append(" native=");
                        sb.append(__native == null ? "<null>" : __native.getClass().getName());
                        sb.append(" (");
                        sb.append(__native == null ? "<null>" : __native.hashCode());
                        sb.append(")");

                        log.finest(sb.toString());
                    }

                    wrapperMap.remove(id);
                    nat.close();
                }
            }
            // }
        }

        if (cleaning) {
            long end = System.nanoTime();
            log.finest("Finished clean up. Elapsed time=" + ((end - start) / 1000000) + "ms");
            cleaning = false;
        }
    }


}