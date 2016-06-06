package ecnu.modana.util;

import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * General property window
 * @author cb
 */
public class PropWindow{
	
	//window and and its components
	Stage propertyWindow = null;	
	List<Label> lableList;
	public List<TextField> tfList;
	Button okBtn = null;	
	Button cancelBtn = null;
	
	/**
	 * create pre-defined property window
	 * (initial valude of text field and action of ok button should be user-defined)
	 */
	public PropWindow(String... propertyName) {
		propertyWindow = new Stage(StageStyle.TRANSPARENT);
		propertyWindow.initModality(Modality.WINDOW_MODAL);
		propertyWindow.setOpacity(0.87);
		lableList=new ArrayList<Label>();
		tfList =new ArrayList<TextField>();
		HBox[] hBoxs=new HBox[propertyName.length+1];
		VBox vBox;
		for(int i=0,j=propertyName.length;i<j;i++)
		{
			Label label=new Label();
			label.setText(propertyName[i]);
			lableList.add(label);
			TextField tf=new TextField();
			tf.setMinHeight(30);
			tfList.add(tf);
			HBox hBox= new HBox(20,label,tf);
			hBoxs[i]=hBox;
		}
		//initial value of text field should be user-defined 
		okBtn = new Button("OK");
		//action for okBtn should be user-defined
		okBtn.setMinWidth(70);
		okBtn.setMinHeight(30);
		cancelBtn = new Button("Cancel");
		cancelBtn.setOnAction(new EventHandler<ActionEvent>() {		
			@Override
			public void handle(ActionEvent e) {
				propertyWindow.close();
			}
		});
		cancelBtn.setMinWidth(70);
		cancelBtn.setMinHeight(30);
		HBox hBox = new HBox(25, okBtn, cancelBtn);
		hBoxs[hBoxs.length-1]=hBox;
		hBox.setPadding(new Insets(0, 20, 0, 20));
//		VBox vBox = new VBox(15, tf, hBox);
		vBox=new VBox(15,hBoxs);
		vBox.setPadding(new Insets(40, 10, 20, 10));
        Scene scene = new Scene(vBox);
        scene.setFill(Color.TRANSPARENT);
        //draw special window
        vBox.setBackground(new Background(
        	new BackgroundFill(
        	new LinearGradient(1, 1, 1, 0, true, CycleMethod.REFLECT, 
        		new Stop(0.0, Color.LIGHTBLUE), new Stop(1.0, Color.WHITE)), 
    		new CornerRadii(15), 
    		Insets.EMPTY)));
        propertyWindow.setScene(scene);
	}

	public Stage getPropertyWindow() {
		return propertyWindow;
	}

	public Button getOkButton() {
		return okBtn;
	}

	public Button getCancelButton() {
		return cancelBtn;
	}
}
