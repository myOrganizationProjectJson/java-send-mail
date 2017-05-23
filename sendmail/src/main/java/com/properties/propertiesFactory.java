package com.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;
public class propertiesFactory {
	private static propertiesFactory _instance = null;
	private Properties properties = new Properties();

	private propertiesFactory() {
		try {
			// InputStream inputStream = this.getClass().getResourceAsStream("jdbc.properties");
			InputStream inputStream = new FileInputStream(new File("./config.properties"));
			properties.load(inputStream);
			if (inputStream != null)
				inputStream.close();
		} catch (Exception e) {
			System.out.println(e + "file not found");
		}
	}
	synchronized public static propertiesFactory getInstance() {
		if (_instance == null) {
			_instance = new propertiesFactory();
		}
		return _instance;
	}
	public propertiesFactory clone() {
		return getInstance();
	}
	public String getConfig(String key) {
		return properties.getProperty(key);
	}
	private boolean isKeyExist(String key) {
		boolean exist = false;
		Set<Object> keys = properties.keySet();
		for (Object object : keys) {
			if (object.toString().equals(key)) {
				exist = true;
				break;
			}
		}
		return exist;
	}
	public String getConfig(String key, String defaultValue) {
		if (isKeyExist(key))
			return properties.getProperty(key);
		else
			return defaultValue;
	}
}