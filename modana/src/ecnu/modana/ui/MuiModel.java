package ecnu.modana.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.eclipse.emf.ecore.*;
public abstract class MuiModel
{
	public String name,type;
	public ObservableList<Integer> tIntegers=FXCollections.observableArrayList();
	public MuiModel(String name,String type)
	{
		this.name=name;
		this.type=type;
	}
}
