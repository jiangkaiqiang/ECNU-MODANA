package plugin.test;

import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.apache.log4j.Logger;

import ecnu.modana.Modana;
import ecnu.modana.abstraction.IUserInterface;
import ecnu.modana.base.PluginMessage;

public class SwingGui extends JFrame implements IUserInterface {

	private Logger logger = Logger.getRootLogger();
	
	private static final long serialVersionUID = 1L;

	public SwingGui() {
		JPanel panel = new JPanel();
        JTextArea textArea = new JTextArea();
        
        panel.setLayout(new GridLayout());
        textArea.setText("----------------test-----------------");
        
        panel.add(new JScrollPane(textArea));
        this.add(panel);
        
        this.setSize(600,400);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setLocationByPlatform(true);
        this.setVisible(true);
        
        Modana.getInstance().sendMsg("msg1", null);
	}
	
	@Override
	public void recvMsg(PluginMessage pMsg, Object[] data) {
	}

	@Override
	public String getName() {
		return "SwingGUI";
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
			logger.debug("launch swing GUI!!!");
		}
		new SwingGui();
	}
}
