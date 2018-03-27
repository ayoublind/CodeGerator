package fsa.m1.sidbd.gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class Controller implements Initializable{
	@FXML private ListView<Control> list;
	ObservableList<Control> data = FXCollections.observableArrayList();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

		data.add(new Button("button"));
		data.add(new TextField("text field"));
		data.add(new Label("label"));
		data.add(new ListView());


		list.setItems(data);
	}

}
