package org.riverframework.core;

import org.riverframework.Document;

import com.google.inject.name.Named;

public interface DocumentFactory {
	@Named("DefaultDocument")
	Document create();

}
