package fsa.m1.sidbd.gui;

import java.util.List;

import fsa.m1.sidbd.model.Attribute;
import fsa.m1.sidbd.model.Component;
import javafx.event.EventHandler;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;

public class DnDCell extends TreeCell<Component>{

	 private TreeView<Component> parentTree;
	 private Component item;

	 private Controller controller;

     public DnDCell(Controller ctn, final TreeView<Component> parentTree) {
         this.setParentTree(parentTree);
         this.controller = ctn;

         // ON SOURCE NODE.
         setOnDragDetected(new EventHandler<MouseEvent>() {
             @Override
             public void handle(MouseEvent event) {
                 //System.out.println("Drag detected on " + item);
                 if (item == null) {
                     return;
                 }
                 Dragboard dragBoard = startDragAndDrop(TransferMode.MOVE);
                 ClipboardContent content = new ClipboardContent();
                 content.put(DataFormat.PLAIN_TEXT, item.toString());
                 dragBoard.setContent(content);
                 event.consume();
             }
         });
         setOnDragDone(new EventHandler<DragEvent>() {
             @Override
             public void handle(DragEvent dragEvent) {
                 //System.out.println("Drag done on " + item);
                 dragEvent.consume();
             }
         });
         setOnDragOver(new EventHandler<DragEvent>() {
             @Override
             public void handle(DragEvent dragEvent) {
                 //System.out.println("Drag over on " + item);
                 if (dragEvent.getDragboard().hasString()) {
                     Component valueToMove = parentTree.getSelectionModel().getSelectedItem().getValue();
                     if (valueToMove != item && !valueToMove.isContainer()) {
                         // We accept the transfert !!!!!
                         dragEvent.acceptTransferModes(TransferMode.MOVE);
                     }
                 }
                 dragEvent.consume();
             }
         });
         setOnDragDropped(new EventHandler<DragEvent>() {
             @Override
             public void handle(DragEvent dragEvent) {
                 //System.out.println("Drag dropped on " + item);
                 Component valueToMove = parentTree.getSelectionModel().getSelectedItem().getValue();
                 TreeItem<Component> itemToMove = search(parentTree.getRoot(), valueToMove);
                 TreeItem<Component> newParent = search(parentTree.getRoot(), item);
                 if(newParent.getValue().isContainer()){
	                 // Remove from former parent.
	                 itemToMove.getParent().getChildren().remove(itemToMove);
	                 // Add to new parent.
	                 newParent.getChildren().add(itemToMove);
	                 newParent.setExpanded(true);
	                 dragEvent.consume();

	                 //add it to text

	                 newParent.getValue().getChildrens().add(valueToMove);
	                 //fils
	                 String idParent = "id='"+newParent.getValue().getId_c()+"'";
	                 //String idFils = "id='"+itemToMove.getValue().getId_c()+"'";

	                 int index = 0;



	                 List<String> code = controller.lignes;

	                 int i = 0;
	                 /*String filsTag = "";
	                 for (String e:code){
	                	 if(e.contains(idFils)){
	                		 System.out.println("\n fils - "+e);
	                		 filsTag = e;
	                		 //detete that from lignes
	                		 i = code.indexOf(e);

	                	 }
	                 }*/
	                 code.remove(i);

	                 String newParentTag = "";

	                 //adding the fils tag to parrent
	                 for(String a:code){
	                	 if (a.contains(idParent)){
	                		 System.out.println("\n parent - "+a);

	                		 String baliseName = a.split(" ")[0];
	     					 //les attributs
	     					 String attributs = "";
	     					 for(Attribute atr:newParent.getValue().getLsAttributes())
	     						 attributs = attributs + " " +atr.getName()+"='"+atr.getValue()+"'";

	     					 newParentTag = newParentTag + baliseName+" "+attributs+"> ";
	     					 String childs = "";

	     					 for(Component c:newParent.getValue().getChildrens()){
	     						 String attrs = "";
	     						 for(Attribute atr:c.getLsAttributes())
		     						 attrs = attrs + " " +atr.getName()+"='"+atr.getValue()+"'";

	     						 childs = childs+"\n\t\t<"+c.getName()+" "+attrs+"> </"+c.getName()+">";
	     					 }

	     					 newParentTag = newParentTag + "\t\t"+childs+"\n\t</"+baliseName.substring(1)+">";

	                		 /*String tb[] = a.split(" ");
	                		 String re = tb[0];

	                		 String re2 = tb[1];

	                		 System.out.println("re : "+re + " " +re2 +">");
	                		 System.out.println(filsTag);
	                		 System.out.println("</"+re.substring(1)+">");

	     					 String oldChilds = "";





	                		 newParentTag = baliseName + " " +idParent+">\n"
	                		 		+ "\t\t" + filsTag + "\n"
	                		 				+ "\t</"+baliseName.substring(1)+">";*/
	     					 index = code.indexOf(a);
	                		 code.set(index, newParentTag);
	                	 }
	                 }

	                 controller.setLignes(code);
	                 controller.updateText();
                 }
             }
         });
     }

     private TreeItem<Component> search(final TreeItem<Component> currentNode, final Component valueToSearch) {
         TreeItem<Component> result = null;
         if (currentNode.getValue() == valueToSearch) {
             result = currentNode;
         } else if (!currentNode.isLeaf()) {
             for (TreeItem<Component> child : currentNode.getChildren()) {
                 result = search(child, valueToSearch);
                 if (result != null) {
                     break;
                 }
             }
         }
         return result;
     }

     @Override
     protected void updateItem(Component item, boolean empty) {
         super.updateItem(item, empty);
         this.item = item;
         String text = (item == null) ? null : item.toString();
         setText(text);
     }

     //getters and setters
	public TreeView<Component> getParentTree() {
		return parentTree;
	}

	public void setParentTree(TreeView<Component> parentTree) {
		this.parentTree = parentTree;
	}
	public void setController(Controller controller) {
		this.controller = controller;
	}
	public Controller getController() {
		return controller;
	}
}
