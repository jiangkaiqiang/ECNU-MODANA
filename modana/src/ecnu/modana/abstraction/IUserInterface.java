package ecnu.modana.abstraction;

/**
 * Extension Interface for UI (graphical UI as well as command line UI)
 * @author cb
 */
public interface IUserInterface extends IPlugin {
	
	/**
	 * launch the UI with cmd param value string
	 * @param paramValues cmd line params
	 */
	public void launchWithParam(String[] paramValues);
}
