package org.riverframework.wrapper;

/**
 * Defines the common operations to control a NoSQL database.
 * 
 * @author mario.sotil@gmail.com
 *
 */
public interface Database extends Base {
	public String getServer();

	public String getFilePath();

	public String getName();

	public Document createDocument(String... parameters);

	public Document getDocument(String... parameters);

	public View getView(String... parameters);

	public DocumentIterator getAllDocuments();

	public DocumentIterator search(String query);

	public Database refreshSearchIndex();

	public void remove();
}
