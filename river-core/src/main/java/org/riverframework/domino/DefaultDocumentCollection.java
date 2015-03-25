package org.riverframework.domino;

import java.util.ArrayList;

import org.riverframework.RiverException;

import lotus.domino.NotesException;

public class DefaultDocumentCollection extends ArrayList<org.riverframework.domino.Document> 
implements org.riverframework.domino.DocumentCollection{
	private static final long serialVersionUID = -5032050258891587783L;
	protected Database database;

	public DefaultDocumentCollection(Database d) {
		database = d;
	}

	@Override
	public org.riverframework.domino.DocumentCollection loadFrom(lotus.domino.DocumentCollection _col) {
		lotus.domino.Document _doc = null;
		
		clear();
		try {
			_doc = _col.getFirstDocument();
			while (_doc != null) {
				org.riverframework.domino.Document doc = database.getDocument(_doc);
				this.add(doc);
				_doc = _col.getNextDocument(_doc);
			}
		} catch (NotesException e) {
			throw new RiverException();
		}
		
		return this;
	}
	
	@Override
	public org.riverframework.domino.DocumentCollection loadFrom(lotus.domino.View _view) {
		lotus.domino.Document _doc = null;
		
		clear();
		try {
			_doc = _view.getFirstDocument();
			while (_doc != null) {
				org.riverframework.domino.Document doc = database.getDocument(_doc);
				this.add(doc);
				_doc = _view.getNextDocument(_doc);
			}
		} catch (NotesException e) {
			throw new RiverException();
		}
		
		return this;
	}
	
	@Override
	public org.riverframework.domino.DocumentCollection loadFrom(lotus.domino.ViewEntryCollection _col) {
		lotus.domino.ViewEntry _entry = null;
		
		clear();
		try {
			_entry = _col.getFirstEntry();
			while (_entry != null) {
				org.riverframework.domino.Document doc = database.getDocument(_entry.getDocument());
				this.add(doc);
				_entry = _col.getNextEntry(_entry);
			}
		} catch (NotesException e) {
			throw new RiverException();
		}
		
		return this;
	}
	
	@Override
	public org.riverframework.domino.Database getDatabase() {
		return database;
	}
	
	@Override
	public org.riverframework.domino.DocumentCollection deleteAll() {
		for(org.riverframework.domino.Document doc : this) {
			doc.delete();
		}
		return this;	
	}
}
