package plugin.test;

import org.apache.log4j.Logger;

import ecnu.modana.abstraction.IPlugin;
import ecnu.modana.base.PluginMessage;

public class SMChecker implements IPlugin {

	private Logger logger = Logger.getRootLogger();
	
	@Override
	public void recvMsg(PluginMessage pMsg, Object[] data) {
		if (pMsg.getName().equals("msg2")) {
			if (pMsg.checkMsgDatatypes(data)) {
				if (logger.isDebugEnabled()) {
					logger.debug("Received Message=msg2 from UppaalSimulator !!!");
					logger.debug("get msg2 data: " + data[0] + ", " + data[1]);
				}
				// modify the data
				data[0] = 22;
				data[1] = "modified_data_string";
			} else {
				logger.error("msg2 data types errors!");
			}
		}
	}

	@Override
	public String getName() {
		return "Statsitical Model Checker";
	}

	@Override
	public String getVersion() {
		return "1.0.0";
	}

	@Override
	public String getDescription() {
		return "...";
	}

}
