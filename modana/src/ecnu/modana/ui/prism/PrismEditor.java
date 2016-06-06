package ecnu.modana.ui.prism;
import java.util.HashMap;
import java.util.List;

import javafx.scene.Node;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import ecnu.modana.ui.MuiGArea;
import ecnu.modana.ui.MuiGEditor;
import ecnu.modana.ui.MuiGLink;
import ecnu.modana.ui.MuiGEditor.MOUSE_ACTION;

public class PrismEditor extends MuiGEditor {
	@Override
	public void addLibToggles(VBox graphLibPane) {
		ToggleButton tb0 = new ToggleButton("S");
		tb0.setMaxSize(30, 30);
		tb0.setFont(Font.font(10));
		tb0.setUserData(PrismState.class);
		graphLibPane.getChildren().add(tb0);
		ToggleButton tb1 = new ToggleButton("R");
		tb1.setMaxSize(30, 30);
		tb1.setFont(Font.font(10));
		tb1.setUserData(PrismTransition.class);
		graphLibPane.getChildren().add(tb1);
	}

	@Override
	public Paint getBackGroundColor() {
		return Color.WHITE;
	}
	private HashMap<String,Node>nodeMap=new HashMap<>();
	@Override
	public void AddMuiGArea(String... property)
	{
		PrismState prismState=new PrismState();
		prismState.name.setValue(property[0].substring(0, property[0].length()-property[1].length()));
		prismState.index.setValue(property[1]);
		Node tNode=prismState.createNode(Double.valueOf(property[2]),Double.valueOf(property[3]),Integer.valueOf(property[1]));
		tNode.setUserData(prismState);
		addMouseEvent2Area(tNode);
		getGraphContent().add(tNode);
		nodeMap.put(prismState.name.getValue()+prismState.index.getValue(),tNode);
	}

	@Override
	public void AddMuiGLink(String... property)
	{
		PrismState source=null,target=null;
		try
		{
			source=(PrismState)nodeMap.get(property[6].substring(0, property[6].length()-1)).getUserData();
			target=(PrismState)nodeMap.get(property[7].substring(0, property[7].length()-1)).getUserData();
			//System.err.println(source.toString()+" "+target.toString());
			PrismTransition prismTransition=new PrismTransition();
			prismTransition.syn.setValue(property[0]);
			prismTransition.condition.setValue(property[1]);
			prismTransition.prob.setValue(property[2]);
			prismTransition.assigns.setValue(property[3]);
			int i=Integer.valueOf(property[4]),j=Integer.valueOf(property[5]);
			prismTransition.setSource(i, source);
			prismTransition.setTarget(j, target);
			// final update of link location
			//finalUpdateLink(prismTransition, null);
			
			source.getAllLinks().add(prismTransition);
			target.getAllLinks().add(prismTransition);
//			System.err.println(source.name+source.index+" "+source.getAllLinks().size());
//			System.err.println(target.name+target.index+" "+target.getAllLinks().size());
			Node tNode=(Node)prismTransition.createNodes(source.getAllLinkPoints().get(i).getX(),source.getAllLinkPoints().get(i).getY(),target.getAllLinkPoints().get(j).getX(),target.getAllLinkPoints().get(j).getY());
			tNode.setUserData(prismTransition);
			prismTransition.getMouseSelectableArea().setUserData(prismTransition);
			MouseEvent2Link(prismTransition);
			getGraphContent().add(tNode);	
		} catch (Exception e)
		{
			if(source==null) System.err.println("not find userdate:"+property[6].substring(0, property[6].length()-1));
			if(target==null) System.err.println("not find userdate:"+property[7].substring(0, property[7].length()-1));
			System.err.println(nodeMap.keySet());
		}
	}
	public void FinalUpdate()
	{
		for (Node node : nodeMap.values())
		{
			PrismState gArea = (PrismState) node.getUserData();
			List<MuiGLink> list = gArea.getAllLinks();
			for (MuiGLink gLink : list)
			{
				if (gLink.getSourceArea() == gLink.getTargetArea())
				{
					// for self link node
					gLink.updateSelfLinkNodes(gLink.getSourcePoint().getX(),
							gLink.getSourcePoint().getY(), gLink
									.getTargetPoint().getX(), gLink
									.getTargetPoint().getY());
				} else if (gArea == gLink.getSourceArea())
				{
					gLink.updateNodes(gLink.getSourcePoint().getX(), gLink
							.getSourcePoint().getY(),
							MOUSE_ACTION.SOURCE_RELINK);
				} else if (gArea == gLink.getTargetArea())
				{
					gLink.updateNodes(gLink.getTargetPoint().getX(), gLink
							.getTargetPoint().getY(),
							MOUSE_ACTION.TARGET_RELINK);
				} else
				{
					System.err
							.println("GArea contains invalid reference to GLink!");
				}
			}
		}
	}

}
