package plugin.test;

import org.apache.log4j.Logger;

import ecnu.modana.Modana;
import ecnu.modana.abstraction.IUserInterface;
import ecnu.modana.base.PluginMessage;

public class JavafxGui implements IUserInterface {
	
	private Logger logger = Logger.getRootLogger();


	@Override
	public void recvMsg(PluginMessage pMsg, Object[] data) {
	}

	@Override
	public String getName() {
		return "JavaFX GUI";
	}

	@Override
	public String getVersion() {
		return "1.0.0";
	}

	@Override
	public String getDescription() {
		return "...";
	}

	@Override
	public void launchWithParam(String[] paramValues) {
		if (logger.isDebugEnabled()) {
			logger.debug("launch JavaFX GUI!!!");
		}
		new MyFxFrame();
		Modana.getInstance().sendMsg("msg1", null);
	}

}
