package fsa.m1.sidbd.gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import fsa.m1.sidbd.model.Attribute;
import fsa.m1.sidbd.model.Component;
import fsa.m1.sidbd.gui.DnDCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Control;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.util.Callback;

public class Controller implements Initializable{

	@FXML private ListView<Component> list_components;

	@FXML private Button btn_supprimer, btn_generer, btn_attribuer;
	@FXML private AnchorPane panel;
	@FXML private MenuBar menu_bar;

	@FXML private TreeView<Component> treeView;
	@FXML private TextArea codeTxt;

	@FXML public Label infos;

	//attribute items
	@FXML private TextField id, texte,width, height;
	@FXML private CheckBox ck_editable, ck_visible;
	@FXML private Button bg;
	@FXML private ComboBox<String> combo_color;

	// Set the Custom Data Format
	static final DataFormat COMPONENT_LIST = new DataFormat("ComponentList");

	//data qui represente l'ensemble des items qu'on peut selectionner
	ObservableList<Component> data = FXCollections.observableArrayList();

	//la list des composants qui existe dans le workspace (dragged)
	List<Component> listItem = new ArrayList<>();

	//le choix du layout envoyer par le main
	private String layout;


	//id fixed
	static int _id = 0;


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ObservableList<String> colors = FXCollections.observableArrayList("bleu","black","white","yellow");
		combo_color.setItems(colors);

