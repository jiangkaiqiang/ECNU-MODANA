package ecnu.modana.ui;

import ecnu.modana.ui.MuiGEditor.MOUSE_ACTION;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.stage.Stage;

/**
 * Abstract graphical element for relation-like element
 * @author cb
 */
public abstract class MuiGLink {
	
	/**
	 * save reference of source end of link
	 */
	private MuiGArea source = null;
	
	/**
	 * save reference of target end of link
	 */
	private MuiGArea target = null;
	
	/**
	 * save index of link point on source end
	 */
	private int sourcePtIndex;
	
	/**
	 * save index of link point on target end
	 */
	private int targetPtIndex;
	
	/**
	 * get user-defined property window
	 * @return property window
	 */
	public abstract Stage getPropertyWindow();
	
	/**
	 * create a set of Javafx Nodes for temporary representation, Path is suggested when drawing a link
	 * @param x source x
	 * @param y source y
	 * @param toX target x
	 * @param toY target y
	 * @return created Javafx Node array
	 */
	public abstract Node createNodes(double x, double y, double toX, double toY);
	
	/**
	 * Effective after createNode() is invoked! - update the location of source point or target point
	 * @param x x of specified point
	 * @param y y of specified point
	 * @param action current mouse action: relink need to be dealt with for link
	 */
	public abstract void updateNodes(double x, double y, MOUSE_ACTION action);
	/**
	 * Effective after createNode() is invoked! - update the location of self link (target and source are the same)
	 * @param x x of source point
	 * @param y y of source point
	 * @param toX x of target point
	 * @param toY y of target point
	 */
	public abstract void updateSelfLinkNodes(double x, double y, double toX, double toY);
	
	/**
	 * Effective after createNode() is invoked! - get all or part of the created Node as mouse event listener
	 * @return listening Node
	 */
	public abstract Node getMouseSelectableArea();
	
	public abstract Node getLinkNode();
	
	/**
	 * hold the references of source in link
	 * @param sLink link point index of source (as the starting point of this link)
	 * @param source source graphic area
	 */
	public void setSource(int sLink, MuiGArea source) {
		sourcePtIndex = sLink;
		this.source = source;
	}
	
	/**
	 * hold the references of target in link
	 * @param tLink link point index of target (as the ending point of this link)
	 * @param target target graphic area
	 */
	public void setTarget(int tLink, MuiGArea target) {
		targetPtIndex = tLink;
		this.target = target;
	}
	
	/**
	 * get the source link point
	 * @return source link point
	 */
	public Point2D getSourcePoint() {
		return source.getAllLinkPoints().get(sourcePtIndex);
	}
	
	/**
	 * get the target link point
	 * @return target link point
	 */
	public Point2D getTargetPoint() {
		return target.getAllLinkPoints().get(targetPtIndex);
	}
	
	/**
	 * get the index of source link point
	 * @return point index
	 */
	public int getSourcePtIndex() {
		return sourcePtIndex;
	}
	
	/**
	 * get the index of target link point
	 * @return point index
	 */
	public int getTargetPtIndex() {
		return targetPtIndex;
	}
	
	/**
	 * get the source area reference
	 * @return source area
	 */
	public MuiGArea getSourceArea() {
		return source;
	}
	
	/**
	 * get the target area reference
	 * @return target area
	 */
	public MuiGArea getTargetArea() {
		return target;
	}
	
}
