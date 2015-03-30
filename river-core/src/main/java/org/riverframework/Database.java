package org.riverframework;

public interface Database extends Base {
	public Session getSession();

	public Document createDocument(String... parameters);

	public View getView(String... parameters);

	public <U extends View> U getView(Class<U> clazz, String... parameters);

	public <U extends Document> U createDocument(Class<U> clazz, String... parameters);

	public Document getDocument(String... parameters);

	public <U extends Document> U getDocument(Class<U> clazz, String... parameters);

	public <U extends Document> U getDocument(Class<U> clazz, boolean createIfDoesNotExist, String... parameters);

	public <U extends Document> U getDocument(Class<U> clazz, org.riverframework.wrapper.Document doc);

	public Document getDocument(org.riverframework.wrapper.Document doc);

	public DocumentCollection getAllDocuments();

	public DocumentCollection search(String query);

	public Database refreshSearchIndex();

	public Counter getCounter(String key);

	public boolean isOpen();

	public String getServer();

	public String getFilePath();

	public String getName();

}
