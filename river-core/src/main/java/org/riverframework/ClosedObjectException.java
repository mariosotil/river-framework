package org.riverframework;

public class ClosedObjectException extends RiverException {
	private static final long serialVersionUID = 7476690644859920352L;

	public ClosedObjectException() {
		super();
	}

	public ClosedObjectException(String message) {
		super(message);
	}

	public ClosedObjectException(Exception e) {
		super(e);
	}

	public ClosedObjectException(String message, Exception e) {
		super(message, e);
	}
}
