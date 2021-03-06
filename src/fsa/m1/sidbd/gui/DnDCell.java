package fsa.m1.sidbd.gui;

import java.io.FileOutputStream;
import java.util.List;

import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

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
                     if (valueToMove != item) {
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

	                 //edit xml file
	                 updateXmlFile(newParent.getValue(), itemToMove.getValue());
                 }
             }

			private void updateXmlFile(Component parent, Component child) {
				try{
			        //Get the JDOM document
			        org.jdom2.Document doc = Controller.useSAXParser("output.xml");

			        //Get list of component element
			        Element rootElement = doc.getRootElement();
			        List<Element> components = rootElement.getChildren();
			 
			        Element childElt = null;
			        for (Element cp : components) {
			        	String id = cp.getAttributeValue("id");

			            if (id.equals(child.getId_c())){
			            	System.out.println("child : "+id);

			            	childElt = cp;
			            	//supprimer le courant
			            	rootElement.getChildren().remove(cp);
			            }
			        }
			        //loop through to edit every Component element
			        for (Element cp : components) {
			        	String id = cp.getAttributeValue("id");

			            if (id.equals(parent.getId_c())){
			            	System.out.println("parent : "+id);

			            	cp.addContent(childElt);
			            }
			        }

			        //document is processed and edited successfully, lets save it in new file
			        XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
			        xmlOutputter.output(doc, new FileOutputStream("output.xml"));

			        //refresh
			        controller.refreshCodeFromXml();
		    	}catch(Exception e){
		    		e.printStackTrace();
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
