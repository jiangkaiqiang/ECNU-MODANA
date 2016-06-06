package ecnu.modana.base;

import org.apache.log4j.Logger;

/**
 * plugin message information
 * @author cb
 */
public class PluginMessage {
	
	private Logger logger = Logger.getRootLogger();
	
	private String receiverClass;
	
	private String name;
	
	private String[] dataTypes;

	public PluginMessage(String receiverClass, String name, String[] dataTypes) {
		this.receiverClass = receiverClass;
		this.name = name;
		this.dataTypes = dataTypes;
	}
	
	/**
	 * get the type of plugin to receive this message
	 */
	public String getReceiverClass() {
		return receiverClass;
	}
	
	/**
	 * get the unique message name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * get the type of any transferred data by index
	 * (null value represents no transferred data or index out of bounds)
	 */
	public String getDataTypeByIndex(int index) {
		String retString = null;
		if (dataTypes != null) {
			try {
				retString = dataTypes[index];
			} catch (ArrayIndexOutOfBoundsException e) {
				logger.error("Plugin message datatype index out of bounds!", e);
			}
		}
		return retString;
	}
	
	/**
	 * check whether all transferred data conform to predefined types
	 * @param data transferred data to be checked
	 * @return checking result
	 */
	public boolean checkMsgDatatypes(Object[] data) {
		boolean b = false;
		if (data == null && dataTypes == null) {
			b = true;
		} else if (data != null && dataTypes != null) {
			if (data.length == dataTypes.length) {
				b = true;
				for (int i = 0; i < data.length; i++) {
					if (dataTypes[i] != null && data[i] != null) {
						if (! (dataTypes[i].equals(data[i].getClass().getCanonicalName())) ) {
							b = false; break;
						}
					} else {
						b = false; break;
					}
				}
			}
		}
		return b;
	}
	
}
