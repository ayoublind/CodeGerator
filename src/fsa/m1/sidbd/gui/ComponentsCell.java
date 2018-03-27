package fsa.m1.sidbd.gui;

import fsa.m1.sidbd.model.Component;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class ComponentsCell extends ListCell<Component>{
    @Override
    protected void updateItem(Component c, boolean bln) {
        if (c != null){
            setGraphic(getComponentCell(c));  // ***set the content of the graphic
        }
    }
    private Node getComponentCell(Component info) {
        HBox node = new HBox();
        node.setSpacing(10);

        Image image = new Image(info.getImageUrl(), 30, 30, true, false);
        ImageView imageView = new ImageView(image);
        Text name = new Text(info.getName());
        //name.setWrappingWidth(100);

        node.getChildren().addAll(imageView, name);

        return node;
    }
}
