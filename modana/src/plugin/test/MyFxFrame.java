package plugin.test;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

import ecnu.modana.Modana;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class MyFxFrame extends JFrame{
	
	private static final long serialVersionUID = 1L;
	
	Canvas canvas = null;
	GraphicsContext gc = null;
    double x = 0;
    int count = 0;

	public MyFxFrame() {
		JFXPanel fxPanel = new JFXPanel();
		this.add(fxPanel);
        
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                initFx(fxPanel);
            }
        });
		
	     this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	     
	     int width = 1080, height = 700;
	     Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
	     if (width < dimension.width && height < dimension.height) {
	    	 this.setSize(width, height); 
	    	 this.setLocationRelativeTo(null);
		 } else {
			this.setExtendedState(MAXIMIZED_BOTH);
		 }
	     this.setVisible(true);
	}
	
	private void initFx(JFXPanel fxPanel) {
        BorderPane root = new BorderPane();
        
        canvas = new Canvas(500, 350);
        gc = canvas.getGraphicsContext2D();
        root.setCenter(canvas);
        
        Canvas c1 = new Canvas(100,100);
        GraphicsContext gc1 = c1.getGraphicsContext2D();
        root.setLeft(c1);
        
        DropShadow ds = new DropShadow();   
		ds.setOffsetY(3.0);
		ds.setOffsetX(3.0);
		ds.setColor(Color.GRAY);
        
		Button btn = new Button();
        btn.setText("Say 'Hello World'");
        btn.setOnAction(new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Hello World!");
                String[] str = new String[1];
                str[0] = "str";
                Modana.getInstance().sendMsg("msg3", str);
                
                canvas.setTranslateX(x++);
                
                gc.applyEffect(ds);
                
                WritableImage img = canvas.snapshot(new SnapshotParameters(), null);
                gc1.drawImage(img, 0, 0, 100, 100);
            }
        });  
        root.setTop(btn);
        
        LinearGradient lg = new LinearGradient(0, 0, 1, 1, true,
                CycleMethod.REFLECT,
                new Stop(0.0, Color.WHITE),
                new Stop(1.0, Color.LIGHTBLUE));
        
        canvas.addEventHandler(MouseEvent.MOUSE_CLICKED, 
        	    new EventHandler<MouseEvent>() {
        	        @Override
        	        public void handle(MouseEvent e) {
        	        	gc.setFill(lg);
						gc.fillRoundRect(e.getX(),e.getY(), 50, 70, 10, 10);
//						gc.beginPath();
//						gc.moveTo(50, 50);
//						gc.bezierCurveTo(80, 50, 80, 100, 50, 100);
//						gc.closePath();
//						gc.stroke();
					}
        	    });
        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, 
        	    new EventHandler<MouseEvent>() {
        	        @Override
        	        public void handle(MouseEvent e) {
        	        	System.out.println(count++);
					}
        	    });
        canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, 
        	    new EventHandler<MouseEvent>() {
        	        @Override
        	        public void handle(MouseEvent e) {
        	        	System.out.println(count++);
					}
        	    });
        
		Text t = new Text();
		t.setEffect(ds);
		t.setCache(true);
		t.setX(120.0f);
		t.setY(120.0f);
		t.setFill(Color.RED);
		t.setText("show now!!!");
		t.setFont(Font.font("null", FontWeight.BOLD, 20));
		root.setBottom(t);
		
		gc.setFont(Font.font("null", FontWeight.BOLD, 20));
		gc.fillText("1111111", 20f, 20f);
        
        Scene scene = new Scene(root, 600, 450);
        fxPanel.setScene(scene);
	}
}
