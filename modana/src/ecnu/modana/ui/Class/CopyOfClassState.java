package ecnu.modana.ui.Class;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import com.sun.xml.internal.txw2.TXW;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ecnu.modana.ui.MuiGArea;
import ecnu.modana.ui.MuiGEditor.MOUSE_ACTION;

public class CopyOfClassState extends MuiGArea {

	// saving created node
	Group graphicNode = null;
	double offsetX=0,offsetY=0;
	ArrayList<Double>offX=new ArrayList<Double>();
	ArrayList<Double>offY=new ArrayList<Double>();
	double groupL,groupW;

	// graphical components
	Rectangle rect = null;
	Polyline polyline=null;
	Text text = null;
	Text text2 = null;

	// saving all link points
	ArrayList<Point2D> linkPoints = null;

	// default rect width and height
	final static double width = 80, height = 50;

	// default text insets within the rectangle
	final static Insets textInsets = new Insets(19, 17, 14, 9);

	Stage propertyWindow = null;

	@Override
	public Stage getPropertyWindow() {
		if (propertyWindow == null) {
			ClassPropWindow ppw = new ClassPropWindow();
			propertyWindow = ppw.getPropertyWindow();
			ppw.getTextField().setText(text.getText());
			ppw.getOkButton().setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					// change data
					text.setText(ppw.getTextField().getText().trim());
					// update node representation
					updateNode(0, 0, MOUSE_ACTION.RELOCATE);
					propertyWindow.close();
				}
			});
		}
		return propertyWindow;
	}

	@Override
	public Node createNode(double x, double y,int index) {
		// draw rounded rectangle
		LinearGradient lg = new LinearGradient(0, 0, 1, 1, true,
				CycleMethod.REFLECT, new Stop(0.0, Color.WHITE), new Stop(1.0,
						Color.LIGHTBLUE));
		rect = new Rectangle(0,0, width, height);
		rect.setFill(Color.TRANSPARENT);
		rect.setStroke(Color.RED);
        rect.setTranslateY(40);
        //rect.setTranslateX(10);
		// draw text
		text = new Text(textInsets.getLeft(), textInsets.getTop(),
				"Class1");
		text.setFont(Font.font(15));
		// autosize node to minimum
		autosizeNode(null, true);
		justifyCenterForText();

		//polyline=new Polyline(0,0,0,height*2,width,height*2,width,0,width/2,-height/2,0,0);
		polyline=new Polyline(0,0,0,height*2,width,height*2,width,0,0,0);
		polyline.setStroke(Color.BLACK);
		polyline.addEventFilter(KeyEvent.KEY_PRESSED,
				new EventHandler<KeyEvent>() {
					@Override
					public void handle(KeyEvent e) {
						System.err.println("key");
						//JOptionPane.showMessageDialog(null, "ah");
					}
				});
		Circle circle=new Circle(0,0,20);
		circle.setTranslateX(20);
		circle.setTranslateY(20);
		circle.setFill(Color.TRANSPARENT);
		circle.setStroke(Color.BLACK);
		TextField textField=new TextField("textField");
		//textField.setTranslateX(10);
		textField.setTranslateY(10);
		textField.setMaxWidth(width-2);
//        rect.setWidth(width);
//        System.err.println(rect.getBoundsInLocal().getWidth()+": "+polyline.getBoundsInLocal().getWidth()+": "+textField.getWidth());

//		rectangle.addEventFilter(MouseEvent.MOUSE_CLICKED,
//				new EventHandler<MouseEvent>() {
//					@Override
//					public void handle(MouseEvent e) {
//						if (e.getClickCount() == 3) 
//						{
//							System.err.println("ah");
//							JOptionPane.showMessageDialog(null, "ah");
//						}
//					}
//				});
		text2 = new Text("ah");
		text2.setX(10);
		text2.setY(80);
		Rectangle tRectangle=new Rectangle(textField.getTranslateX(),textField.getTranslateY(),textField.getWidth(),textField.getHeight());
		tRectangle.setFill(Color.TRANSPARENT);
		tRectangle.setStroke(Color.TRANSPARENT);
		Group g = new Group(textField, polyline,circle,rect,tRectangle);
		// set dropshadow effect
		setDefaultDropShadowEffect(g, true);
		g.setTranslateX(x);
		g.setTranslateY(y);
		offsetX=x;
		offsetY=y;
		for(Node tNode:g.getChildren())
		{
//			offX.add(tNode.getTranslateX());
//			offY.add(tNode.getTranslateY());
			offX.add(1.0);
			offY.add(1.0);
		}
		groupL=g.getBoundsInLocal().getWidth();
		groupW=g.getBoundsInLocal().getHeight();

		g.setPickOnBounds(true); // important!!!
		graphicNode = g;
		return g;
	}

	@Override
	public void updateNode(double diffX, double diffY, MOUSE_ACTION action) {
		double directX=1,directY=1;
		switch (action) {
		case NW_RESIZE:
//			rect.setX(rect.getX() + diffX);
//			rect.setWidth(rect.getWidth() - diffX);
//			text.setX(text.getX() + diffX);
//			rect.setY(rect.getY() + diffY);
//			rect.setHeight(rect.getHeight() - diffY);
//			text.setY(text.getY() + diffY);
//			directX=-1;
//			directY=-1;
			break;
		case SW_RESIZE:
//			rect.setX(rect.getX() + diffX);
//			rect.setWidth(rect.getWidth() - diffX);
//			text.setX(text.getX() + diffX);
//			rect.setHeight(rect.getHeight() + diffY);
			directY=-1;
			break;
		case W_RESIZE:
//			rect.setX(rect.getX() + diffX);
//			rect.setWidth(rect.getWidth() - diffX);
//			text.setX(text.getX() + diffX);
			directY=0;
			break;
		case NE_RESIZE:
//			rect.setY(rect.getY() + diffY);
//			rect.setHeight(rect.getHeight() - diffY);
//			text.setY(text.getY() + diffY);
//			rect.setWidth(rect.getWidth() + diffX);
			directX=-1;
			break;
		case SE_RESIZE:
//			rect.setHeight(rect.getHeight() + diffY);
//			rect.setWidth(rect.getWidth() + diffX);
			directX=-1;
			directY=-1;
			break;
		case E_RESIZE:
//			rect.setWidth(rect.getWidth() + diffX);
			directX=-1;
			directY=0;
			break;
		case N_RESIZE:
//			rect.setY(rect.getY() + diffY);
//			rect.setHeight(rect.getHeight() - diffY);
//			text.setY(text.getY() + diffY);
			directX=0;
			break;
		case S_RESIZE:
			//rect.setHeight(rect.getHeight() + diffY);
			directY=-1;
			directX=0;
			break;
		case RELOCATE:
//			rect.setX(rect.getX() + diffX);
//			rect.setY(rect.getY() + diffY);
//			text.setX(text.getX() + diffX);
//			text.setY(text.getY() + diffY);
//			polyline.setTranslateX(rect.getX() + diffX);
//			polyline.setTranslateY(rect.getY() + diffY);
			break;
		default:
			return;
		}
		if(action==MOUSE_ACTION.W_RESIZE||action==MOUSE_ACTION.SW_RESIZE||action==MOUSE_ACTION.NW_RESIZE)
			graphicNode.setTranslateX(offsetX+=diffX);
		if(action==MOUSE_ACTION.N_RESIZE||action==MOUSE_ACTION.NW_RESIZE||action==MOUSE_ACTION.NE_RESIZE)
			graphicNode.setTranslateY(offsetY+=diffY);
		if(action==MOUSE_ACTION.RELOCATE)
		{
			graphicNode.setTranslateX(offsetX+=diffX);
			graphicNode.setTranslateY(offsetY+=diffY);
		}
		if(action!=MOUSE_ACTION.RELOCATE)
		{
			int j=0;
			for(Node node:graphicNode.getChildren())
			{
				//rect.setTranslateX(graphicNode.getBoundsInLocal().getWidth()+diffX);
	//			graphicNode.setTranslateX(graphicNode.getBoundsInLocal().getWidth()/(graphicNode.getBoundsInLocal().getWidth()+diffX));
	//			graphicNode.setTranslateX(graphicNode.getBoundsInLocal().getHeight()/(graphicNode.getBoundsInLocal().getHeight()+diffX));
				//double sx=(graphicNode.getBoundsInParent().getWidth()-2)/(graphicNode.getBoundsInParent().getWidth()+directX*diffX-2);
				//double sy=(graphicNode.getBoundsInParent().getHeight()-2)/(graphicNode.getBoundsInParent().getHeight()+directY*diffY-2);
				double sx=groupL/(groupL+directX*diffX);
				double sy=groupW/(groupW+directY*diffY);
//				groupL+=directX*diffX;
//				groupW+=directY*diffY;
				node.setTranslateX(node.getTranslateX()*sx);
				node.setTranslateY(node.getTranslateY()*sy);
				System.err.println(directX*diffX);
				if(node instanceof Polyline)
				{
//					double sx=polyline.getBoundsInLocal().getWidth()/(polyline.getBoundsInLocal().getWidth()+directX*diffX);
//					double sy=polyline.getBoundsInLocal().getHeight()/(polyline.getBoundsInLocal().getHeight()+directY*diffY);
					//System.out.println(node.getScaleX());
//					node.setScaleX(sx);
//					node.setScaleY(sy);
//					offX.set(j, offX.get(j)*sx);
//					offY.set(j, offY.get(j)*sy);
					//System.err.println(node.getScaleX());
					ObservableList<Double> list= polyline.getPoints();
					for(int i=0;i<list.size();i+=2)
						list.set(i, list.get(i)*sx);
					for(int i=1;i<list.size();i+=2)
						list.set(i, list.get(i)*sy);
					//JOptionPane.showConfirmDialog(null, polyline.getBoundsInLocal().getWidth()+" "+sx);
				}
				else if(node instanceof Rectangle)
				{
					//System.err.println(node.getTranslateY()+": "+sy);
					//System.err.println(node.getBoundsInLocal().getWidth());
//					node.setScaleX(sx);
//					node.setScaleY(sy);
//					System.err.println(sx+": "+node.getBoundsInLocal().getWidth());
//					System.out.println(node.getBoundsInParent().getWidth());
					//System.exit(0);
					((Rectangle) node).setWidth(((Rectangle) node).getWidth()*sx);
					((Rectangle) node).setHeight(((Rectangle) node).getHeight()*sy);
				}
				if(node instanceof Circle)
				{
					if(diffX==diffY)
					{
						double radius=((Circle) node).getRadius();
						((Circle) node).setRadius(radius*sx);
					}
//					radius-=1;
//					node.setTranslateX(radius*sx);
//					node.setTranslateY(radius*sy);
				}
			}
		}
		groupL=graphicNode.getBoundsInParent().getWidth();
		groupW=graphicNode.getBoundsInParent().getHeight();
		// make sure that size is not smaller than its minimum value
		autosizeNode(null, false);
		justifyCenterForText();
		// disable link points
		linkPoints = null;
	}

	@Override
	public ArrayList<Point2D> getAllLinkPoints() {
		// if List is disabled as null, update it
		if (linkPoints == null) {
			Bounds bounds = graphicNode.getBoundsInParent();
			linkPoints = new ArrayList<Point2D>(4);
			linkPoints.add(new Point2D((bounds.getMaxX() + boundOffsetRB
					+ bounds.getMinX() + boundOffsetLT) / 2, bounds.getMinY()
					+ boundOffsetLT));
			linkPoints
					.add(new Point2D(bounds.getMaxX() + boundOffsetRB,
							(bounds.getMaxY() + boundOffsetRB
									+ bounds.getMinY() + boundOffsetLT) / 2));
			linkPoints.add(new Point2D((bounds.getMaxX() + boundOffsetRB
					+ bounds.getMinX() + boundOffsetLT) / 2, bounds.getMaxY()
					+ boundOffsetRB));
			linkPoints
					.add(new Point2D(bounds.getMinX() + boundOffsetLT,
							(bounds.getMaxY() + boundOffsetRB
									+ bounds.getMinY() + boundOffsetLT) / 2));
		}
		return linkPoints;
	}

	/**
	 * autosize the node with or without new string
	 * 
	 * @param str
	 *            optional new string (null represents using original string)
	 * @param minSize
	 *            force to minimum size if true; change too small width or
	 *            height to minimum value if false
	 */
	public void autosizeNode(String str, boolean minSize) {
		if (str != null) {
			text.setText(str);
		}
		double minWidth = text.getBoundsInLocal().getWidth()
				+ textInsets.getRight();
		double minHeight = text.getBoundsInLocal().getHeight()
				+ textInsets.getBottom();
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
	 * relocate the text string to center of the rectangle
	 */
	private void justifyCenterForText() {
		double x = (rect.getWidth() - text.getBoundsInLocal().getWidth()) / 2;
		double y = (rect.getHeight() - text.getBoundsInLocal().getHeight()) / 2;
		text.setX(rect.getX() + x);
		text.setY(rect.getY() + y + 13);
	}

}
