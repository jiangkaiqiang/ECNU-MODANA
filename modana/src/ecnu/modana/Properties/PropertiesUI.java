package ecnu.modana.Properties;

import java.io.File;
import java.io.IOException;

import ecnu.modana.model.PrismModel.Reward;
import ecnu.modana.ui.MyTextConvertor;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * @author JKQ
 *
 * 2015年11月29日下午3:41:30
 */
public class PropertiesUI {
	//plotComposer window
    private Stage propertiesStage = null;
    private MenuBar menuBar = null;
    private PropertiesManager pManager = new PropertiesManager();
	 public void start(Stage plotComposerStage) throws Exception  {
			this.propertiesStage = plotComposerStage;
			plotComposerStage.initModality(Modality.WINDOW_MODAL);
			plotComposerStage.setOpacity(0.87);
			plotComposerStage.setTitle("Properties List");
			plotComposerStage.setWidth(500);
			plotComposerStage.setHeight(300);
			plotComposerStage.centerOnScreen();
			plotComposerStage.setResizable(false);
			BorderPane propertiesRoot = new BorderPane();
			initPlotPanel(propertiesRoot);
			Scene plotScene = new Scene(propertiesRoot);
			plotComposerStage.setScene(plotScene);
			plotComposerStage.show();
		}
	private void initPlotPanel(BorderPane propertiesRoot) {
		// TODO Auto-generated method stub
		 VBox menuPane = new VBox();
	 	 addPropertiesMenu(menuPane);
	 	 propertiesRoot.setTop(menuPane);
	 	 BorderPane centerPane = new BorderPane();
	 	 addPropertiesList(centerPane);
	 	propertiesRoot.setCenter(centerPane);
	}
	private void addPropertiesList(BorderPane centerPane) {
		// TODO Auto-generated method stub
		TableView<Properties> propertiesTableView = new TableView<Properties>();
		propertiesTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		propertiesTableView.setEditable(true);
		// set data of table view
		propertiesTableView.setItems(pManager.propertiesObervableList);
		// add columns
		TableColumn<Properties, String> propertiesCol = new TableColumn<Properties, String>(
				"Property    Value");
		propertiesCol.setEditable(true);
		propertiesCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Properties, String>, ObservableValue<String>>() {

					@Override
					public ObservableValue<String> call(
							CellDataFeatures<Properties, String> f) {
						return f.getValue().getProperties();
					}
				});
		propertiesCol.setCellFactory(new Callback<TableColumn<Properties, String>, TableCell<Properties, String>>() {
			@Override
			public TableCell<Properties, String> call(TableColumn<Properties, String> f) {
				TextFieldTableCell<Properties, String> cell = new TextFieldTableCell<Properties, String>(new MyTextConvertor());
				cell.setAlignment(Pos.CENTER);
				return cell;
			}
		});
		propertiesCol.setMinWidth(300);
		propertiesCol.setMaxWidth(500);
		propertiesCol.setPrefWidth(500);
		addPropertyOperation(propertiesCol,propertiesTableView);
		propertiesTableView.getColumns().add(propertiesCol);
		centerPane.setCenter(propertiesTableView);
		
	}
	private void addPropertyOperation(
			TableColumn<Properties, String> propertiesCol,
			TableView<Properties> propertiesTableView) {
		// TODO Auto-generated method stub
		propertiesTableView.addEventHandler(MouseEvent.MOUSE_RELEASED,
				new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				if(e.getButton().equals(MouseButton.SECONDARY)){
				ContextMenu rightClickCol = new ContextMenu();
				MenuItem deleteProperty = new MenuItem("Delete Property");
				MenuItem verify = new MenuItem("Verify");
				MenuItem verifyTraditional = new MenuItem("VerifyTradi");
				rightClickCol.getItems().addAll(deleteProperty,verify,verifyTraditional);
				propertiesTableView.setContextMenu(rightClickCol);
				deleteProperty.setOnAction(new EventHandler<ActionEvent>() {
		        	public void handle(ActionEvent e){
		        		pManager.propertiesObervableList.removeAll(propertiesTableView.getSelectionModel().getSelectedItems());   
		        	}
				});
				verify.setOnAction(new EventHandler<ActionEvent>() {
		        	public void handle(ActionEvent e){
		        		try {
							pManager.verifier(propertiesTableView.getSelectionModel().getSelectedItems().get(0),0);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
		        	}
				});
				verifyTraditional.setOnAction(new EventHandler<ActionEvent>() {
		        	public void handle(ActionEvent e){
		        		try {
							pManager.verifier(propertiesTableView.getSelectionModel().getSelectedItems().get(0),1);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
		        	}
				});
			   }
			}
		});
		
	}
	private void addPropertiesMenu(VBox menuPane) {
		// TODO Auto-generated method stub
		menuBar = new MenuBar();
        Menu menuProperties= new Menu("Properties");
        Menu menuAdd = new Menu("Add Property");
        MenuItem saveItem = new MenuItem("Save Properties");
        saveProperties(saveItem);
        MenuItem openItem = new MenuItem("Open Properties");
        openProperties(openItem);
        MenuItem addItem = new MenuItem("Add Property");
        addProperty(addItem);
        menuProperties.getItems().addAll(saveItem,openItem);
        menuAdd.getItems().add(addItem);
        menuBar.getMenus().addAll(menuProperties,menuAdd);
        menuPane.getChildren().add(menuBar);
	}
	private void addProperty(MenuItem addItem) {
		// TODO Auto-generated method stub
		addItem.setOnAction(new EventHandler<ActionEvent>() {
        	public void handle(ActionEvent e){
        		//Properties pro = new Properties("new property");
        		Properties pro = new Properties("P=?[F in_v>=5 & time>=3 ]");
        		//Properties pro = new Properties("P=?[F h<=0.1 & h>=-0.1 & in_v<=1.5 & in_v>=-1.5 ]"); //  P=?[G s==1 | s==0 ]");// P=?[F in_v>=5 & time>=3 ]
        		pManager.propertiesObervableList.add(pro);
        	}
		});
		
	}
	private void openProperties(MenuItem openItem) {
		// TODO Auto-generated method stub
		openItem.setOnAction(new EventHandler<ActionEvent>() {
        	public void handle(ActionEvent e){
				FileChooser fileChooser = new FileChooser();
				Stage s = new Stage();
				File file = fileChooser.showSaveDialog(s);
				if (file == null)
					return;
				String FilePath = file.getAbsolutePath().toString().replace("\\","/");//.replaceAll(".txt", "")+ ".txt";
        	}
		});
	}
	private void saveProperties(MenuItem saveItem) {
		// TODO Auto-generated method stub
		saveItem.setOnAction(new EventHandler<ActionEvent>() {
        	public void handle(ActionEvent e){
				FileChooser fileChooser = new FileChooser();
				FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
						"TXT files (*.txt)", "*.txt");
				fileChooser.getExtensionFilters().add(extFilter);
				Stage s = new Stage();
				File file = fileChooser.showSaveDialog(s);
				if (file == null)
					return;
				String FilePath = file.getAbsolutePath().toString().replace("\\","/");//.replaceAll(".txt", "")+ ".txt";
        	}
		});
	}
	public Stage getPropertiesStage() {
 		return propertiesStage;
 	}
}
