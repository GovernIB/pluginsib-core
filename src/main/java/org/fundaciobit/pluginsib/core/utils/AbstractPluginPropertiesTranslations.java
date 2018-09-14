package org.fundaciobit.pluginsib.core.utils;


import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.Properties;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import org.fundaciobit.pluginsib.core.utils.AbstractPluginProperties;

/*
 *@author cfuster 
 */


public abstract class AbstractPluginPropertiesTranslations extends AbstractPluginProperties {


	/**
	   * 
	   */
	  public AbstractPluginPropertiesTranslations() {
	    super();
	  }

	  /**
	   * @param propertyKeyBase
	   * @param properties
	   */
	  public AbstractPluginPropertiesTranslations(String propertyKeyBase, Properties properties) {
	    super(propertyKeyBase, properties);
	  }

	  /**
	   * @param propertyKeyBase
	   */
	  public AbstractPluginPropertiesTranslations(String propertyKeyBase) {
	    super(propertyKeyBase);
	  }
	  
	  

	  // ---------------------------------------------------------
	  // ------------------- I18N Utils ------------------------
	  // ---------------------------------------------------------

	  public abstract String getResourceBundleName();

	  public final String getTraduccio(String key, Locale locale, Object... params) {
	    return getTraduccio(getResourceBundleName(), key, locale, params);
	  }

	  public final String getTraduccio(String resourceBundleName, String key, Locale locale,
	      Object... params) {

	    try {
	      // TODO MILLORA: Map de resourcebundle per resourceBundleName i locale

	      ResourceBundle rb = ResourceBundle.getBundle(resourceBundleName, locale, UTF8CONTROL);

	      String msgbase = rb.getString(key);

	      if (params != null && params.length != 0) {
	        msgbase = MessageFormat.format(msgbase, params);
	      }

	      return msgbase;

	    } catch (Exception mre) {
	      //log.error("No trob la traducció per '" + key + "'", new Exception());
	      System.err.println("No trob la traducció per '" + key + "' a la classe " +this.getClass());
	      return key + "_" + locale.getLanguage().toUpperCase();
	    }

	  }

	  protected UTF8Control UTF8CONTROL = new UTF8Control();

	  public class UTF8Control extends ResourceBundle.Control {
	    public ResourceBundle newBundle(String baseName, Locale locale, String format,
	        ClassLoader loader, boolean reload) throws IllegalAccessException,
	        InstantiationException, IOException {
	      // The below is a copy of the default implementation.
	      String bundleName = toBundleName(baseName, locale);
	      String resourceName = toResourceName(bundleName, "properties");
	      ResourceBundle bundle = null;
	      InputStream stream = null;
	      if (reload) {
	        URL url = loader.getResource(resourceName);
	        if (url != null) {
	          URLConnection connection = url.openConnection();
	          if (connection != null) {
	            connection.setUseCaches(false);
	            stream = connection.getInputStream();
	          }
	        }
	      } else {
	        stream = loader.getResourceAsStream(resourceName);
	      }
	      if (stream != null) {
	        try {
	          // Only this line is changed to make it to read properties files as
	          // UTF-8.
	          bundle = new PropertyResourceBundle(new InputStreamReader(stream, "UTF-8"));
	        } finally {
	          stream.close();
	        }
	      }
	      return bundle;
	    }
	  }
	  
	
	
}
