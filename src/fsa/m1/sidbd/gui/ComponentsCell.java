package fsa.m1.sidbd.gui;

import java.io.IOException;

import fsa.m1.sidbd.model.Component;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

public class ComponentsCell implements Callback<ListView<Component>, ListCell<Component>> {

	// only one global event handler
	private EventHandler<DragEvent> oneClickHandler;


	public ComponentsCell(){
	    oneClickHandler = new EventHandler<DragEvent>() {
	        @Override
	        public void handle(DragEvent event) {
	            Parent p = (Parent) event.getSource();
	            //  do what you want to do with data.
	            System.out.println((Component)p.getUserData());
	        }
	    };
	}

	@Override
	public ListCell<Component> call(ListView<Component> param) {
	    return new DemoListCell(oneClickHandler);
	}

	public static final class DemoListCell extends ListCell<Component> {

		private EventHandler<DragEvent> clickHandler;

	    /**
	     * This is ListView item root node.
	     */
	    private Parent itemRoot;

	    private Label label_AppName;
	    private ImageView imgv_AppIcon;

	    DemoListCell(EventHandler<DragEvent> clickHandler) {
	        this.clickHandler = clickHandler;
	    }

	    @Override
	    protected void updateItem(Component app, boolean empty) {
	        super.updateItem(app, empty);
	        if (app == null) {
	            setText(null);
	            setGraphic(null);
	            return;
	        }
	        if (null == itemRoot) {
	            try {
	                itemRoot = FXMLLoader.load(getClass().getResource(("appList_item.fxml")));
	            } catch (IOException e) {
	                throw new RuntimeException(e);
	            }
	            label_AppName = (Label) itemRoot.lookup("#item_Label_AppName");
	            imgv_AppIcon = (ImageView) itemRoot.lookup("#item_ImageView_AppIcon");
	            itemRoot.setOnDragDone(clickHandler);
	        }
	        //  set user data. like android's setTag(Object).
	        itemRoot.setUserData(app);
	        label_AppName.setText(app.getName());
	        imgv_AppIcon.setImage(new Image(app.getImageUrl()));

	        setGraphic(itemRoot);
	    }
	}

}
