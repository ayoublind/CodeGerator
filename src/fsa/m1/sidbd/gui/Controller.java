package fsa.m1.sidbd.gui;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.DOMBuilder;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.xml.sax.SAXException;

import fsa.m1.sidbd.model.Attribute;
import fsa.m1.sidbd.model.Component;
import fsa.m1.sidbd.gui.DnDCell;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
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
import javafx.scene.image.Image;
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
import javafx.stage.FileChooser;
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
	@FXML Button bg;
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

	Document doc = new Document();


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ObservableList<String> colors = FXCollections.observableArrayList("bleu","black","white","yellow");
		combo_color.setItems(colors);
	}

	public void initialiser() {
		try{
			//changer le type du maincontainer selon le type choisi par l'utilisateur
			//changeMainContainer();

			//data for the composants
			Component parent = new Component("file:resources/fenetre.png", "Fenetre", 0, null,new ArrayList<>());

			//adding the data to the list
			data.add(parent);
			data.add(new Component("file:resources/button.png", "button", 1, parent, false, new ArrayList<>(),new ArrayList<>()));
			data.add(new Component("file:resources/textfield.png", "TextField", 2, parent, false, new ArrayList<>(),new ArrayList<>()));
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
			//lignes.add("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			//lignes.add("<frame LayoutType='"+getLayout()+"' width='full' height='full'>");
			//lignes.add("</frame>");

			doc.setRootElement(new Element("frame"));
	        doc.getRootElement().setAttribute("LayoutType", getLayout());
	        doc.getRootElement().setAttribute("width", "full");
	        doc.getRootElement().setAttribute("height", "full");

	        XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
	        //output xml to console for debugging
	        //xmlOutputter.output(doc, System.out);
	        xmlOutputter.output(doc, new FileOutputStream("output.xml"));

	        refreshCodeFromXml();

			//drag and drop over the frame(treeview) elemnt
			treeView.setCellFactory(new Callback<TreeView<Component>, TreeCell<Component>>() {
	            @Override
	            public TreeCell call(TreeView<Component> param) {
	                return new DnDCell(Controller.this,param);
	            }
	        });

			//la liste des elemnet de type container
			List<Component> lsContainers = new ArrayList<>();
			for(Component c:data){
				if(c.isContainer())
					lsContainers.add(c);
			}



			//input numbers only for width and height
			width.textProperty().addListener(new ChangeListener<String>() {
			    @Override
			    public void changed(ObservableValue<? extends String> observable, String oldValue,
			        String newValue) {
			        if (!newValue.matches("\\d*")) {
			            width.setText(newValue.replaceAll("[^\\d]", ""));
			        }
			    }
			});
			height.textProperty().addListener(new ChangeListener<String>() {
			    @Override
			    public void changed(ObservableValue<? extends String> observable, String oldValue,
			        String newValue) {
			        if (!newValue.matches("\\d*")) {
			            height.setText(newValue.replaceAll("[^\\d]", ""));
			        }
			    }
			});

		}catch(Exception e){
			e.printStackTrace();
		}
	}

	//show les attributs deja attribuer pour cette composant
	private void showAttributes(){
		Component selected = treeView.getSelectionModel().getSelectedItem().getValue();

		id.setText(selected.getId_c());

		if(selected.getLsAttributes().size() > 1){

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

			double x = event.getX();
			double y = event.getY();

			Component c = list.get(0);
			_id++;
			c.setId_c("id"+_id);
			c.getLsAttributes().add(new Attribute("id", "id"+_id));


			panel.getChildren().add(getSelectedNodeType(list, x, y));


			//add this componennt to the listitem
			listItem.add(c);

			//setter le id fixe
			id.setText("id"+_id);

			//adding to the tree
			treeView.getRoot().getChildren().add(new TreeItem<>(c));

			//add the component to file
			addCompoToJdomFile(c);

			//sync the code visible
			refreshCodeFromXml();

			// Data transfer is successful
			dragCompleted = true;
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
		//ArrayList<Attribute> newAttributes = new ArrayList<>();

		if(selected.getLsAttributes().size() == 1){
			selected.getLsAttributes().add(new Attribute("height", height.getText()));
			selected.getLsAttributes().add(new Attribute("width", width.getText()));
			selected.getLsAttributes().add(new Attribute("texte", texte.getText()));

			String editable = "false";
			if(ck_editable.isSelected())
				editable = "true";
			selected.getLsAttributes().add(new Attribute("editable", editable));

			String visible = "false";
			if(ck_visible.isSelected())
				visible = "true";
			selected.getLsAttributes().add(new Attribute("visible", visible));

			selected.getLsAttributes().add(new Attribute("color", combo_color.getValue()));
			selected.getLsAttributes().add(new Attribute("bg", bg.getText()));
		}

		//selected.setLsAttributes(newAttributes);

		//adding attributs to xml code
		editAttributsToXml(id.getText(), selected);

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setContentText("Ok ! Bien Ajouter :) ");
		alert.setHeaderText("Les attributs sonts bien ajouter a "+selected.getName()+" !");
		alert.showAndWait();
	}
	private void editAttributsToXml(String text, Component selected) {
		try{
	        //Get the JDOM document
	        org.jdom2.Document doc = useSAXParser("output.xml");

	        //Get list of component element
	        Element rootElement = doc.getRootElement();
	        List<Element> components = rootElement.getChildren();

	        //loop through to edit every Component element
	        for (Element cp : components) {
	        	String id = cp.getAttributeValue("id");

	            if (id.equals(selected.getId_c())){
	            	System.out.println(id);

	            	for(Attribute t:selected.getLsAttributes())
	            		cp.setAttribute(t.getName(), t.getValue());
	            }
	        }

	        //document is processed and edited successfully, lets save it in new file
	        XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
	        xmlOutputter.output(doc, new FileOutputStream("output.xml"));

	        //refresh
	        refreshCodeFromXml();
    	}catch(Exception e){
    		e.printStackTrace();
    	}
	}

	public void HandleGenerer(){
		try{
			//la liste des element choisis
			for(TreeItem<Component> tr:treeView.getRoot().getChildren()){
				System.out.println(tr.getValue().getName());
			}

			Source xslt = new StreamSource(new File("transform.xsl"));
	        Source text = new StreamSource(new File("output.xml"));
	        TransformerFactory factory = TransformerFactory.newInstance();
	        Transformer transformer = factory.newTransformer(xslt);


	        transformer.transform(text, new StreamResult(new File("output.html")));

	        if (Desktop.isDesktopSupported()) {
	            Desktop.getDesktop().open(new File("output.html"));
	        }
		}catch(Exception e){
			e.printStackTrace();
		}
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
				if(selected.getValue().isContainer()){
					//interdie on doit supprimer tt les sous elts
					Alert at = new Alert(AlertType.ERROR);
					at.setContentText("Erreur ! il faut selectionner element par elemnent avant de supprimer le parent");
					at.setHeaderText("Erreur ! Parent with Childrens !");
					at.showAndWait();
				}else{
					panel.getChildren().remove(treeView.getSelectionModel().getSelectedIndex());
					treeView.getRoot().getChildren().remove(selected);

					//remove element from jdom
					removeCompoFromJdomFile(selected.getValue());
				}

			} else {

			}
		}else{
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText("Erreur ! il faut selectionner un elt du tree avant de suppr");
			alert.setHeaderText("Erreur ! No data selected !");
			alert.showAndWait();
		}
	}


	//lire xml file content to textarea
	void refreshCodeFromXml(){
		try{
			File file = new File("output.xml");
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String text = null;

			codeTxt.setText("");
			while ((text = reader.readLine()) != null) {
				codeTxt.appendText(text+"\n");
			}

			reader.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	//Get JDOM document from DOM Parser
    private void readFile(String fileName){
        try{
        	final String file = "output.xml";
            org.jdom2.Document jdomDoc;

            //we can create JDOM Document from DOM, SAX and STAX Parser Builder classes
            jdomDoc = useDOMParser(fileName);
            Element root = jdomDoc.getRootElement();
            List<Element> empListElements = root.getChildren("Employee");

            /*for (Element empElement : empListElements) {
                    Employee emp = new Employee();
                    emp.setId(Integer.parseInt(empElement.getAttributeValue("id")));
                    emp.setAge(Integer.parseInt(empElement.getChildText("age")));
                    emp.setName(empElement.getChildText("name"));
                    emp.setRole(empElement.getChildText("role"));
                    emp.setGender(empElement.getChildText("gender"));
                    empList.add(emp);
                }
                //lets print Employees list information
                for (Employee emp : empList)
                    System.out.println(emp);*/
        }catch(Exception e){
        	e.printStackTrace();
        }
    }
    //Get JDOM document from DOM Parser
    private static org.jdom2.Document useDOMParser(String fileName)
            throws ParserConfigurationException, SAXException, IOException {
        //creating DOM Document
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        dBuilder = dbFactory.newDocumentBuilder();
        Document doc = (Document) dBuilder.parse(new File(fileName));
        DOMBuilder domBuilder = new DOMBuilder();
        return domBuilder.build((org.w3c.dom.Document) doc);

    }

    private void addCompoToJdomFile(Component c){
    	try{
	        //Get the JDOM document
	        org.jdom2.Document doc = useSAXParser("output.xml");

	        //Get list of component element
	        Element rootElement = doc.getRootElement();
	        List<Element> components = rootElement.getChildren();

	        //loop through to edit every Component element
	        for (Element cp : components) {
	            if (c.getParentCompo().getName() == cp.getName()){

	            }
	        }
	        Element elt = new Element(c.getName());
	        for(Attribute t:c.getLsAttributes())
	        	elt.setAttribute(t.getName(), t.getValue());

	        elt.setText(c.getName());

	        doc.getRootElement().getChildren().add(elt);

	        //document is processed and edited successfully, lets save it in new file
	        XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
	        xmlOutputter.output(doc, new FileOutputStream("output.xml"));
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }

    //remove elemnet from xml
    private void removeCompoFromJdomFile(Component c){
    	try{
	        //Get the JDOM document
	        org.jdom2.Document doc = useSAXParser("output.xml");

	        //Get list of component element
	        Element rootElement = doc.getRootElement();
	        List<Element> components = rootElement.getChildren();

	        //loop through to edit every Component element
	        for (Element cp : components) {
	            if (cp.getAttributeValue("id").equals(c.getId_c())){
	            	rootElement.getChildren().remove(cp);
	            }
	        }

	        //document is processed and edited successfully, lets save it in new file
	        XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
	        xmlOutputter.output(doc, new FileOutputStream("output.xml"));

	        //refresh
	        refreshCodeFromXml();
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }


    //Get JDOM document from SAX Parser
    static org.jdom2.Document useSAXParser(String fileName) throws JDOMException,
            IOException {
        SAXBuilder saxBuilder = new SAXBuilder();
        return saxBuilder.build(new File(fileName));
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
			double x = event.getX();
			double y = event.getY();

			Component c = list.get(0);

			_id++;
			c.setId_c("id"+_id);
			c.getLsAttributes().add(new Attribute("id", "id"+_id));

			//add the component to file
			addCompoToJdomFile(c);

			//sync the code visible
			refreshCodeFromXml();


			//setter le id fixe
			id.setText("id"+_id);

			panel.getChildren().add(getSelectedNodeType(list, x, y));

			//add this componennt to the listitem
			listItem.add(c);

			//adding to the tree
			treeView.getRoot().getChildren().add(new TreeItem<>(c));

			// Data transfer is successful
			dragCompleted = true;
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


	//getters and setters
	public void setLayout(String layout) {
		this.layout = layout;
	}
	public String getLayout() {
		return layout;
	}
}
