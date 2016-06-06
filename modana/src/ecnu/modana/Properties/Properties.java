package ecnu.modana.Properties;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * @author JKQ
 *
 * 2015年11月29日下午3:41:36
 */
public class Properties {
	private StringProperty properties;

	public StringProperty getProperties() {
		return properties;
	}

	public void setProperties(StringProperty properties) {
		this.properties = properties;
	}
	public Properties(String properties){
		
	    this.properties = new SimpleStringProperty(properties);
	}

}
