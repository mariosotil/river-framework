package org.riverframework.wrapper;

public interface NativeReferenceCollector extends Runnable {
	public void terminate();

	public void logStatus();
}
