package ecnu.modana;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import ecnu.modana.abstraction.IPlugin;
import ecnu.modana.util.DirVisitor;
import ecnu.modana.util.XMLHandler;

/**
 * manage all implementation classes of IPlugin
 * @author cb
 */
class PluginManager {

	private Logger logger = Logger.getRootLogger();
	
	/**
	 * save all valid plugin implementation classes
	 */
	private HashMap<String, Class<IPlugin>> pluginClsMap;
	
	/**
	 * save entry class and parameter string of each entry point
	 */
	private HashMap<String, String> entryPointMap;

	public PluginManager() {
		super();
		this.pluginClsMap = new HashMap<String, Class<IPlugin>>();
		this.entryPointMap = new HashMap<String, String>();
	}
	
	/**
	 * add class to plugin manager
	 * @param cls class to be added
	 */
	public void addPluginClass(Class<IPlugin> cls) {
		if (pluginClsMap.get(cls.getName()) == null) {
			pluginClsMap.put(cls.getName(), cls);
		}
	}
	
	/**
	 * load plugins from plugin directory
	 */
	public void loadPlugins(PluginClassLoader pcl) {
		try {
			//read plugin directory path
			String path = Modana.getModanaProperty(ModanaProperty.pluginDir);
			//invoke DirVisitor to visit and load all valid classes
			new DirVisitor(new PluginClassHandler(this, pcl)).visitAll(path, true);
		} catch (Exception e) {
			logger.error("Unknown error!", e);
		}
	}
	
	/**
	 * load all registered entry plugin flag from plugin-cfg.xml
	 * @return whether successfully loading
	 */
	public boolean loadEntryPluginFlag() {
		boolean b = true;
		try {
        	//read xml path
    		String path = Modana.getModanaProperty(ModanaProperty.pluginCfgFile);
    		//read xsd path
    		String xsdPath = Modana.getModanaProperty(ModanaProperty.pluginCfgXsdFile);
    		
			XMLHandler handler = new XMLHandler(path);
			//validate xml file
			b = handler.validateByXSD(xsdPath);
			if (b) { //read xml file if it is valid
				SAXReader reader = new SAXReader();
				Document doc = reader.read(path);
				Element rootElement = doc.getRootElement();
				 //get all entry plugin
				Iterator<?> it = rootElement.elementIterator("entry-plugin");
				while (it.hasNext()) {
					Element entryElement = (Element) it.next();
					String flag = entryElement.element("entry-flag").getTextTrim(); //get entry flag
					String clazz = entryElement.element("entry-class").getTextTrim(); //get entry class
					
					if (entryPointMap.get(flag) == null) { //check if there exists duplicate flag
						entryPointMap.put(flag, clazz);
					} else {
						logger.warn("There exist duplicate entry flags!");
						b = false;
					}
					
				}
			}
		}  catch (DocumentException e) {
			logger.error("Read document error!", e);
			b = false;
		} catch (Exception e) {
			logger.error("Unknown error!", e);
			b = false;
		}
		return b;
	}
	
	/**
	 * find specified IUserInterface with flag parameter
	 * @param paramFlag flag parameter (the 1st cmd line parameter)
	 * @return class of IUserInterface plugin (null if the flag is not found in map)
	 */
	public Class<IPlugin> getSpecifiedUserInterface(String paramFlag) {
		return pluginClsMap.get(entryPointMap.get(paramFlag));
	}
	
	/**
	 * find specified IPlugin with plugin class name
	 * @param pluginClsName plugin class name
	 * @return class of plugin
	 */
	public Class<IPlugin> getSpecifiedPlugin(String pluginClsName) {
		return pluginClsMap.get(pluginClsName);
	}
	
	/**
	 * return all plugin class name strings
	 * @return the set of plugin class name strings
	 */
	public Set<String> getAllPluginClasses() {
		return pluginClsMap.keySet();
	}
}
