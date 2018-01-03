package local.wrapper;

import local.mock.MockException;
import local.mock.DocumentCollectionMock;
import local.mock.DocumentMock;
import local.mock.SessionMock;
import org.riverframework.River;
import org.riverframework.RiverException;
import org.riverframework.wrapper.Document;
import org.riverframework.wrapper.DocumentIterator;
import org.riverframework.wrapper.Session;

class DocumentIteratorFromDocumentCollection extends AbstractBaseWrapper<DocumentCollectionMock> implements DocumentIterator<DocumentCollectionMock, DocumentMock> {
    // private static final Logger log = River.LOG_WRAPPER_LOTUS_DOMINO;

    private DocumentMock __document = null;
    private Document<DocumentMock> _doc = null;

    @SuppressWarnings("unchecked")
    protected DocumentIteratorFromDocumentCollection(Session<SessionMock> _session, DocumentCollectionMock __native) {
        super(_session, __native);

        try {
            __document = __native.getFirstDocument();
        } catch (MockException e) {
            throw new RiverException(e);
        }

        _doc = (Document<DocumentMock>) _factory.getDocument(__document); //Document<org.riverframework.mock.Base>
    }

    public String calcObjectId(DocumentCollectionMock __object) {
        String objectId = "";
        if (__object != null) { // && !isRecycled(__object)) {

            StringBuilder sb = new StringBuilder();
            sb.append(__object.getClass().getName());
            sb.append(River.ID_SEPARATOR);
            sb.append(__object.hashCode());

            objectId = sb.toString();
        }
        return objectId;
    }

    @Override
    public boolean hasNext() {
        return __document != null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Document<DocumentMock> next() {
        Document<DocumentMock> _current = _doc;

        try {
            __document = __native.getNextDocument(__document);
        } catch (MockException e) {
            throw new RiverException(e);
        }

        _doc = (Document<DocumentMock>) _factory.getDocument(__document); //Document<org.riverframework.mock.Base>

        return _current;
    }

    @Override
    public DocumentIterator<DocumentCollectionMock, DocumentMock> iterator() {
        return this;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();

    }

    @Override
    public DocumentIterator<DocumentCollectionMock, DocumentMock> deleteAll() {
        for (Document<DocumentMock> doc : this) {
            doc.delete();
        }

        return this;
    }

    @Override
    public boolean isOpen() {
        return __native != null; // && !isRecycled(__documentCollection);
    }

    @Override
    @Deprecated
    public void close() {

    }

    @Override
    public String toString() {
        return getClass().getName() + "(" + objectId + ")";
    }
}
