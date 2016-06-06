package ecnu.modana.model;

import java.io.File;
import java.util.HashMap;

import org.apache.log4j.Logger;

import ecnu.modana.ui.MuiGEditor;
import javafx.event.Event;
import javafx.scene.control.Tab;
/**
 * manager all models,diagrams.Interacte with UI  singleton
 * @author LiuJufu
 */
public class ModelManager
{
	public Logger logger = Logger.getRootLogger();
	private ModelManager(){}
	private static ModelManager modelManager=null;
	public static ModelManager getInstance() {  
        if (modelManager == null) {    
            synchronized (ModelManager.class) {    
               if (modelManager == null) {    
            	   modelManager = new ModelManager();   
               }    
            }    
        }    
        return modelManager;   
    }  
    public enum ModelType
	{
		PrismModel;
	}
    //寤鸿涓�绉嶆ā鍨嬬殑鎵�鏈夊瓙鍥剧被鍨嬫斁涓�琛�
	public enum DiagramType
	{
		PrismDiagram
	};
	//public ObservableList<AbstractModel> modelList=FXCollections.observableArrayList();
    public HashMap<String, AbstractModel> modelListMap=new HashMap<>();
	public String CreateModel(String ecroePath,String modelName,ModelType modelType)
	{
		System.out.println("create model:"+modelName);
		AbstractModel abstractModel=null;
//		if(new File(ecroePath).exists()==false) return "ecroe鏂囦欢涓嶅瓨鍦�!";
//		if(modelName==null||modelName.isEmpty()) return "妯″瀷鍚嶇О鏈浘瀹氫箟";
		if(modelListMap.containsKey(modelName)) return "妯″瀷鍚嶇О宸插瓨鍦�";
		switch (modelType)
		{
		case PrismModel:abstractModel=new PrismModel(modelName,ecroePath,"PrismModel"); //"./prism.ecore"
			break;
		default:
			abstractModel=new PrismModel(modelName,ecroePath,"PrismModel");
			break;
		}
		//modelList.add(abstractModel);
		modelListMap.put(modelName, abstractModel);
		return "";
	}
	public AbstractModel GetModel(String modelName)
	{
		if(null==modelName)
		{
			System.err.println("model name is null");
			return null;
		}
		if(modelListMap.containsKey(modelName))
			return modelListMap.get(modelName);
		else 
		{
			System.err.println("not contain model:"+modelName);
			return null;
		}
	}
	public MuiGEditor GetEditor(String modelName,String diagramName)
	{
		return GetModel(modelName).EditorMap.get(diagramName);
	}
	public boolean CreateDiagram(MuiGEditor muiGEditor,String modelName,DiagramType diagramType,String diagramName)
	{
//		//check if there is tab selected
//		Tab oldTab = tabPane.getSelectionModel().getSelectedItem();
//		//create new tab
//		Tab tab = muiModule.createGaphicalEditor().buildEditorTab(
//			"New model", muiModule, primaryStage, tabPane, 
//			snapshotCanvas, menuBar, modelTreeView, newTreeItem);
//		//select new created tab
//		tabPane.getSelectionModel().select(tab);
//		//manually send select event message to new tab
//		if (oldTab != null) {
//			Event.fireEvent(oldTab, new Event(Tab.SELECTION_CHANGED_EVENT));
//		}
//		Event.fireEvent(tab, new Event(Tab.SELECTION_CHANGED_EVENT));
		modelListMap.get(modelName).EditorMap.put(diagramName, muiGEditor);
		modelListMap.get(modelName).CreateDiagram(diagramType, diagramName);
		return true;
	}
	public boolean SaveModel(String modelName,String filePath)
	{
		try{
			System.out.println("save model:"+modelListMap.keySet()+":"+modelName+",filePath:"+filePath);
			modelListMap.get(modelName).SaveModel(filePath);
			return true;
			}catch(Exception e)
			{
				e.printStackTrace();
				return false;
			}
	}
	public boolean ChangeModelName(String oldName,String newName)
	{
		return true;
	}
	public boolean ChangeDiagramName(String oldName,String newName)
	{
		return true;
	}
	public boolean DeleteModel(String modelName)
	{
		System.out.println("Delete Model:"+modelName);
		modelListMap.remove(modelName);
		return true;
	}
	public boolean DeleteDiagram(String modelName,String diagramName)
	{
		return true;
	}
}
