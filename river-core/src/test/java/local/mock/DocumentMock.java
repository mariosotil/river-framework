package local.mock;

import java.util.Map;
import java.util.Vector;

public class DocumentMock extends BaseMock {
    public DatabaseMock getDatabase() {
        return null;
    }

    public String getId() {
        return null;
    }

    public Object getProperty(String name) {
        return null;
    }

    public void putProperties(Map<String, Object> properties) {

    }

    public Map<String, Object> getProperties() {
        return null;
    }

    public void delete() {

    }

    public String getUniversalID()  {
        return null;
    }

    public DatabaseMock getParentDatabase()  {
        return null;
    }

    public Item replaceItemValue(Object... parameters)  {
        return null;
    }

    public void computeWithForm(Object... parameter) {

    }

    public Vector<?> getItemValue(String name) { return null; }

    public boolean hasItem(String name) { return false; }

    public Item getFirstItem(String name) { return null; }

    public Vector<EmbeddedObject> getEmbeddedObjects() { return null; }

    public Vector<Item> getItems() { return null; }

    public boolean isNewNote() { return false; }

    public void removePermanently(Object... parameters) { }

    public void save(Object... parameters) { }

    public boolean isDeleted() { return false; }
}
