package ecnu.modana.ui;

import javafx.scene.control.Tab;
import javafx.scene.control.TreeItem;

public class MyTreeItem extends TreeItem <String> {
	private Tab myTab;

	public Tab getMyTab() {
		return myTab;
	}

	public void setMyTab(Tab myTab) {
		this.myTab = myTab;
	}
	

}
