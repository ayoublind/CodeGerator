package fsa.m1.sidbd.gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import fsa.m1.sidbd.model.Component;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class Controller implements Initializable{

	@FXML private ListView<Component> list_components;

	@FXML private Button btn_supprimer, btn_generer, btn_attribuer;
	@FXML private VBox panel;
	@FXML private MenuBar menu_bar;

	@FXML private TreeView<Component> treeView;
	@FXML private TextArea codeTxt;

	//attribute items
	@FXML private TextField id, texte,width, height;
	@FXML private CheckBox ck_editable, ck_visible;
	@FXML private Button bg;
	@FXML private ComboBox<Color> combo_color;

	// Set the Custom Data Format
	static final DataFormat COMPONENT_LIST = new DataFormat("ComponentList");

	//data qui represente l'ensemble des items qu'on peut selectionner
	ObservableList<Component> data = FXCollections.observableArrayList();

	//la list des composants qui existe dans le workspace (dragged)
	List<Component> listItem = new ArrayList<>();


	@Override
	public void initialize(URL location, ResourceBundle resources) {


		//data for the composants
		Component parent = new Component("file:resources/fenetre.png", "Fenetre", 0, null);

		//adding the data to the list
		data.add(parent);
		data.add(new Component("file:resources/icon.png", "button", 1, parent));
		data.add(new Component("file:resources/delete.png", "text field", 2, parent));
		data.add(new Component("file:resources/dart-board.png", "label", 3, parent));
		data.add(new Component("file:resources/play-button.png", "ListView", 4, parent));



		//la liste des composants
		list_components.setCellFactory(new ComponentsCell());
		list_components.setItems(data);
		list_components.getSelectionModel().selectedItemProperty().addListener(e->{
			Component c = list_components.getSelectionModel().getSelectedItem();
			System.out.println(c.toString());
		});

		// Add mouse event handlers for the source
		list_components.setOnDragDetected(new EventHandler <MouseEvent>(){
			public void handle(MouseEvent event){
				//System.out.println("Event on Source: drag detected");
				dragDetected(event, list_components);
			}
		});

		list_components.setOnDragOver(new EventHandler <DragEvent>() {
			public void handle(DragEvent event)
			{
				//System.out.println("Event on Source: drag over");
				dragOver(event, list_components);
			}
		});

		list_components.setOnDragDropped(new EventHandler <DragEvent>() {
			public void handle(DragEvent event)
			{
				//System.out.println("Event on Source: drag dropped");
				dragDropped(event, list_components);
			}
		});

		list_components.setOnDragDone(new EventHandler <DragEvent>() {
			public void handle(DragEvent event)
			{
				//System.out.println("Event on Source: drag done");
				dragDone(event, list_components);
			}
		});

		// Add mouse event handlers for the target

		panel.setOnDragOver(new EventHandler <DragEvent>()
		{
			public void handle(DragEvent event)
			{
				//System.out.println("Event on Target: drag over");
				panelDragOver(event, panel);
			}
		});

		panel.setOnDragDropped(new EventHandler <DragEvent>()
		{
			public void handle(DragEvent event)
			{
				//System.out.println("Event on Target: drag dropped");
				panelDragDropped(event, panel);
			}
		});

		panel.setOnDragDone(new EventHandler <DragEvent>()
		{
			public void handle(DragEvent event)
			{
				//System.out.println("Event on Target: drag done");
				panelDragDone(event, panel);
			}
		});

		//set the panel change size
		//DragResizeMod.makeResizable(panel, null);

		//on va recupirer le liste des composants qui se trouve dans le panel (workspace)

		//adding the man layout
		listItem.add(parent);

		//treeview
		treeView.setRoot(new TreeItem<>(parent));
		treeView.setShowRoot(true);



		//mise ajour de la liste
		updateItemTree();

	}

	//update les elements du treeview
	private void updateItemTree(){
		//the root item
		if(!listItem.isEmpty()){
			//tree view for the component
			for(int i=1;i<listItem.size();i++){
				TreeItem<Component> item = new TreeItem<>(listItem.get(i));
				boolean found = false;
				for (TreeItem<Component> depNode : treeView.getRoot().getChildren()) {
					if (depNode.getValue().equals(item.getValue())){
						depNode.getChildren().add(item);
						found = true;
						break;
					}
				}
				if (!found) {
					TreeItem<Component> depNode = new TreeItem<>(listItem.get(i).getParentCompo());
					treeView.getRoot().getChildren().add(depNode);
					depNode.getChildren().add(item);
				}
			}
		}
		//get the corrent selected item and set the cell factory by the costmer type
		TreeItem<Component> selectedItem = treeView.getSelectionModel().getSelectedItem();
		treeView.setCellFactory((TreeView<Component> p) -> new TextFieldTreeCellImpl(selectedItem));

	}
	//the cellfactory class for the treefield
	private final class TextFieldTreeCellImpl extends TreeCell<Component> {

        private TextField textField;
        private final ContextMenu addMenu = new ContextMenu();

        public TextFieldTreeCellImpl(final TreeItem<Component> selected) {
            MenuItem addMenuItem = new MenuItem("Delete");
            addMenu.getItems().add(addMenuItem);
            addMenuItem.setOnAction(e -> {
                getTreeItem().getChildren().remove(selected);
            });
        }

        @Override
        public void startEdit() {
            super.startEdit();
            if (textField == null) {
                createTextField();
              }
              setText(null);
              setGraphic(textField);
              textField.selectAll();
        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();
            setText(getItem().toString());
            setGraphic(getTreeItem().getGraphic());
        }

        @Override
        public void updateItem(Component item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setText(null);
                setGraphic(null);
              } else {
                if (isEditing()) {
                  if (textField != null) {
                    textField.setText(getString());
                  }
                  setText(null);
                  setGraphic(textField);
                } else {
                  setText(getString());
                  setGraphic(getTreeItem().getGraphic());
                }
              }
        }

        private void createTextField() {
            textField = new TextField(getString());
            textField.setOnKeyReleased((KeyEvent t) -> {
              if (t.getCode() == KeyCode.ENTER) {
            	  //commitEdit(textField.getText());
              } else
            	  if (t.getCode() == KeyCode.ESCAPE) {
            		  cancelEdit();
              }
            });
          }

        private String getString() {
            return getItem() == null ? "" : getItem().toString();
        }
    }

	//drag detected event

	private void dragDetected(MouseEvent event, ListView<Component> listView){
		// Make sure at least one item is selected
		int selectedCount = listView.getSelectionModel().getSelectedIndices().size();

		if (selectedCount == 0)
		{
			event.consume();
			return;
		}

		// Initiate a drag-and-drop gesture
		Dragboard dragboard = listView.startDragAndDrop(TransferMode.COPY_OR_MOVE);

		// Put the the selected items to the dragboard
		ArrayList<Component> selectedItems = this.getSelectedComponent(listView);

		ClipboardContent content = new ClipboardContent();
		content.put(COMPONENT_LIST, selectedItems);

		dragboard.setContent(content);
		event.consume();
	}

	private void dragOver(DragEvent event, ListView<Component> listView)
	{
		// If drag board has an ITEM_LIST and it is not being dragged
		// over itself, we accept the MOVE transfer mode
		Dragboard dragboard = event.getDragboard();

		if (event.getGestureSource() != listView && dragboard.hasContent(COMPONENT_LIST))
		{
			event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
		}

		event.consume();
	}
	private ArrayList<Component> getSelectedComponent(ListView<Component> listView)
	{
		// Return the list of selected Fruit in an ArratyList, so it is
		// serializable and can be stored in a Dragboard.
		ArrayList<Component> list = new ArrayList<>(listView.getSelectionModel().getSelectedItems());

		return list;
	}

	@SuppressWarnings("unchecked")
	private void dragDropped(DragEvent event, ListView<Component> listView)
	{
		boolean dragCompleted = false;

		// Transfer the data to the target
		Dragboard dragboard = event.getDragboard();

		if(dragboard.hasContent(COMPONENT_LIST))
		{
			ArrayList<Component> list = (ArrayList<Component>)dragboard.getContent(COMPONENT_LIST);
			//listView.getItems().addAll(list);
			double x = event.getX();
			double y = event.getY();

			Component c = list.get(0);
			//c.setLayoutX(x);
			//c.setLayoutY(y);

			panel.getChildren().add(getSelectedNodeType(list, x, y));

			/*ImageView compo = new ImageView(c.getImageUrl());
			compo.setFitHeight(30);
			compo.setFitWidth(30);
			compo.setPreserveRatio(true);
			panel.getChildren().add(compo);*/

			//add this componennt to the listitem
			listItem.add(c);

			//adding to the tree
			treeView.getRoot().getChildren().add(new TreeItem<>(c));

			//adding context menu
			//initContextMenu(getSelectedNodeType(list, x, y));
			// Data transfer is successful
			dragCompleted = true;

			/*/rendre tout les items du panel Resizable
			for(Node n:panel.getChildren()){
				DragResizeMod.makeResizable(n);
				n.resize(n.getLayoutX(), n.getLayoutY());

			}*/
		}

		// Data transfer is not successful
		event.setDropCompleted(dragCompleted);
		event.consume();
	}

	private Node getSelectedNodeType(ArrayList<Component> list, double x, double y) {
		Component selected = list.get(0);

		Node node = null;

		switch (selected.getType()) {
			case 0:
				node = new AnchorPane();
				break;
			case 1:
				node = new Button(selected.getName());
				break;
			case 2:
				TextField t = new TextField();
				t.setEditable(false);
				node = t;
				break;
			case 3:
				Label lb = new Label(selected.getName());

				node = lb;
				break;
			case 4:
				ListView<String> ls = new ListView<>();
				ls.setItems(FXCollections.observableArrayList("item1","item2","item3","item4"));
				node = ls;
				break;
			default:
				break;
		}


		//positionner le node
		node.setLayoutX(x);
		node.setLayoutY(y);

		//lorsque on click sur une button
		ComponentClickedItem(node);

		//return
		return node;
	}

	private void dragDone(DragEvent event, ListView<Component> listView){
		// Check how data was transfered to the target
		// If it was moved, clear the selected items
		TransferMode tm = event.getTransferMode();

		if (tm == TransferMode.MOVE)
		{
			//removeSelectedFruits(listView);
		}

		event.consume();
	}

	//les handllers : evenements pour manipuler les buttons
	public void HandleAttribuer(){
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setContentText("Wait ! Working progress :) ");
		alert.setHeaderText("Wait ! Working on that !");
		alert.showAndWait();
	}
	public void HandleGenerer(){
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setContentText("Wait ! Working progress :) ");
		alert.setHeaderText("Wait ! Working on that !");
		alert.showAndWait();
	}
	public void HandleSupprimer(){
		if(treeView.getSelectionModel().getSelectedItem() != null){
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setContentText("Wait ! Working progress :) ");
			alert.setHeaderText("Wait ! Working on that !");
			alert.showAndWait();
		}else{
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText("Erreur ! il faut selectionner un elt du tree avant de suppr");
			alert.setHeaderText("Erreur ! No data selected !");
			alert.showAndWait();
		}
	}



	/*
	 *
	 */

	private void panelDragOver(DragEvent event, Pane p)
	{
		// If drag board has an ITEM_LIST and it is not being dragged
		// over itself, we accept the MOVE transfer mode
		Dragboard dragboard = event.getDragboard();

		if (event.getGestureSource() != p && dragboard.hasContent(COMPONENT_LIST))
		{
			event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
		}

		event.consume();
	}

	private void panelDragDropped(DragEvent event, Pane p)
	{
		boolean dragCompleted = false;

		// Transfer the data to the target
		Dragboard dragboard = event.getDragboard();

		if(dragboard.hasContent(COMPONENT_LIST))
		{
			ArrayList<Component> list = (ArrayList<Component>)dragboard.getContent(COMPONENT_LIST);
			//list_components.getItems().addAll(list);
			double x = event.getX();
			double y = event.getY();

			Component c = list.get(0);
			//c.setLayoutX(x);
			//c.setLayoutY(y);

			panel.getChildren().add(getSelectedNodeType(list, x, y));

			/*ImageView compo = new ImageView(c.getImageUrl());
			compo.setFitHeight(30);
			compo.setFitWidth(30);
			compo.setPreserveRatio(true);
			panel.getChildren().add(compo);*/

			//add this componennt to the listitem
			listItem.add(c);

			//adding to the tree
			treeView.getRoot().getChildren().add(new TreeItem<>(c));


			//adding context menu
			//initContextMenu(getSelectedNodeType(list, x, y));
			// Data transfer is successful
			dragCompleted = true;

			/*/rendre tout les items du panel Resizable
			for(Node n:panel.getChildren()){
				DragResizeMod.makeResizable(n);
				n.resize(n.getLayoutX(), n.getLayoutY());
			}*/
		}

		// Data transfer is not successful
		event.setDropCompleted(dragCompleted);
		event.consume();
	}

	private void panelDragDone(DragEvent event, Pane  p){
		// Check how data was transfered to the target
		// If it was moved, clear the selected items
		TransferMode tm = event.getTransferMode();

		if (tm == TransferMode.MOVE)
		{
			//removeSelectedFruits(listView);
		}

		event.consume();
	}

	//methode event on button clicked for components on the panel
	private void ComponentClickedItem(Node node){
		node.setOnMouseClicked(e->{
			desactivateAttributes(false);
			System.out.println("mousse clicked");
		});
	}

	//methode to disactivate the attributes items
	public void desactivateAttributes(boolean val){
		id.setDisable(val);
		height.setDisable(val);
		width.setDisable(val);
		texte.setDisable(val);
		ck_editable.setDisable(val);
		ck_visible.setDisable(val);
		combo_color.setDisable(val);
		bg.setDisable(val);
	}

	//this methode is used to generate the component from name
	public String componentFromName(String name){
		String res = null;
		for(TreeItem<Component> item : treeView.getRoot().getChildren()){
			if(item.getParent().getValue().equals(name))
				res = item.getParent().getValue().getName();
		}
		return res;
	}


}
