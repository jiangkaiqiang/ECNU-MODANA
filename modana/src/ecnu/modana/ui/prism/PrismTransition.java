package ecnu.modana.ui.prism;

import ecnu.modana.ui.MuiGEditor.MOUSE_ACTION;
import ecnu.modana.ui.MuiGLink;
import ecnu.modana.util.PropWindow;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcTo;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class PrismTransition extends MuiGLink {
	
	//saving created node
	Group graphicNode = null;
	
	//graphical components
	MoveTo moveTo = null;
	LineTo lineTo = null;
	LineTo lineToA = null;
	MoveTo moveToB = null;
	LineTo lineToC = null;
	Path path = null;
	Text text = null;
	
	//for self link components (part of path)
	ArcTo arcTo = null;
	LineTo arctoLineTo = null;
	
	//record two end points
	double sourceX, sourceY, targetX, targetY;
	public StringProperty syn=new SimpleStringProperty(),condition=new SimpleStringProperty();
	public StringProperty prob=new SimpleStringProperty(),assigns=new SimpleStringProperty();
	
	Stage propertyWindow = null;
	
	public PrismTransition()
	{
//		syn.setValue("syn");
//		condition.setValue("condition");
//		prob.setValue("prob");
//		assigns.setValue("assigns");
		syn.setValue("");
		condition.setValue("");
		prob.setValue("");
		assigns.setValue("");
	}

	@Override
	public Stage getPropertyWindow() {
//		if (propertyWindow == null) {		
//			PrismPropWindow ppw = new PrismPropWindow();
//			propertyWindow = ppw.getPropertyWindow();
//			ppw.getTextField().setText(text.getText());
//			ppw.getOkButton().setOnAction(new EventHandler<ActionEvent>() {		
//				@Override
//				public void handle(ActionEvent e) {
//					//change data
//					text.setText(ppw.getTextField().getText().trim());
//					propertyWindow.close();
//				}
//			});
//		}
		if (propertyWindow == null) 
		{		
			PropWindow ppw = new PropWindow("syn:","condition:","prob:","assign:");
			propertyWindow = ppw.getPropertyWindow();
			//ppw.getTextField().setText(text.getText());
			ppw.getOkButton().setOnAction(new EventHandler<ActionEvent>() {		
				@Override
				public void handle(ActionEvent e) {
					//change data
					text.setText(GetStr());
					propertyWindow.close();
				}
			});
			int i=0;
			ppw.tfList.get(i++).textProperty().bindBidirectional(syn);
			ppw.tfList.get(i++).textProperty().bindBidirectional(condition);
			ppw.tfList.get(i++).textProperty().bindBidirectional(prob);
			ppw.tfList.get(i++).textProperty().bindBidirectional(assigns);
		}
		return propertyWindow;
	}
	@Override
	public Node createNodes(double x, double y, double toX, double toY) {
		//record two end points
		sourceX = x;
		sourceY = y;
		targetX = x;
		targetY = y;
		//draw line from (x, y) to (toX, toY)
		moveTo = new MoveTo(x, y);
		double newX = getReducedArrowHeadPoint(x, toX);
		double newY = getReducedArrowHeadPoint(y, toY);
		lineTo = new LineTo(newX, newY);
		//draw end arrow
		double endArrow[] = computeEndArrowXY(x, y, newX, newY);
		lineToA = new LineTo(endArrow[0], endArrow[1]);
		moveToB = new MoveTo(newX, newY);
		lineToC = new LineTo(endArrow[2], endArrow[3]);
		
		path = new Path(moveTo, lineTo, lineToA, moveToB, lineToC);
		path.setStroke(Color.LIGHTBLUE);
		path.setStrokeWidth(3);
		//draw text
		text = new Text((x+toX)/2, (y+toY)/2, GetStr());
		text.setFont(Font.font(15));
		
		Group g = new Group(path, text);
		
		g.setPickOnBounds(false); //important!!!
		graphicNode = g;
		return g;
	}

	@Override
	public void updateNodes(double x, double y, MOUSE_ACTION action) {
		if (action == MOUSE_ACTION.SOURCE_RELINK) { //move source point
			sourceX = x;
			sourceY = y;
			
		} else if (action == MOUSE_ACTION.TARGET_RELINK) { //move target point
			targetX = x;
			targetY = y;
		} else {
			return;
		}
		if (arcTo != null) {
			if (action == MOUSE_ACTION.SOURCE_RELINK) {
				targetX = getTargetPoint().getX();
				targetY = getTargetPoint().getY();
			} else if (action == MOUSE_ACTION.TARGET_RELINK) {
				sourceX = getSourcePoint().getX();
				sourceY = getSourcePoint().getY();
			}
			path.getElements().removeAll(arcTo, arctoLineTo);
			arcTo = null;
			arctoLineTo = null;
			path.getElements().addAll(lineTo, lineToA, moveToB, lineToC);
		}
		//move end arrow
		double newX = getReducedArrowHeadPoint(sourceX, targetX);
		double newY = getReducedArrowHeadPoint(sourceY, targetY);
		moveTo.setX(sourceX);
		moveTo.setY(sourceY);
		lineTo.setX(newX);
		lineTo.setY(newY);
		
		double endArrow[] = computeEndArrowXY(sourceX, sourceY, newX, newY);
		lineToA.setX(endArrow[0]);
		lineToA.setY(endArrow[1]);
		moveToB.setX(newX);
		moveToB.setY(newY);
		lineToC.setX(endArrow[2]);
		lineToC.setY(endArrow[3]);
		
		//move text
		text.setX((sourceX + targetX)/2 - 15);
		text.setY((sourceY + targetY)/2);
		
	}
	

	@Override
	public void updateSelfLinkNodes(double x, double y, double toX, double toY) {
		moveTo.setX(x);
		moveTo.setY(y);
		path.getElements().remove(1, path.getElements().size());
		arcTo = new ArcTo();
		arcTo.setX(toX);
		arcTo.setY(toY);
		//compute distance
		double r = Math.sqrt((x-toX)*(x-toX) + (y-toY)*(y-toY)) * 0.65;
		arcTo.setRadiusX(r);
		arcTo.setRadiusY(r);
		arcTo.setLargeArcFlag(true);
		arcTo.setSweepFlag(true);
		double arrowX = toX - Math.signum(x-toX)*6.5;
		double arrowY = toY - Math.signum(y-toY)*6.5;
		arctoLineTo = new LineTo(arrowX, arrowY);
		path.getElements().addAll(arcTo, arctoLineTo);
		
		//move text
		double mx = (x + toX) / 2;
		double my = (y + toY) / 2;
		double dx = Math.signum(x-toX);
		double dy = Math.signum(y-toY);
		if ( (dx>0 && dy>0) || (dx<0 && dy<0) ) {
			dx = -dx;
		} else {
			dy = -dy;
		}
		text.setX(mx + dx*r*1.2 - 15);
		text.setY(my + dy*r*1.2);
	}

	@Override
	public Node getMouseSelectableArea() {
		return graphicNode;
	}

	//get an array containing coordinates of end arrow
	private double[] computeEndArrowXY(double x0, double y0, double x1, double y1) {
		double distance = Math.sqrt((x0-x1)*(x0-x1) + (y0-y1)*(y0-y1));
		int arrowLen = 12;		
		double xa = x1 + arrowLen * ((x0 - x1) + (y0 - y1) / 2) / distance;
		double ya = y1 + arrowLen * ((y0 - y1) - (x0 - x1) / 2) / distance;
		double xb = x1 + arrowLen * ((x0 - x1) - (y0 - y1) / 2) / distance;
		double yb = y1 + arrowLen * ((y0 - y1) + (x0 - x1) / 2) / distance;
		double a[] = {xa, ya, xb, yb};
		return a;
	}
	
	private double getReducedArrowHeadPoint(double a0, double a1) {
		return a1 + Math.signum(a0-a1)*3;
	}

	@Override
	public Node getLinkNode()
	{
		return graphicNode;
	}
	
	private String GetStr()
	{
		int geShu=0;
		String str=syn.getValue().trim();
		if(""!=str) geShu++;
		String tstr=condition.getValue().trim();
		if(""!=tstr)
		{
			if(geShu!=0)
				str+=":";
			str+=tstr;
			geShu++;
		}
		tstr=prob.getValue().trim();
		if(""!=tstr)
		{
			if(geShu!=0)
				str+=":";
			str+=tstr;
			geShu++;
		}
		tstr=assigns.getValue().trim();
		if(""!=tstr)
		{
			if(geShu!=0)
				str+=":";
			str+=tstr;
			geShu++;
		}
		return str;
	}
}
