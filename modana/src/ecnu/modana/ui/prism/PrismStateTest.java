package ecnu.modana.ui.prism;

import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ecnu.modana.ui.MuiGArea;
import ecnu.modana.ui.MuiGEditor.MOUSE_ACTION;

public class PrismStateTest extends MuiGArea {
	
	//saving created node
	Group graphicNode = null;
	
	//graphical components
	Rectangle rect = null;
	Text text = null;
	
	//saving all link points
	ArrayList<Point2D> linkPoints = null;
	
	//default rect width and height
	final static double width = 50, height = 50;
	
	//default text insets within the rectangle
	final static Insets textInsets = new Insets(19, 17, 14, 9);
	
	Stage propertyWindow = null;
	
	@Override
	public Stage getPropertyWindow() {
		if (propertyWindow == null) {		
			PrismPropWindow ppw = new PrismPropWindow();
			propertyWindow = ppw.getPropertyWindow();
			ppw.getTextField().setText(text.getText());
			ppw.getOkButton().setOnAction(new EventHandler<ActionEvent>() {		
				@Override
				public void handle(ActionEvent e) {
					//change data
					text.setText(ppw.getTextField().getText().trim());
					//update node representation
					updateNode(0, 0, MOUSE_ACTION.RELOCATE);
					propertyWindow.close();
				}
			});
		}
		return propertyWindow;
	}

	@Override
	public Node createNode(double x, double y,int index) {
		//draw rounded rectangle
		double corner = 0;
//		LinearGradient lg = new LinearGradient(0, 0, 1, 1, true, CycleMethod.REFLECT, 
//				new Stop(0.0, Color.WHITE), new Stop(1.0, Color.LIGHTBLUE));
		rect = new Rectangle(x, y, width, height);
		rect.setArcHeight(corner);
		rect.setArcWidth(corner);
		rect.setFill(Color.WHITE);
		rect.setStroke(Color.LIGHTBLUE);
		//draw text
		text = new Text(x+textInsets.getLeft(), y+textInsets.getTop(), "s0");
		text.setFont(Font.font(15));
		//autosize node to minimum
		autosizeNode(null, true);
		justifyCenterForText();
		
		Group g = new Group(rect, text);
		//set dropshadow effect
		setDefaultDropShadowEffect(g, true);
		
		g.setPickOnBounds(true); //important!!!
		graphicNode = g;
		return g;
	}

	@Override
	public void updateNode(double diffX, double diffY, MOUSE_ACTION action) {
		switch (action) {
		case NW_RESIZE:
			rect.setX(rect.getX()+diffX);
			rect.setWidth(rect.getWidth()-diffX);
			text.setX(text.getX()+diffX);
			rect.setY(rect.getY()+diffY);
			rect.setHeight(rect.getHeight()-diffY);
			text.setY(text.getY()+diffY);
			break;
		case SW_RESIZE:
			rect.setX(rect.getX()+diffX);
			rect.setWidth(rect.getWidth()-diffX);
			text.setX(text.getX()+diffX);
			rect.setHeight(rect.getHeight()+diffY);
			break;
		case W_RESIZE:
			rect.setX(rect.getX()+diffX);
			rect.setWidth(rect.getWidth()-diffX);
			text.setX(text.getX()+diffX);
			break;
		case NE_RESIZE:
			rect.setY(rect.getY()+diffY);
			rect.setHeight(rect.getHeight()-diffY);
			text.setY(text.getY()+diffY);
			rect.setWidth(rect.getWidth()+diffX);
			break;
		case SE_RESIZE:
			rect.setHeight(rect.getHeight()+diffY);
			rect.setWidth(rect.getWidth()+diffX);
			break;
		case E_RESIZE:
			rect.setWidth(rect.getWidth()+diffX);
			break;
		case N_RESIZE:
			rect.setY(rect.getY()+diffY);
			rect.setHeight(rect.getHeight()-diffY);
			text.setY(text.getY()+diffY);
			break;
		case S_RESIZE:
			rect.setHeight(rect.getHeight()+diffY);
			break;
		case RELOCATE:
			rect.setX(rect.getX()+diffX);
			rect.setY(rect.getY()+diffY);
			text.setX(text.getX()+diffX);
			text.setY(text.getY()+diffY);
			break;
		default:
			return;
		}
		//make sure that size is not smaller than its minimum value
		autosizeNode(null, false);
		justifyCenterForText();
		//disable link points
		linkPoints = null;
	}
	
	@Override
	public ArrayList<Point2D> getAllLinkPoints() {
		//if List is disabled as null, update it
		if (linkPoints == null) {
			Bounds bounds = graphicNode.getBoundsInParent();
			linkPoints = new ArrayList<Point2D>(4);
			linkPoints.add(new Point2D( 
					(bounds.getMaxX()+boundOffsetRB+bounds.getMinX()+boundOffsetLT) / 2, 
					bounds.getMinY() + boundOffsetLT ));
			linkPoints.add(new Point2D(
					bounds.getMaxX() + boundOffsetRB , 
					(bounds.getMaxY()+boundOffsetRB+bounds.getMinY()+boundOffsetLT) / 2 ));
			linkPoints.add(new Point2D( 
					(bounds.getMaxX()+boundOffsetRB+bounds.getMinX()+boundOffsetLT) / 2, 
					bounds.getMaxY() + boundOffsetRB ));
			linkPoints.add(new Point2D(
					bounds.getMinX() + boundOffsetLT, 
					(bounds.getMaxY()+boundOffsetRB+bounds.getMinY()+boundOffsetLT) / 2 ));
		}		 
		return linkPoints;
	}
	
	/**
	 * autosize the node with or without new string
	 * @param str optional new string (null represents using original string)
	 * @param minSize force to minimum size if true; change too small width or height to minimum value if false
	 */
	public void autosizeNode(String str, boolean minSize) {
		if (str != null) {
			text.setText(str);
		}
		double minWidth = text.getBoundsInLocal().getWidth()+textInsets.getRight();
		double minHeight = text.getBoundsInLocal().getHeight()+textInsets.getBottom();
		if (minSize) {
			rect.setWidth(minWidth);
			rect.setHeight(minHeight);
		} else {
			if (rect.getWidth() < minWidth) {
				rect.setWidth(minWidth);
			}
			if (rect.getHeight() < minHeight) {
				rect.setHeight(minHeight);
			}
		}
	}
	
	/**
	 * relocate the tex string to center of the rectangle
	 */
	private void justifyCenterForText() {
		double x = (rect.getWidth() - text.getBoundsInLocal().getWidth()) / 2;
		double y = (rect.getHeight() - text.getBoundsInLocal().getHeight()) / 2;
		text.setX(rect.getX()+x);
		text.setY(rect.getY()+y+13);
	}
	
}
