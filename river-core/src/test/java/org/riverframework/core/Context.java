package org.riverframework.core;

import java.io.File;

import org.ini4j.Wini;
import org.riverframework.RiverException;
import org.riverframework.core.Credentials;
import org.riverframework.wrapper.SessionFactory;
import org.riverframework.wrapper.SessionModule;

import com.google.inject.Guice;
import com.google.inject.Injector;

public final class Context {
	private static final Context INSTANCE = new Context();

	private String localDatabase = "";
	private String remoteDatabase = "";
	private String server = "";

	protected Context() {
		// Is protected to defeat instantiation.
		try {
			String location = System.getProperty("user.home") + File.separator + ".river-framework" + File.separator
					+ "test-context";

			Wini context = new Wini(new File(location));

			localDatabase = context.get("default", "local-database");
			remoteDatabase = context.get("default", "remote-database");
			server = context.get("default", "server");

		} catch (Exception e) {
			throw new RiverException(e);
		}
	}

	public static Context getInstance() {
		return INSTANCE;
	}

	public String getDatabase() {
		return localDatabase;
	}

	public String getServer() {
		return server;
	}

	public String getRemoteDatabase() {
		return remoteDatabase;
	}

	public org.riverframework.wrapper.Session getSession() {
		org.riverframework.wrapper.Session _session = null;
		Injector injector = Guice.createInjector(new SessionModule());
		SessionFactory sessionFactory = injector.getInstance(SessionFactory.class);

		_session = sessionFactory.createDomino(null, null, Credentials.getPassword());
		// _session = sessionFactory.createOpenntf(null, null, Credentials.getPassword());

		return _session;
	}
}
