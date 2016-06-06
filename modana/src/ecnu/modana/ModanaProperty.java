package ecnu.modana;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * Modana configuration information
 * @author cb
 */
class ModanaProperty {
	
	private Logger logger = Logger.getRootLogger();
	
	//keys of properties is as follows:
	public final static String pluginDir = "modana.pluginDir";
	public final static String pluginCfgFile = "modana.pluginCfgFile";
	public final static String pluginCfgXsdFile = "modana.xsd.pluginCfgFile";
	
	//class path is not read from properties
	public final static String classPath = "modana.classPath";
	
	private HashMap<String, String> props;
	
	/**
	 * Preprocess and save the information read from java properties file 
	 * @param p Java properties file
	 */
	public ModanaProperty(Properties p) throws Exception {
		props = new HashMap<String, String>();
		//get class path
		String clspath = Modana.class.getClassLoader().getResource("").toURI().getPath();
		props.put(classPath, clspath);
		if (logger.isDebugEnabled()) {
			logger.debug(classPath+"="+props.get(classPath));
		}
		
		Enumeration<Object> en = p.keys();
		while (en.hasMoreElements()) {
			String key = (String) en.nextElement();
			if (key.equals(pluginDir)) {
				//get the plugin directory, otherwise a default class path
				String value = (String) p.get(pluginDir);
				if (value.equals("")) {
					value = props.get(classPath); //default class path
				} else {
					//convert a relative path to an absolute path
					value = Modana.class.getClassLoader().getResource(value).toURI().getPath();
				}
				props.put(pluginDir, value);
			} else {
				String path = (String) p.get(key);
				//convert a relative path to an absolute path
				String value = Modana.class.getClassLoader().getResource(path).toURI().getPath();
				props.put(key, value);
			}
			if (logger.isDebugEnabled()) {
				logger.debug(key+"="+props.get(key));
			}
		}
	}
	
	/**
	 * @param key using the static string of ModanaProperty as key
	 * @return preprocessed properties
	 */
	public String get(String key) {
		return props.get(key);
	}
}
