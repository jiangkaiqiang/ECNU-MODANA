package ecnu.modana;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectStreamException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import ecnu.modana.abstraction.IPlugin;
import ecnu.modana.base.PluginMessage;

/**
 * Modana plugin framework
 * @author cb
 */
public class Modana{
	
	/**
	 * Single instance of Modana
	 */
	private static volatile Modana INSTANCE;
	
	private final String logPropertiesFile = "log4j.properties";
	private final String modanaPropertiesFile = "modana.properties";
	
	/**
	 * Properties read from modanaPropertiesFile
	 */
	private static ModanaProperty modanaProps;
	
	private PluginClassLoader pluginClassLoader;
	private PluginManager pluginManager;
	private MessageManager messageManager;
	private PluginInstanceManager pluginInstanceManager;
	
	/**
	 * get single instance of Modana
	 * @return single instance
	 */
	public static Modana getInstance() {
		if (INSTANCE == null) {
			synchronized (Modana.class) {
				if (INSTANCE == null) {
					INSTANCE = new Modana();
					return INSTANCE;
				}
			}
		}
		return INSTANCE;
	}
	
	//hook this method for 'deserialization'
	private Object readResolve() throws ObjectStreamException {
        return INSTANCE;  
    }
	
	/**
	 * private constructor of Modana plugin framework, for 'singleton'
	 */
	private Modana() {
		// MUST init log4j first for debugging the other part of the project
		PropertyConfigurator.configure(Modana.class.getClassLoader()
				.getResourceAsStream(logPropertiesFile));
		modanaConfigInit();
		initPluginClassLoader();
		initPluginFramework();
	}	
	
	/**
	 * configure Modana
	 */
	private void modanaConfigInit() {
		Logger logger = Logger.getRootLogger();
		Properties props = new Properties();
		try {
			InputStreamReader isr = new InputStreamReader(
				Modana.class.getClassLoader().getResourceAsStream(modanaPropertiesFile));
			props.load(isr);
			modanaProps = new ModanaProperty(props); // read Properties to ModanaProperty
		} catch (FileNotFoundException e) {
			logger.error("Modana properties file not found!", e);
		} catch (IOException e) {
			logger.error("Modana properties file read IO error!", e);
		} catch (NullPointerException e) {
			logger.error("Modana properties preprocessing error!", e);
		} catch (Exception e) {
			logger.error("Unknow error!", e);
		}
	}
	
	/**
	 * initialize user-defined class loader
	 */
	private void initPluginClassLoader() {
		URL[] urls = new URL[1];
		try {
			urls[0] = new File(modanaProps.get(ModanaProperty.pluginDir)).toURI().toURL();
			pluginClassLoader = new PluginClassLoader(urls);
		} catch (MalformedURLException e) {
			Logger.getRootLogger().error("Malformed URL error in initializing plugin class loader!", e);
		}
	}
	
	/**
	 * initialize plugin framework
	 */
	private void initPluginFramework() {
		//first : plugin manager
		pluginManager = new PluginManager();
		pluginManager.loadPlugins(pluginClassLoader);
		pluginManager.loadEntryPluginFlag();
		//second : plugin instance manager
		pluginInstanceManager = new PluginInstanceManager(pluginManager);
		//third : message manager
		messageManager = new MessageManager(pluginManager);
		messageManager.loadMsgs();
		messageManager.checkMsgDependency();
	}

	/**
	 * get Modana property by key
	 * @param key property key
	 * @return property value
	 */
	public static String getModanaProperty(String key) {
		return modanaProps.get(key);
	}
	
	/**
	 * send specified message with data
	 * @param msg message name
	 * @param data transferred data
	 */
	public void sendMsg(String msg, Object[] data) {
		PluginMessage pm = messageManager.getMessageByName(msg);
		if (pm != null) {
			IPlugin plugin = pluginInstanceManager
					.getPluginInstance(pm.getReceiverClass());
			if (plugin != null) {
				// send msg using existing plugin instance
				plugin.recvMsg(pm, data);
			} else {
				Logger.getRootLogger().error("Message sending error! Receiver not found!");
			}
		} else {
			Logger.getRootLogger().error("Message sending error! Message not found!");
		}
	}
	
	/**
	 * get all specified plugin instances (also instances of E referred to as extension points)
	 * @param cls interface of extension point
	 * @return list of instances of E
	 */
	@SuppressWarnings("unchecked") //ignore the warning of IPlugin-to-E casting
	public <E extends IPlugin> ArrayList<E> getPluginInstancesByType(Class<E> cls) {
		ArrayList<E> list = new ArrayList<E>();
		Iterator<String> it = pluginManager.getAllPluginClasses().iterator();
		while (it.hasNext()) {
			String str = (String) it.next();	
			if (cls.isAssignableFrom(pluginManager.getSpecifiedPlugin(str))) {
				list.add((E)pluginInstanceManager.getPluginInstance(str));
			}
		}
		return list;
	}
	
	/**
	 * Step 1: remove existing instance if possible;
	 * Step 2: add new instance if plugin is not null.
	 * @param plugin plugin instance to be added
	 */
	public void addOrReplacePluginInstance(IPlugin plugin) {
		pluginInstanceManager.removeInstance(plugin.getClass().getCanonicalName());
		pluginInstanceManager.addInstance(plugin.getClass().getCanonicalName(), plugin);
	}
	
	/**
	 * return all plugin class name strings
	 * @return the set of plugin class name strings
	 */
	public Set<String> getAllPluginClasses() {
		return pluginManager.getAllPluginClasses();
	}
	
	/**
	 * manually register new message and check its receiver dependency
	 * @param msgName new message name
	 * @param receiverName receiver class name of the message
	 * @param datatypes transferred data types
	 */
	public void addNewMsg(String msgName, String receiverName, String[] datatypes) {
		messageManager.addNewMsg(msgName, receiverName, datatypes);
	}
	
	/**
	 * get message by name
	 * @param msgName message name
	 * @return message object
	 */
	public PluginMessage getMessageByName(String msgName) {
		return messageManager.getMessageByName(msgName);
	}
	
	/**
	 * get all plugin messages
	 * @return Collection of messages
	 */
	public Collection<PluginMessage> getAllMessages() {
		return messageManager.getAllMessages();
	}
	
	/**
	 * instantiate and launch entry plugin (UI) with param flag
	 * @param params cmd line params (the 1st parameter is the identifier of entry)
	 */
	public void launchEntryPlugin(String[] params) {
		pluginInstanceManager.launchEntryPlugin(params);
	}
	
	//////////
	///Main///
	//////////
	public static void main(String[] args) {
		// launch UI to start
		Modana.getInstance().launchEntryPlugin(args);
	}
	
}
