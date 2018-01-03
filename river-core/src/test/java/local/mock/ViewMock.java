package local.mock;

public class ViewMock extends BaseMock {
    public String getName() {
        return null;
    }

    public void updateIndex() {

    }

    public void delete() {

    }

    public void remove()  {

    }

    public void refresh()  {

    }

    public void FTSearch(Object... parameters) {}

    public void setAutoUpdate(boolean autoupdate) {

    }

    public DatabaseMock getParent() { return null; }

    public DocumentMock getDocumentByKey(Object... parameters) { return null; }

    public DocumentMock getFirstDocument() { return null; }

    public DocumentMock getNextDocument(DocumentMock doc) { return null; }

    public DocumentCollectionMock getAllEntries() { return null; }

    public DocumentCollectionMock getAllDocumentsByKey(Object... paramters) { return null; }

    public int getColumnCount() { return 0; }
}
