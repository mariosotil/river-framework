package org.riverframework;

@SuppressWarnings("serial")
public class RiverException extends RuntimeException {
	// static final long serialVersionUID = 1L;

	public RiverException() {
		super();
	}

	public RiverException(String message) {
		super(message);
	}

	public RiverException(Exception e) {
		super(e);
	}

	public RiverException(String message, Exception e) {
		super(message, e);
	}

}
