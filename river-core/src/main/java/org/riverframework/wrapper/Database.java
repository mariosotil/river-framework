package org.riverframework.wrapper;

/**
 * Defines the common operations to control a NoSQL database.
 * 
 * @author mario.sotil@gmail.com
 *
 */
public interface Database<N> extends Base<N> {
	public String getServer();

	public String getFilePath();

	public String getName();

	public Document<N> createDocument(String... parameters);

	public Document<N> getDocument(String... parameters);

	public View<N> createView(String... parameters);

	public View<N> getView(String... parameters);

	public DocumentIterator<N> getAllDocuments();

	public DocumentIterator<N> search(String query);

	public Database<N> refreshSearchIndex();

	public void delete();
}
