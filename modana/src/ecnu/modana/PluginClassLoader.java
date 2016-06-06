package ecnu.modana;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * A class loader to dynamically add urls from which we load classes
 * @author cb
 */
class PluginClassLoader extends URLClassLoader {

	/**
	 * constructor
	 * @param urls base url paths
	 */
	public PluginClassLoader(URL[] urls) {
		super(urls);
	}

	/**
	 * add an new url path for finding classes
	 * @param url new url path
	 */
	@Override
	protected void addURL(URL url) {
		super.addURL(url);
	}	
	
}
