package ecnu.modana.ui;

import java.util.ArrayList;
import java.util.LinkedList;

import ecnu.modana.ui.MuiGEditor.MOUSE_ACTION;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Abstract graphical element for entity-like element
 * @author cb
 */
public abstract class MuiGArea {
	public StringProperty name=new SimpleStringProperty("muiGArea");
	public StringProperty index=new SimpleStringProperty("0");
	/**
	 * left and top offset of the true bound (without dropshadow)
	 */
	public static double boundOffsetLT = MuiGEditor.dropshadowOffsetX * 2;
	
	/**
	 * right and bottom offset of the true bound (without dropshadow)
	 */
	public static double boundOffsetRB = -MuiGEditor.dropshadowOffsetY * 4;
	
	/**
	 * save all links on this area
	 */
	protected LinkedList<MuiGLink> glinkList = new LinkedList<MuiGLink>();
	
	/**
	 * get user-defined property window
	 * @return property window
	 */
	public abstract Stage getPropertyWindow();
	
	/**
	 * create a new Javafx Node for representation, usually Shape or Group
	 * @param x x of the left top corner
	 * @param y y of the left top corner
	 * @return created Javafx Node
	 */
	public abstract Node createNode(double x, double y,int index);
	
	/**
	 * Effective after createNode() is invoked! - update location or size or both of the Node, i.e. relocate+resize
	 * @param diffX difference value of x of the left top corner (X-lastX)
	 * @param diffY difference value of y of the left top corner (Y-lastY)
	 * @param action current mouse action: relocate, resize need to be dealt with for area
	 */
	public abstract void updateNode(double diffX, double diffY, MOUSE_ACTION action);
	
	/**
	 * Effective after createNode() is invoked! - get the list available link points defined by concrete graphic class 
	 * (link points should depend on the location and size of the created Node)
	 */
	public abstract ArrayList<Point2D> getAllLinkPoints();
	
	/**
	 * add new link to list
	 * @param gLink new link to be added
	 */
	public void addLinkOnArea(MuiGLink gLink) {
		glinkList.add(gLink);
	}
	
	/**
	 * remove link from list
	 * @param gLink link to be removed
	 */
	public void removeLinkOnArea(MuiGLink gLink) {
		glinkList.remove(gLink);
	}
	
	/**
	 * get the list containing all links
	 * @return list of links
	 */
	public LinkedList<MuiGLink> getAllLinks() {
		return glinkList;
	}
	
	
	/**
	 * set default dropshadow effect to this Node
	 */
	public static void setDefaultDropShadowEffect(Node node, boolean shadowed) {
		if (shadowed) {
			DropShadow ds = new DropShadow();
			ds.setOffsetY(MuiGEditor.dropshadowOffsetY);
			ds.setOffsetX(MuiGEditor.dropshadowOffsetX);
			ds.setColor(Color.GRAY);
			node.setEffect(ds);
		} else {
			node.setEffect(null);
		}
	}
}
