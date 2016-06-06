package ecnu.modana.PlotComposer;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.StringProperty;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.control.TextField;
import ecnu.modana.PlotComposer.JBarChart;

/**
 * @author JKQ
 *
 * 2015年11月29日下午3:42:11
 */
public class PlotComposer {
	private TextField xProperty,yProperty;
	List<Object> stepList = new ArrayList<Object>();
	List<Number> stateList = new ArrayList<Number>();
    List<Number> variableList = new ArrayList<Number>();
	public void SetXYList() {	
        stepList.add(1);
        stepList.add(2);
        stepList.add(3);
        variableList.add(1);
        stateList.add(4);
        variableList.add(7);
        stateList.add(4);
        variableList.add(9);
        stateList.add(6);
	}
	public LineChart<Object, Number> getLineChart(TextField xAxisField,TextField yAxisField) throws Exception {
		this.xProperty = xAxisField;
		this.yProperty = yAxisField;
		JLineChart jLineChart = null;
		jLineChart = new JLineChart();
		jLineChart.SetX(stepList);
		jLineChart.SetY(stateList);
		jLineChart.SetY(variableList);
		return jLineChart.getJLineChart(xProperty,yProperty);
	}
	public BarChart<String, Number> getBarChart(TextField xAxisField,TextField yAxisField) throws Exception {
		
		this.xProperty = xAxisField;
		this.yProperty = yAxisField;
		JBarChart jbarChart = null;
		jbarChart = new JBarChart();
		jbarChart.SetX(stepList);
		jbarChart.SetY(stateList);
		jbarChart.SetY(variableList);
		return jbarChart.getJBarChart(xProperty,yProperty);
	}
}
