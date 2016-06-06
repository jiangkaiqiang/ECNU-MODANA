package ecnu.modana.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ecnu.modana.model.ModelManager.DiagramType;
import ecnu.modana.ui.MuiGEditor;
import ecnu.modana.ui.prism.PrismEditor;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;

public abstract class AbstractModel
{
	//enum DiagramType{PrismDiagram;}
	public String name="PrismModel",type="dtmc";
	protected String ecoreFile=null;
	protected String modelPath=null;
	/**
	 * 
	 * @param ecoreFile ecore filePath
	 * @param modelEClassName EClassName of model
	 * @param modelName name of this model
	 */
	public AbstractModel(String name, String ecoreFile,String modelEClassName)
	{
		this.ecoreFile=ecoreFile;
		this.name=name;
		modelIO=new ModelIO(name,ecoreFile, modelEClassName);
	}
	public ModelIO modelIO=null;
	public void SetModelNameType(String name,String type)
	{
		this.name=name;
		modelIO.SetProperty(this.name, name,type);
	}
	/**
	 * all class diagram
	 */
	private List<String> diagramList=new ArrayList<>();
	public HashMap<String, ObservableList<Node>>graphContentMap=new HashMap<>();
	public HashMap<String, MuiGEditor> EditorMap=new HashMap<>();
//	public ArrayList<ObservableList<Node>> graphContentList ;
	
	//MuiGEditor muiGEditor=null;
	public MuiGEditor CreateDiagram(DiagramType diagramType,String diagramName)
	{
		MuiGEditor muiGEditor=null;
		if(!EditorMap.containsKey(diagramName)) 
			return muiGEditor;
		if(graphContentMap.containsKey(diagramName))
			return muiGEditor;
		muiGEditor= EditorMap.get(diagramName);
		graphContentMap.put(diagramName, muiGEditor.getGraphContent());
		diagramList.add(diagramName);
//		muiGEditor.getGraphContent().addListener(new ListChangeListener<Node>(){
//			@Override
//			public void onChanged(
//					javafx.collections.ListChangeListener.Change<? extends Node> c)
//			{
//				 while (c.next()) {
//				        System.err.println("Change: "+c.hashCode());
//				        if (c.wasRemoved()) {
//				         System.err.println(c.toString());
//				        }
//				        if (c.wasAdded()) {
//				         System.err.println(c.toString());
//				        }
//				      }
//				    }				
//		  });
//		muiGEditorList.add(muiGEditor);
//		graphContentList.add(muiGEditor.getGraphContent());
		return muiGEditor;
	}
	public void UpdateDiagram()
	{
		for(String diagramName:diagramList)
		{
			graphContentMap.remove(diagramName);
			graphContentMap.put(diagramName, EditorMap.get(diagramName).getGraphContent());
		}
	}
	public void UpdateDiagram(String diagramName)
	{
		System.out.println(diagramName+"----");
		if(graphContentMap.containsKey(diagramName)) System.err.println(diagramName);
		if(graphContentMap.containsKey(diagramName)) graphContentMap.remove(diagramName);
		graphContentMap.put(diagramName, EditorMap.get(diagramName).getGraphContent());
	}
	public boolean SaveModel(String filePath)
	{
		return true;
	}
	/**
	 * save element into EMF
	 * @param eclassName
	 * @param objName must be distinct
	 * @param propertyValue
	 */
	protected void SaveElement(String eclassName,String objName,String... propertyValue)
	{
		if(!modelIO.NewEClass(eclassName, objName))
		{
			System.err.println("create Eclass exist "+eclassName+" "+objName);
			//return ;
		}
		if(null==propertyValue) return;
		if(!modelIO.SetProperty(objName, propertyValue))
			System.err.println("SetProperty error: "+eclassName);
	}
	/**
	 * read model file,and add relate graphic element to last list
	 * @param ecoreFile
	 * @param filePath
	 * @return
	 */
	public boolean LoadFromFile(String filePath)
	{
		return true;
	}
}
