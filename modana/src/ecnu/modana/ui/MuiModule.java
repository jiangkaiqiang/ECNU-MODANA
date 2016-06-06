package ecnu.modana.ui;

import ecnu.modana.abstraction.IPlugin;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

/**
 * A user-defined module of ModanaUI, 
 * defining your modeling&analysis related UI as a whole
 * (including menu, editor and treeview)
 * @author cb
 */
public interface MuiModule extends IPlugin {
	/**
	 * add the returned menuBar to a main menu
	 * (will be shown all the time)
	 * @return menu to be added
	 */
	public abstract Menu getMainMenu();
	
	/**
	 * add the returned menuBar as a module menu 
	 * (will be shown only when this module editor is active)
	 * @return menu to be added
	 */
	public abstract Menu getModuleMenu();
	
	/**
	 * create a new tree item for showing new model 
	 * (return different instance every time)
	 * @return tree item
	 */
	public abstract TreeItem<String> createTreeModelExplorer(String modelName);
	/**
	 * create a new user-defined graphical editor in a tab 
	 * (return different instance every time)
	 * @return graphical editor (MuiGEditor)
	 */
	public abstract MuiGEditor createGaphicalEditor();
	
	public ContextMenu modelContextMenu=new ContextMenu();
	public ContextMenu diagramContextModel=new ContextMenu();
	public abstract void AddModelContextMenu();
	public abstract void AddDiagramContextMenu();	
}
