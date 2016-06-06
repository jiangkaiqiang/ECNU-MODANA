package ecnu.modana.abstraction;

import javafx.scene.control.TreeItem;
import ecnu.modana.base.PluginMessage;

/**
 * Common interface for all plugins, 
 * including the abstract methods: recvMsg
 * for communicating with other plugins
 * @author cb
 */
public interface IPlugin {
	
	/**
	 * do something when receiving msg from another plugin
	 * (e.g. you can check the correctness of transferred data using pMsg.checkMsgDatatypes).
	 * Note that receiver can modify the data that can be obtained back by sender
	 * @param pMsg the sent plugin message
	 * @param data transferred data (be null when no data need to be transferred)
	 */
	public void recvMsg(PluginMessage pMsg, Object[] data);
	
	/**
	 * @return informative name of this plugin
	 */
	public String getName();
	
	/**
	 * @return version of this plugin (e.g. 1.0.0)
	 */
	public String getVersion();
	
	/**
	 * @return brief description of this plugin
	 */
	public String getDescription();
}