		initialiser();
	}

	public void initialiser() {

		//changer le type du maincontainer selon le type choisi par l'utilisateur
		//changeMainContainer();

		//data for the composants
		Component parent = new Component("file:resources/fenetre.png", "Fenetre", 0, null,new ArrayList<>());

		//adding the data to the list
		data.add(parent);
		data.add(new Component("file:resources/button.png", "button", 1, parent, false, new ArrayList<>(),new ArrayList<>()));
		data.add(new Component("file:resources/textfield.png", "text field", 2, parent, false, new ArrayList<>(),new ArrayList<>()));
		data.add(new Component("file:resources/label.png", "label", 3, parent, false, new ArrayList<>(),new ArrayList<>()));
		data.add(new Component("file:resources/listview.png", "ListView", 4, parent, false, new ArrayList<>(),new ArrayList<>()));
		data.add(new Component("file:resources/hbox.png", "Hbox", 5, parent, true, new ArrayList<>(),new ArrayList<>()));
		data.add(new Component("file:resources/vbox.png", "Vbox", 6, parent, true, new ArrayList<>(),new ArrayList<>()));
		data.add(new Component("file:resources/webview.png", "WebView", 7, parent, false, new ArrayList<>(),new ArrayList<>()));
		data.add(new Component("file:resources/pane.png", "Pane", 8, parent, true, new ArrayList<>(),new ArrayList<>()));
		data.add(new Component("file:resources/gridpane.png", "GridPane", 9, parent, true, new ArrayList<>(), new ArrayList<>()));
		data.add(new Component("file:resources/checkbox.png", "CheckBox", 10, parent,new ArrayList<>()));
		data.add(new Component("file:resources/borderPane.png", "BorderPane", 11, parent, true, new ArrayList<>(),new ArrayList<>()));


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

		treeView.getSelectionModel().selectedItemProperty().addListener(e->{
			desactivateAttributes(false);
			//panel.setUserData(treeView.getSelectionModel().getSelectedItem().getValue());
			id.setText(treeView.getSelectionModel().getSelectedItem().getValue().getId_c());
			showAttributes();
		});


		//mise ajour de la liste
		updateItemTree();


		//initialise the code part with example code
		codeTxt.setText(" <frame> \n");

		//drag and drop over the frame(treeview) elemnt
		treeView.setCellFactory(new Callback<TreeView<Component>, TreeCell<Component>>() {
            @Override
            public TreeCell call(TreeView<Component> param) {
                return new DnDCell(param);
            }
        });

		//la liste des elemnet de type container
		List<Component> lsContainers = new ArrayList<>();
		for(Component c:data){
			if(c.isContainer())
				lsContainers.add(c);
		}
		//et on vas appliquer le drag and drap sur toutes les type container
		//appliquerDragAndDropOnContainer();

	}

	//show les attributs deja attribuer pour cette composant
	private void showAttributes(){
		Component selected = treeView.getSelectionModel().getSelectedItem().getValue();

		if(!selected.getLsAttributes().isEmpty()){
			id.setText(selected.getId_c());
			height.setText(selected.getLsAttributes().get(1).getValue());
			width.setText(selected.getLsAttributes().get(2).getValue());
			texte.setText(selected.getLsAttributes().get(3).getValue());
			ck_editable.setText(selected.getLsAttributes().get(4).getValue());
			ck_visible.setText(selected.getLsAttributes().get(5).getValue());
			combo_color.setValue(selected.getLsAttributes().get(6).getValue());
			bg.setText(selected.getLsAttributes().get(7).getValue());
		}
	}

	/*
	 *  appliquer le drag and drap sur toutes les type container
	 */
	private void appliquerDragAndDropOnContainer() {
		for(Node n:panel.getChildren()){
			if(n.equals(BorderPane.class) || n.equals(VBox.class) ||
					n.equals(HBox.class) || n.equals(AnchorPane.class)){

				n.setOnDragOver(new EventHandler <DragEvent>(){
					public void handle(DragEvent event){
						panelDragOver(event, n);
					}
				});
				n.setOnDragDropped(new EventHandler <DragEvent>(){
					public void handle(DragEvent event){
						panelDragDropped(event, n);
					}
				});
				n.setOnDragDone(new EventHandler <DragEvent>(){
					public void handle(DragEvent event){
						panelDragDone(event, n);
					}
				});
			}
		}

	}

	/*
	 * method to change the main container layout selon le type choisi
	 */
	public void changeMainContainer() {
		Node container = null;
		String containerSelected = getLayout();
		//on va selectionner le container selon le choix
		if(containerSelected.equals("Frame"))
			container = new AnchorPane();
		else
			if(containerSelected.equals("BorderLayout"))
				container = new BorderPane();
			else
				if(containerSelected.equals("LinearLayout(Vertical)"))
					container = new VBox();
				else
					container = new HBox();

		//et on vas remplacer le container avec notre choix
		container.prefHeight(panel.getPrefHeight());
		container.prefWidth(panel.getPrefWidth());
		panel.getChildren().add(container);

		//panel = container;

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
			_id++;
			c.setId_c("id"+_id);


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

			//setter le id fixe
			id.setText("id"+_id);

			//adding to the tree
			treeView.getRoot().getChildren().add(new TreeItem<>(c));

			//System.out.println(c.getName());

			//verification au niveau du code
			String splitting[] = codeTxt.getText().split(" ");
			String out = "";

			for(int i=0;i<splitting.length;i++){
				if(!splitting[i].equals("</frame>"))
					out = out + " " + splitting[i];
			}

			codeTxt.setText(out+" \n\t <"+c.getName()+" "
					+ " id='"+c.getId_c()+"' > "
					+" </"+c.getName()+"> "
					+ "\n </frame> ");

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

	/*
	 * on veut retourner le composant selectionner apartir de la liste des composants
	 */
	private Node getSelectedNodeType(ArrayList<Component> list, double x, double y) {
		Component selected = list.get(0);

		Node node = new AnchorPane();

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
				ls.setMaxSize(80, 80);
				//ls.prefWidth(20);
				node = ls;
				break;
			case 5:
				HBox hb = new HBox();
				hb.setMaxSize(80, 80);
				hb.setStyle("background-color: #e3e3e3;");
				node = hb;
				break;
			case 6:
				VBox vb = new VBox();
				vb.setMaxSize(80, 80);
				vb.setStyle("background-color: #e3e3e3;");
				node = vb;
				break;
			case 7:
				WebView wb = new WebView();
				wb.setMaxSize(80, 80);
				wb.setStyle("background-color: #e3e3e3;");
				node = wb;
				break;
			case 8:
				AnchorPane p = new AnchorPane();
				p.setMaxSize(80, 80);
				p.setStyle("background-color: #e3e3e3;");
				node = p;
				break;
			case 9:
				GridPane gp = new GridPane();
				gp.setMaxSize(80, 80);
				node = gp;
				break;
			case 10:
				CheckBox ck = new CheckBox("checkbox");
				node = ck;
				break;
			case 11:
				BorderPane bp = new BorderPane();
				bp.setMaxSize(80, 80);
				node = bp;
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
		//initilialiser avec les attributs deja selectionner
		Component selected = treeView.getSelectionModel().getSelectedItem().getValue();
		//attribuer les nouvaux attributs entres
		ArrayList<Attribute> newAttributes = new ArrayList<>();

		newAttributes.add(new Attribute("id", id.getText()));
		newAttributes.add(new Attribute("height", height.getText()));
		newAttributes.add(new Attribute("width", width.getText()));
		newAttributes.add(new Attribute("texte", texte.getText()));

		String editable = "false";
		if(ck_editable.isSelected())
			editable = "true";
		newAttributes.add(new Attribute("editable", editable));

		String visible = "false";
		if(ck_visible.isSelected())
			visible = "true";
		newAttributes.add(new Attribute("visible", visible));

		newAttributes.add(new Attribute("color", combo_color.getValue()));
		newAttributes.add(new Attribute("bg", bg.getText()));

		selected.setLsAttributes(newAttributes);

		//changes selon les attributs

		//adding attributs to xml code
		addAttributsToXmlTag(id.getText(), selected);

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setContentText("Ok ! Bien Ajouter :) ");
		alert.setHeaderText("Les attributs sonts bien ajouter a "+selected.getName()+" !");
		alert.showAndWait();
	}
	public void HandleGenerer(){
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setContentText("Wait ! Working progress :) ");
		alert.setHeaderText("Wait ! Working on that !");
		alert.showAndWait();

		//la liste des element choisis
		for(TreeItem<Component> tr:treeView.getRoot().getChildren()){
			System.out.println(tr.getValue().getName());
		}

		//fermer la frame
		codeTxt.appendText("</frame>");
	}
	public void HandleSupprimer(){
		if(treeView.getSelectionModel().getSelectedItem() != null){

			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Confirmer votre choix");
			alert.setHeaderText("Attention, Confirmer votre choix ");
			alert.setContentText("Est-ce que vous voulez vraiment supprimer cette composant ?");

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK){
				TreeItem<Component> selected = treeView.getSelectionModel().getSelectedItem();
				// working !!
				panel.getChildren().remove(treeView.getSelectionModel().getSelectedIndex());
				treeView.getRoot().getChildren().remove(selected);

			} else {

			}
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

	private void panelDragOver(DragEvent event, Node p)
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

	private void panelDragDropped(DragEvent event, Node p)
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

			_id++;
			c.setId_c("id"+_id);

			//verification au niveau du code
			String splitting[] = codeTxt.getText().split(" ");
			String out = "";

			for(int i=0;i<splitting.length;i++){
				if(!splitting[i].equals("</frame>"))
					out = out + " " + splitting[i];
			}

			codeTxt.setText(out+" \n\t <"+c.getName()+" "
					+ " id='"+c.getId_c()+"' > "
					+" </"+c.getName()+"> "
					+"\n </frame> ");


			//setter le id fixe
			id.setText("id"+_id);

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

			//String attributs_txt = "";
			//for(Attribute atr:c.getLsAttributes())
				//attributs_txt = attributs_txt+" "+atr.getName()+"='"+atr.getValue()+"' ";

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

	private void panelDragDone(DragEvent event, Node  p){
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
		//id.setDisable(val);
		height.setDisable(val);
		width.setDisable(val);
		texte.setDisable(val);
		ck_editable.setDisable(val);
		ck_visible.setDisable(val);
		combo_color.setDisable(val);
		bg.setDisable(val);

		//vider les champs
		//id.setText("");
		height.setText("");
		width.setText("");
		texte.setText("");
		ck_editable.setSelected(false);
		ck_visible.setSelected(false);
		combo_color.setValue("");
		bg.setText("...");
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


	//ajouter la liste des attributs a un elemnt xml
	private void addAttributsToXmlTag(String id, Component c){

		String after = "";
		String before = "";

		String t[] = codeTxt.getText().split(" ");

		//test
		for(int k=0;k<t.length;k++)
			System.out.println(t[k]+" - ");

		for(int i=0;i<t.length;i++){
			if(t[i].equals("id='"+id+"'")){
				for(int j=i+1;j<t.length;j++)
					after = after +" "+ t[j];
				break;
			}else{
				before = before + " "+ t[i];
			}
		}

		String attributs_txt = "";
		for(Attribute atr:c.getLsAttributes()){
			if(!atr.getValue().equals("")) //s'il est vide pas la peine de l'ajouter
				attributs_txt = attributs_txt+" "+atr.getName()+"='"+atr.getValue()+"' ";
		}


		System.out.println("Before :"+before);
		System.out.println("ch :"+attributs_txt);
		System.out.println("after : "+after);

		//set the code approprie
		codeTxt.setText(before+attributs_txt+after);

		//close parents
		closeParentBalise();
	}

	//fermer la balise parente
	private void closeParentBalise(){
		String t[] = codeTxt.getText().split(" ");
		String allTheCode = "";

		for(int i=0;i<t.length-2;i++){
			allTheCode = allTheCode + " "+ t[i];
		}
		allTheCode = allTheCode + "\n </frame> ";

		codeTxt.setText(allTheCode);
	}

	//getters and setters
	public void setLayout(String layout) {
		this.layout = layout;
	}
	public String getLayout() {
		return layout;
	}
}
