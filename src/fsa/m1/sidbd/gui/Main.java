package fsa.m1.sidbd.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader=new FXMLLoader(getClass().getResource("MainAppView.fxml"));
			BorderPane root = (BorderPane) loader.load();
			Controller controller = loader.getController();

	        primaryStage.setTitle("Projet IDM");
	        primaryStage.setScene(new Scene(root));

	        //lorsque on click qq part dans la fenetre on doit fermer la liste des aatribute
	        for(Node n:root.getChildren()){
	        	n.setOnMouseClicked(e->{
	        		controller.desactivateAttributes(true);
	        	});
	        }

	        primaryStage.getIcons().add(new Image("file:resources/logo.png"));
	        primaryStage.show();


		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
