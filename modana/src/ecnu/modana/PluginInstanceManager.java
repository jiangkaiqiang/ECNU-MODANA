package ecnu.modana;

import java.util.HashMap;

import org.apache.log4j.Logger;

import ecnu.modana.abstraction.IPlugin;
import ecnu.modana.abstraction.IUserInterface;

/**
 * manage all instances of plugins (instantiating plugins)
 * @author cb
 */
class PluginInstanceManager {

	private Logger logger = Logger.getRootLogger();
	
	private HashMap<String, IPlugin> pluginInstanceMap;
	
	/**
	 * hold a plugin manager reference
	 */
	private PluginManager pluginManager;
	
	/**
	 * constructor
	 * @param pluginManager to get all plugin class object to check and  create plugin
	 */
	public PluginInstanceManager(PluginManager pluginManager) {
		super();
		this.pluginManager = pluginManager;
		pluginInstanceMap = new HashMap<String, IPlugin>();
	}
	
	/**
	 * instantiate and launch entry plugin (UI) with param flag
	 * @param params cmd line params (the 1st parameter is the identifier of entry)
	 */
	public void launchEntryPlugin(String[] params) {
		IUserInterface entryPlugin = null;
		try {
			if (params != null && params.length > 0) {
				//instantiate the entry plugin
				entryPlugin = (IUserInterface) pluginManager
						.getSpecifiedUserInterface(params[0]).newInstance();
				//save instance to pluginInstanceTable
				pluginInstanceMap.put(entryPlugin.getClass().getName(), entryPlugin);
				entryPlugin.launchWithParam(params);
			} else {
				logger.error("No entry flag is specified in cmd line!");
			}
		} catch (Exception e) {
			logger.error("Unknow error (Entry plugin instantiation failed) !", e);
		}
	}
	
	/**
	 * get plugin instance by class name
	 *  (creating a new instance or returning an existing instance)
	 * @param pluginClassName full class name
	 * @return IPlugin instance
	 */
	public IPlugin getPluginInstance(String pluginClassName) {
		IPlugin instance = pluginInstanceMap.get(pluginClassName);
		//check if the instance already exists
		if (null == instance) {
			//create an instance
			Class<IPlugin> cls = pluginManager.getSpecifiedPlugin(pluginClassName);
			if (null != cls) {
				try {
					instance = cls.newInstance();
					// add to pluginInstanceTable
					pluginInstanceMap.put(pluginClassName, instance);
				} catch (InstantiationException | IllegalAccessException e) {
					logger.error("Creating (" + pluginClassName + ") instance error!", e);
				}
			} else {
				logger.error("(" + pluginClassName + ") class not found!");
			}
		}
		return instance;
	}
	
	/**
	 * remove instance manually
	 * @param pluginClassName class name of plugin instance to be removed
	 */
	public void removeInstance(String pluginClassName) {
		if (pluginInstanceMap.get(pluginClassName) != null) {
			pluginInstanceMap.remove(pluginClassName);
		}
	}
	
	/**
	 * add instance manually
	 * @param pluginClassName class name of plugin instance to be added
	 * @param pluginObj plugin instance to be added
	 */
	public void addInstance(String pluginClassName, IPlugin pluginObj) {
		if (pluginInstanceMap.get(pluginClassName) == null) {
			if (pluginObj != null) {
				pluginInstanceMap.put(pluginClassName, pluginObj);
			} else {
				logger.error("Adding instance failed! New (" + pluginClassName + ") instance is null!");
			}
		}
	}
}
