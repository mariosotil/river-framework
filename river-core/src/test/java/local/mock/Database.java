package local.mock;

public class Database extends Base {
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
        return true;
    }

    public Document createDocument() {
        return null;
    }

    public Document getDocumentByUNID(String unid)  {
        return null;
    }

    public Document getDocumentByID(String id)  {
        return null;
    }

    public void delete() {

    }

    public View createView(String... parameters)  {
        return null;
    }


    public View getView(String... parameters) {
        return null;
    }

    public DocumentCollection getAllDocuments()  {
        return null;
    }

    public DocumentCollection search(Object... parameters)  {
        return null;
    }

    public void updateFTIndex(Object... parameters)  {

    }

    public void remove() {}
}
