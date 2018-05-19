package fsa.m1.sidbd.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceDialog;
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

	        //on load the app we gone ask the user to choice the main layout type
	        primaryStage.setOnShowing(e->{
	        	showLayoutChoiceBox(controller);
	        });
	        primaryStage.show();


		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void showLayoutChoiceBox(Controller controller) {
		List<String> choices = new ArrayList<>();
		//conteneur
		choices.add("Frame");
		choices.add("BorderLayout");
		choices.add("LinearLayout(Vertical)");
		choices.add("LinearLayout(Horizental)");


		ChoiceDialog<String> dialog = new ChoiceDialog<>(choices.get(0), choices);
		// Get the Stage.
		Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();

		// Add a custom icon.
		stage.getIcons().add(new Image("file:resources/logo.png"));
		dialog.setTitle("Choix de Layout(Main)");
		dialog.setHeaderText("Vous devez choisir un conteneur avant d'utiliser l'application ");
		dialog.setContentText("Choisissez votre mise en page :");

		// get the value
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()){
		    System.out.println("Votre Choix: " + result.get());
		    controller.setLayout(result.get());
		    
		    controller.changeMainContainer();
		    controller.initialiser();
		    controller.infos.setText("Main Container : "+result.get());
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
