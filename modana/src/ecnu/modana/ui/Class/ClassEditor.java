package ecnu.modana.ui.Class;

import javafx.scene.control.ToggleButton;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import ecnu.modana.ui.MuiGEditor;

public class ClassEditor extends MuiGEditor {

	@Override
	public void addLibToggles(VBox graphLibPane) {
		ToggleButton tb0 = new ToggleButton("C");
		tb0.setMaxSize(30, 30);
		tb0.setFont(Font.font(10));
		tb0.setUserData(ClassState.class);
		graphLibPane.getChildren().add(tb0);
		ToggleButton tb1 = new ToggleButton("R");
		tb1.setMaxSize(30, 30);
		tb1.setFont(Font.font(10));
		tb1.setUserData(ClassTransition.class);
		graphLibPane.getChildren().add(tb1);
	}

	@Override
	public Paint getBackGroundColor() {
		return Color.WHITE;
	}

	@Override
	public void AddMuiGArea(String... property)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void AddMuiGLink(String... property)
	{
		// TODO Auto-generated method stub
		
	}

}
