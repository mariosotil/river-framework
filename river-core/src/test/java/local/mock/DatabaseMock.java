package local.mock;

import org.dizitart.no2.Nitrite;

public class DatabaseMock extends BaseMock {
    private SessionMock __session;
    private Nitrite ___database;
    private String databaseName;

    public DatabaseMock(SessionMock __session, Nitrite ___database, String databaseName) {
        this.__session = __session;
        this.___database = ___database;
        this.databaseName = databaseName;
    }

    public String getTitle() {
        return null;
    }

    public String getFileName()  {
        return null;
    }

    public String getServer()  {
        return null;
    }

    public String getFilePath()  {
        return null;
    }

    public boolean isOpen() {
        return ___database != null;
    }

    public DocumentMock createDocument() {
        return null;
    }

    public DocumentMock getDocumentByUNID(String unid)  {
        return null;
    }

    public DocumentMock getDocumentByID(String id)  {
        return null;
    }

    public void delete() {
        __session.removeDatabase(databaseName);
    }

    public ViewMock createView(String... parameters)  {
        return null;
    }


    public ViewMock getView(String... parameters) {
        return null;
    }

    public DocumentCollectionMock getAllDocuments()  {
        return null;
    }

    public DocumentCollectionMock search(Object... parameters)  {
        return null;
    }

    public void updateFTIndex(Object... parameters)  {

    }

    public void close() {
        ___database.close();
        ___database = null;
    }
}
