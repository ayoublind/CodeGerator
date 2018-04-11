package fsa.m1.sidbd.gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import fsa.m1.sidbd.model.Component;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuBar;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;

public class Controller implements Initializable{

	@FXML private ListView<Component> list_components;
	@FXML private ListView<String> ls_attribues;
	@FXML private Button btn_supprimer, btn_generer, btn_attribuer;
	@FXML private Pane panel;
	@FXML private MenuBar menu_bar;

	// Set the Custom Data Format
	static final DataFormat COMPONENT_LIST = new DataFormat("ComponentList");


	ObservableList<Component> data = FXCollections.observableArrayList();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

		data.add(new Component("file:resources/icon.png", "button"));
		data.add(new Component("file:resources/delete.png", "text field"));
		data.add(new Component("file:resources/dart-board.png", "label"));
		data.add(new Component("file:resources/play-button.png", "TextArea"));


		list_components.setCellFactory(new ComponentsCell());
		list_components.setItems(data);
		list_components.getSelectionModel().selectedItemProperty().addListener(e->{
			Component c = list_components.getSelectionModel().getSelectedItem();
			System.out.println(c.getName());
		});

		// Add mouse event handlers for the source
		list_components.setOnDragDetected(new EventHandler <MouseEvent>(){
			public void handle(MouseEvent event){
				System.out.println("Event on Source: drag detected");
				dragDetected(event, list_components);
			}
		});

		list_components.setOnDragOver(new EventHandler <DragEvent>() {
			public void handle(DragEvent event)
			{
				System.out.println("Event on Source: drag over");
				dragOver(event, list_components);
			}
		});

		list_components.setOnDragDropped(new EventHandler <DragEvent>() {
			public void handle(DragEvent event)
			{
				System.out.println("Event on Source: drag dropped");
				dragDropped(event, list_components);
			}
		});

		list_components.setOnDragDone(new EventHandler <DragEvent>() {
			public void handle(DragEvent event)
			{
				System.out.println("Event on Source: drag done");
				dragDone(event, list_components);
			}
		});

		// Add mouse event handlers for the target

		panel.setOnDragOver(new EventHandler <DragEvent>()
		{
			public void handle(DragEvent event)
			{
				System.out.println("Event on Target: drag over");
				panelDragOver(event, panel);
			}
		});

		panel.setOnDragDropped(new EventHandler <DragEvent>()
		{
			public void handle(DragEvent event)
			{
				System.out.println("Event on Target: drag dropped");
				panelDragDropped(event, panel);
			}
		});

		panel.setOnDragDone(new EventHandler <DragEvent>()
		{
			public void handle(DragEvent event)
			{
				System.out.println("Event on Target: drag done");
				panelDragDone(event, panel);
			}
		});
	}

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
			Button b = new Button("Name");

			b.setLayoutX(x);
			b.setLayoutY(y);

			panel.getChildren().add(b);
			// Data transfer is successful
			dragCompleted = true;
		}

		// Data transfer is not successful
		event.setDropCompleted(dragCompleted);
		event.consume();
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

	}
	public void HandleGenerer(){

	}
	public void HandleSupprimer(){

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
			//ArrayList<Component> list = (ArrayList<Component>)dragboard.getContent(COMPONENT_LIST);
			//list_components.getItems().addAll(list);
			double x = event.getX();
			double y = event.getY();
			Button b = new Button("Name");

			b.setLayoutX(x);
			b.setLayoutY(y);

			panel.getChildren().add(b);
			// Data transfer is successful
			dragCompleted = true;
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
}
