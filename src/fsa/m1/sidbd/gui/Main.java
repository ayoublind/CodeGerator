package fsa.m1.sidbd.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader=new FXMLLoader(getClass().getResource("MainAppView.fxml"));
			BorderPane root = (BorderPane) loader.load();
			Controller controllerEmp = loader.getController();

	        primaryStage.setTitle("Projet IDM");
	        primaryStage.setScene(new Scene(root));
	        primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
