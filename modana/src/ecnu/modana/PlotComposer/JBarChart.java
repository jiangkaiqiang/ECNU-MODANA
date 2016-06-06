package ecnu.modana.PlotComposer;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.chart.Axis;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TextField;

/**
 * @author JKQ
 *
 * 2015年11月29日下午3:41:55
 */
public class JBarChart extends JChart
{
	private List<Object>xDataList=null;
//	private String xName;
	private List<List<Number>> yDataList=new ArrayList<List<Number>>();
//	private List<String>yNameList=new ArrayList<>();
	//private String chartTitle="chart";
//	private StringProperty chartTitle=new SimpleStringProperty("chart");
	private BarChart<String, Number> barChart = null;
	public void SetX(List<Object> xDataList) {this.xDataList=xDataList;}
	public void SetY(List<Number>yDataList){this.yDataList.add(yDataList);}

//	@Override
	public BarChart<String, Number> getJBarChart(TextField xAxisField,TextField yAxisField)  throws Exception 
	{
		final CategoryAxis xAxis;
	    final NumberAxis yAxis = new NumberAxis();
//		if(xDataList.get(0) instanceof String)
			xAxis = new CategoryAxis();
//		else 
//			xAxis=new NumberAxis();
//		xAxis.setLabel(xName);
		yAxis.labelProperty().bindBidirectional(yAxisField.textProperty());
		xAxis.labelProperty().bindBidirectional(xAxisField.textProperty());
		barChart = new BarChart<String, Number>(xAxis, yAxis);
		//this.chartTitle=stringProperty;
//		chartTitle.bindBidirectional(stringProperty.textProperty());
//		lineChart.setTitle(stringProperty.textProperty().getValue());
		for(int i=0,j=yDataList.size();i<j;i++)
		{
			List<Number> yList=yDataList.get(i);
			XYChart.Series<String,Number> series = new XYChart.Series<String,Number>();
//		    series.setName(yNameList.get(i));
		    for(int k=0;k<yList.size();k++)
		    	series.getData().add(new XYChart.Data(xDataList.get(k).toString(),yList.get(k)));
		    barChart.getData().add(series);
		}  
		return barChart;
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
