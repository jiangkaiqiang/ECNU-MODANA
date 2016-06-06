package ecnu.modana.ui.Class;
import java.util.ArrayList;

import sun.net.www.content.text.plain;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;

public class Text
{
	Pane pane = new Pane();
	ChoiceBox<String> choiceBox=new ChoiceBox<>();
	TextArea textField=new TextArea();
	String []resStrings;
	public Text()
	{
		this.Text("reward","formula","label");
	}
	public void Text(String...str)
	{
		textField.setMaxWidth(400);
		textField.setMaxHeight(500);
		for(String tstr:str)
			choiceBox.getItems().add(tstr);
		if(str.length>0)
			choiceBox.getSelectionModel().select(0);
		resStrings=new String[str.length];
		choiceBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0,
					Number arg1, Number arg2)
			{
				//System.err.println(choiceBox.getItems().get(arg2.intValue()));
				try
				{
					//System.err.println(arg2.intValue());
					resStrings[arg1.intValue()]= textField.getText();
					//System.err.println(resStrings[arg2.intValue()]);
					textField.setText(resStrings[arg2.intValue()]);
				} catch (Exception e)
				{
					System.err.println(e.getMessage());
					System.err.println("cuo");
				}
			}
		});
		pane.setMaxWidth(1000);
		pane.setMaxHeight(400);
		textField.setTranslateX(100);
		pane.getChildren().addAll(choiceBox,textField);
	}
	public Pane GetPane()
	{
		return pane;
	}
	public String[] GetRes()
	{
		return resStrings;
	}
}
