package local.wrapper;

import local.mock.DatabaseException;
import org.riverframework.River;
import org.riverframework.RiverException;
import org.riverframework.wrapper.Document;
import org.riverframework.wrapper.DocumentIterator;
import org.riverframework.wrapper.Session;

class DocumentIteratorFromDocumentCollection extends AbstractBaseNoSQL<local.mock.DocumentCollection> implements DocumentIterator<local.mock.DocumentCollection, local.mock.Document> {
    // private static final Logger log = River.LOG_WRAPPER_LOTUS_DOMINO;

    private local.mock.Document __document = null;
    private Document<local.mock.Document> _doc = null;

    @SuppressWarnings("unchecked")
    protected DocumentIteratorFromDocumentCollection(Session<local.mock.Session> _session, local.mock.DocumentCollection __native) {
        super(_session, __native);

        try {
            __document = __native.getFirstDocument();
        } catch (DatabaseException e) {
            throw new RiverException(e);
        }

        _doc = (Document<local.mock.Document>) _factory.getDocument(__document); //Document<org.riverframework.mock.Base>
    }

    public boolean isRecycled() {
        return AbstractBaseNoSQL.isObjectRecycled(__native);
    }

    public String calcObjectId(local.mock.DocumentCollection __object) {
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
    public Document<local.mock.Document> next() {
        Document<local.mock.Document> _current = _doc;

        try {
            __document = __native.getNextDocument(__document);
        } catch (DatabaseException e) {
            throw new RiverException(e);
        }

        _doc = (Document<local.mock.Document>) _factory.getDocument(__document); //Document<org.riverframework.mock.Base>

        return _current;
    }

    @Override
    public DocumentIterator<local.mock.DocumentCollection, local.mock.Document> iterator() {
        return this;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();

    }

    @Override
    public DocumentIterator<local.mock.DocumentCollection, local.mock.Document> deleteAll() {
        for (Document<local.mock.Document> doc : this) {
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
