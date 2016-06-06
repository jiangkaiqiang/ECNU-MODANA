package ecnu.modana;

import java.io.File;
import java.io.IOException;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.log4j.Logger;

import ecnu.modana.abstraction.IPlugin;
import ecnu.modana.base.NonPlugin;
import ecnu.modana.util.IFileHandler;

/**
 * load class of plugin implementation for class and jar file
 * according to its superclass registered in PluginManager
 * @author cb
 */
class PluginClassHandler implements IFileHandler {

	private Logger logger = Logger.getRootLogger();
	
	/**
	 * save plugin classes information to pm
	 */
	private PluginManager pm;
	
	/**
	 * save plugin class loader to pcl
	 */
	private PluginClassLoader pcl;
	
	/**
	 * init PluginClassHandler
	 * @param pm used to save plugin classes information
	 */
	public PluginClassHandler(PluginManager pm, PluginClassLoader pcl) {
		super();
		this.pm = pm;
		this.pcl = pcl;
	}

	@Override
	public void handle(File target) {
		//handle only class and jar file
		if (target.getName().endsWith(".class")) { //handle class file
			handleClassFile(target);	
		} else if (target.getName().endsWith(".jar")) { // handle jar file	
			handleJarFile(target);	
		}
	}

	private void handleJarFile(File target) {
		JarFile jar = null;
		try {
			jar = new JarFile(target);
			//add new jar file to the class-loading paths
			pcl.addURL(target.toURI().toURL());
			Enumeration<JarEntry> entries = jar.entries();
			while (entries.hasMoreElements()) { //visit all entries in jar
				String entryName = entries.nextElement().getName();
				if (entryName.endsWith(".class")) { //find class file in jar
					String className = entryName.substring( //Here the path is separated by URI separator "/"
							0, entryName.lastIndexOf(".")).replace("/", ".");
					try {
						Class<?> cls = pcl.loadClass(className);
						//handle the loaded class
						handleLoadedClass(cls);
					} catch (ClassNotFoundException e) {
						logger.error("Class (" + className + ") is not found!", e);
					} catch (Exception e) {
						logger.error("Unknown exception!", e);
					}
				}
			}
		} catch (IOException e) {
			logger.error("Read jar file (" + target.getPath() + ") error!", e);
		} finally {
			if (null != jar) {
				try {
					jar.close();
				} catch (IOException e) {
					logger.error("Close jar file (" + target.getPath() + ") error!", e);
				}
			}
		}
	}

	private void handleClassFile(File target) {
		int preIndex = Modana.getModanaProperty(ModanaProperty.pluginDir).length();
		String path = target.toURI().getPath();
		String className = path.substring(preIndex, 
				path.lastIndexOf(".class")).replace("/", "."); //get class name
		URLClassLoader classLoader = null;
		try {
			Class<?> cls = pcl.loadClass(className);
			//handle the loaded class
			handleLoadedClass(cls);
		} catch (ClassNotFoundException e) {
			logger.error("Class (" + className + ") is not found!", e);
		} catch (Exception e) {
			logger.error("Unknown exception!", e);
		} finally {
			if (null != classLoader) {
				try {
					classLoader.close();
				} catch (IOException e) {
					logger.error("ClassLoader close error!", e);
				}
			}
		}
	}

	/**
	 * filter out irrelevant Class (we only handle classes implementing IPlugin)
	 * @param cls target class
	 */
	@SuppressWarnings("unchecked") //ignore the warning of Class<?>-to-Class<IPlugin> casting
	private void handleLoadedClass(Class<?> cls) {
		//find child class (not an interface) of IPlugin
		if (IPlugin.class.isAssignableFrom(cls) && !cls.isInterface()) {
			//do not handle the class marked with @NonPlugin
			if (!cls.isAnnotationPresent(NonPlugin.class)) {
				pm.addPluginClass((Class<IPlugin>) cls); //add to plugin manager
			}
		}
	}
}
