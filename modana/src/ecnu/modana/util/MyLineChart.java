package ecnu.modana.util;

import java.awt.Panel;
import java.util.ArrayList;
import java.util.List;

import ecnu.modana.PlotComposer.JChart;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.Axis;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MyLineChart extends JChart
{
	private List<Object>xDataList=null;
	private String xName;
	private List<List<Number>> yDataList=new ArrayList<List<Number>>();
	private List<String>yNameList=new ArrayList<>();
	private String stageTitle="LineChart",chartTitle="chart";
	
	public void SetX(List<Object> xDataList,String xName) {this.xDataList=xDataList;this.xName=xName;}
	public void SetY(List<Number>yDataList,String yName){this.yDataList.add(yDataList);this.yNameList.add(yName);}
	
	public void SetTitle(String stageTitle,String chartTitle)
	{
		this.stageTitle=stageTitle;
		this.chartTitle=chartTitle;
	}
//	private void Work()
//	{
//		launch(null);
//	}
//	@Override
	public void start(Stage primaryStage) throws Exception 
	{
		primaryStage.initModality(Modality.APPLICATION_MODAL);
		primaryStage.setTitle(this.stageTitle);
		final Axis xAxis;
	    final NumberAxis yAxis = new NumberAxis();
		if(xDataList.get(0) instanceof String)
			xAxis = new CategoryAxis();
		else 
			xAxis=new NumberAxis();
		xAxis.setLabel(xName);
		final LineChart<Object, Number> lineChart = new LineChart<>(xAxis, yAxis);
		lineChart.setTitle(this.chartTitle);
		for(int i=0,j=yDataList.size();i<j;i++)
		{
			List<Number> yList=yDataList.get(i);
			XYChart.Series series = new XYChart.Series();
		    series.setName(yNameList.get(i));
		    for(int k=0;k<yList.size();k++)
		    	series.getData().add(new XYChart.Data(xDataList.get(k),yList.get(k)));
		    lineChart.getData().add(series);
		}
		Scene scene  = new Scene(lineChart,800,600);       
		primaryStage.setScene(scene);
		primaryStage.centerOnScreen();
		scene.getStylesheets().add("LineChart.css");
		//scene.getStylesheets().add(getClass().getResource("css/LineChart.css").toExternalForm());
		primaryStage.show();
	}
//	@Override
//	public void run() {
//		try {
//			System.err.println("aht");
//			start(new Stage());
//		} catch (Exception e) {
//			System.err.println("ah");
//			e.printStackTrace();
//		}		
//	}	
}
