package fsa.m1.sidbd.gui;

import java.net.URL;
import java.util.ResourceBundle;

import fsa.m1.sidbd.model.Component;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.Pane;

public class Controller implements Initializable{

	@FXML private ListView<Component> list_components;
	@FXML private ListView ls_attribues;
	@FXML private Button btn_supprimer, btn_generer, btn_attribuer;
	@FXML private Pane panel;
	@FXML private MenuBar menu_bar;


	ObservableList<Component> data = FXCollections.observableArrayList();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

		data.add(new Component("file:resources/icon.png", "button"));
		data.add(new Component("file:resources/delete.png", "text field"));
		data.add(new Component("file:resources/dart-board.png", "label"));
		data.add(new Component("file:resources/play-button.png", "TextArea"));

		list_components.setDisable(false);

		list_components.setItems(data);
		list_components.setCellFactory(p -> new ComponentsCell());
	}
	//les handllers : evenements pour manipuler les buttons
	public void HandleAttribuer(){

	}
	public void HandleGenerer(){

	}
	public void HandleSupprimer(){

	}
}
