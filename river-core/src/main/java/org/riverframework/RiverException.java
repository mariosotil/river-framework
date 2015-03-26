package org.riverframework;

public class RiverException extends RuntimeException {
	private static final long serialVersionUID = 6867771909552313131L;

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
