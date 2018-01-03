package local.wrapper;

import local.mock.*;
import org.riverframework.River;
import org.riverframework.RiverException;
import org.riverframework.wrapper.Database;
import org.riverframework.wrapper.Document;
import org.riverframework.wrapper.DocumentIterator;
import org.riverframework.wrapper.View;

class DefaultDatabase extends AbstractBaseWrapper<DatabaseMock> implements org.riverframework.wrapper.Database<DatabaseMock> {
    // private static final Logger log = River.LOG_WRAPPER_LOTUS_DOMINO;

    protected DefaultDatabase(org.riverframework.wrapper.Session<SessionMock> _session, DatabaseMock __native) {
        super(_session, __native);
    }

    public String calcObjectId(DatabaseMock __database) {
        String objectId = "";

        if (__database != null) { // && !isRecycled(__database)) {
            try {
                StringBuilder sb = new StringBuilder(1024);
                sb.append(__database.getServer());
                sb.append(River.ID_SEPARATOR);
                sb.append(__database.getFilePath());

                objectId = sb.toString();
            } catch (MockException e) {
                throw new RiverException(e);
            }
        }

        return objectId;
    }

    @Override
    public String getServer() {
        try {
            return __native.getServer();
        } catch (MockException e) {
            throw new RiverException(e);
        }
    }

    @Override
    public String getFilePath() {
        try {
            return __native.getFilePath();
        } catch (MockException e) {
            throw new RiverException(e);
        }
    }

    @Override
    public String getName() {
        try {
            String name = __native.getTitle();
            return name.equals("") ? __native.getFileName() : name;
        } catch (MockException e) {
            throw new RiverException(e);
        }
    }

    @Override
    public boolean isOpen() {
        try {
            return (__native != null && __native.isOpen());
        } catch (MockException e) {
            throw new RiverException(e);
        }
    }

    @Override
    public Document<DocumentMock> createDocument(String... parameters) {
        DocumentMock __doc = null;

        try {
            __doc = __native.createDocument();
        } catch (MockException e) {
            throw new RiverException(e);
        }

        @SuppressWarnings("unchecked")
        Document<DocumentMock> doc = (Document<DocumentMock>) _factory.getDocument(__doc);

        return doc;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Document<DocumentMock> getDocument(String... parameters) {
        DocumentMock __doc = null;
        Document<DocumentMock> doc = null;

        if (parameters.length > 0) {
            String id = parameters[0];

            doc = (Document<DocumentMock>) _factory.getDocument(id);

            if (!doc.isOpen()) {
                try {
                    if (id.length() == 32) {
                        __doc = __native.getDocumentByUNID(id);
                    }
                } catch (Exception e) {
                    // Do nothing
                }

                try {
                    if (__doc == null && id.length() == 8) {
                        __doc = __native.getDocumentByID(id);
                    }
                } catch (Exception e) {
                    // Do nothing
                }

                doc = (Document<DocumentMock>) _factory.getDocument(__doc);
            }
        } else {
            doc = (Document<DocumentMock>) _factory.getDocument((DocumentMock) null);
        }

        return doc;
    }

    @Override
    public View<ViewMock> createView(String... parameters) {
        ViewMock __view = null;
        String name = null;

        try {
            if (parameters.length == 1) {
                name = parameters[0];
                __view = __native.createView(name);
            } else if (parameters.length == 2) {
                name = parameters[0];
                String selectionFormula = parameters[1];
                __view = __native.createView(name, selectionFormula);
            } else {
                throw new RiverException("It was expected these parameters: name (required) and selection formula (optional).");
            }
        } catch (MockException e) {
            throw new RiverException(e);
        }

        View<ViewMock> _view = getView(name);
        return _view;
    }

    @Override
    public View<ViewMock> getView(String... parameters) {
        ViewMock __view = null;

        try {
            if (parameters.length > 0) {
                String id = parameters[0];
                __view = __native.getView(id);
            }

            if (__view != null)
                __view.setAutoUpdate(false);

        } catch (MockException e) {
            throw new RiverException(e);
        }

        @SuppressWarnings("unchecked")
        View<ViewMock> _view = (View<ViewMock>) _factory.getView(__view);
        return _view;
    }

    @Override
    public DocumentIterator<BaseMock, DocumentMock> getAllDocuments() {
        DocumentCollectionMock _col;

        try {
            _col = __native.getAllDocuments();
        } catch (MockException e) {
            throw new RiverException(e);
        }

        @SuppressWarnings("unchecked")
        DocumentIterator<BaseMock, DocumentMock> _iterator =
                (DocumentIterator<BaseMock, DocumentMock>) _factory.getDocumentIterator(_col);
        return _iterator;
    }

    @Override
    public DocumentIterator<BaseMock, DocumentMock> search(String query) {
        DocumentIterator<BaseMock, DocumentMock> _iterator =
                search(query, 0);
        return _iterator;
    }

    @Override
    public DocumentIterator<BaseMock, DocumentMock> search(String query, int max) {
        DocumentCollectionMock _col;

        try {
            _col = __native.search(query, null, max);
        } catch (MockException e) {
            throw new RiverException(e);
        }

        @SuppressWarnings("unchecked")
        DocumentIterator<BaseMock, DocumentMock> _iterator =
                (DocumentIterator<BaseMock, DocumentMock>) _factory.getDocumentIterator(_col);
        return _iterator;
    }

    @Override
    public Database<DatabaseMock> refreshSearchIndex(boolean createIfNotExist) {
        try {
            __native.updateFTIndex(createIfNotExist);
        } catch (MockException e) {
            throw new RiverException(e);
        }
        return this;
    }

    @Override
    public void delete() {
        try {
            if (__native != null)
                __native.delete();
        } catch (MockException e) {
            throw new RiverException(e);
        }

        close();
    }

    @Override
    public String toString() {
        return getClass().getName() + "(" + objectId + ")";
    }

    @Override
    @Deprecated
    public void close() {
        // Don't recycle or close it. Let the server do that.
    }
}
