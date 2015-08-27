package org.riverframework.utils;

import java.security.AccessControlException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.riverframework.RiverException;

public class LoggerHelper {
	
	private Formatter formatter = new SimpleFormatter();  
	private Logger log = null;
	private Level level = Level.OFF;
		
	public LoggerHelper(Logger log) {
		this.log = log;
		
		Level level = null;
		Logger parent = log;
		while (parent != null && ((level = parent.getLevel()) == null)) {
			parent = parent.getParent();
		}
		
		this.level = level == null ? Level.OFF : level;
	}

	public LoggerHelper clearHandlers() {
		for (Handler h : log.getHandlers()) {
			log.removeHandler(h);
		}
		return this;
	}
	
	public LoggerHelper setFormatter(Formatter formatter) {
		this.formatter = formatter;
		
		for (Handler h : log.getHandlers()) {
			h.setFormatter(formatter);
		}		
		
		return this;
	}

	public LoggerHelper addConsoleHandler() {
		ConsoleHandler hConsole = new ConsoleHandler();
		hConsole.setFormatter(formatter);
		hConsole.setLevel(level);
		log.addHandler(hConsole);

		return this;
	}

	public LoggerHelper addFileHandler(String filename) {
		try {
			int pos1 = filename.indexOf("{{");
			int pos2 = filename.indexOf("}}");

			String modifiedFilename = null;
			
			if (pos1 > -1 && pos2 > -1) {
				SimpleDateFormat sdf = new SimpleDateFormat(filename.substring(pos1 + 2, pos2));
				String date = sdf.format(new Date());
			
				modifiedFilename = filename.substring(0, pos1) + date + filename.substring(pos2 + 2, filename.length());
			} else {
				modifiedFilename = filename;
			}
			
			FileHandler hFile = new FileHandler(modifiedFilename);
			hFile.setFormatter(formatter);  
			hFile.setLevel(level);
			log.addHandler(hFile);
		} catch (Exception e) {
			throw new RiverException(e);
		}  
		
		return this;
	}

	public LoggerHelper setUseParentHandlers(boolean use) { 
		log.setUseParentHandlers(use);
		
		return this;
	}
	
	public LoggerHelper setLevel(Level level) {
		for (Handler h : log.getHandlers()) {
			h.setLevel(level);
		}
		
		try {
			log.setLevel(level);
		} catch (AccessControlException e) {
			log.warning("AccessControlException: it was not possible to set the log level. Message:" + e.getMessage());
		}

		this.level = level;

		return this;
	}
}
