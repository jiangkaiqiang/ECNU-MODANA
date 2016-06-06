package plugin.test;

import org.apache.log4j.Logger;

import ecnu.modana.Modana;
import ecnu.modana.abstraction.IPlugin;
import ecnu.modana.base.PluginMessage;

public class UppaalSimulator implements IPlugin {
	
	private Logger logger = Logger.getRootLogger();

	@Override
	public void recvMsg(PluginMessage pMsg, Object[] data) {
		if (pMsg.getName().equals("msg1")) {
			if (pMsg.checkMsgDatatypes(data)) {
				if (logger.isDebugEnabled()) {
					logger.debug("Received Message=msg1 from SwingGui !!!");
				}
				doSomething();
			} else {
				logger.error("msg1 data types errors!");
			}
		} else if (pMsg.getName().equals("msg3")) {
			if (pMsg.checkMsgDatatypes(data)) {
				if (logger.isDebugEnabled()) {
					logger.debug("Received Message=msg3 from JavafxGui !!!");
				}
				data[0] = "Show me!!!";
			} else {
				logger.error("msg3 data types errors!");
			}
		}
	}

	@Override
	public String getName() {
		return "Uppaal Simulator";
	}

	@Override
	public String getVersion() {
		return "1.0.0";
	}

	@Override
	public String getDescription() {
		return "...";
	}
	
	public void doSomething() {
		Object[] objs = new Object[2];
		objs[0] = new Integer(11);
		objs[1] = new String("new_data_string");
		if (logger.isDebugEnabled()) {
			logger.debug("UppaalSimulator sends data: "+objs[0]+", "+objs[1]);
		}
		Modana.getInstance().sendMsg("msg2", objs);
		if (logger.isDebugEnabled()) {
			logger.debug("UppaalSimulator gets returned data: "+objs[0]+", "+objs[1]);
		}
	}

}
