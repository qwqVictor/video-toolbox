package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tab;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

public class controller {
	public static final ObservableList<String> data = FXCollections.observableArrayList();
	public static final ObservableList<String> data2 = FXCollections.observableArrayList();
	public static final ObservableList<String> video3 = FXCollections.observableArrayList();
	public static final ObservableList<String> music = FXCollections.observableArrayList();
	Map <String,Hyperlink> map1=new HashMap<>();
	Map <String,Hyperlink> map2=new HashMap<>();
	Map <String,Hyperlink> map3=new HashMap<>();
	Map <String,Hyperlink> map4=new HashMap<>();
	

	@FXML
    private ListView<String> musiclist;
	
    @FXML
    private Hyperlink hyperlink0;

    @FXML
    private Hyperlink hyperlink1;

    @FXML
    private Hyperlink hyperlink10;

    @FXML
    private Hyperlink hyperlink11;

    @FXML
    private Hyperlink hyperlink12;

    @FXML
    private Hyperlink hyperlink13;

    @FXML
    private Hyperlink hyperlink14;

    @FXML
    private Hyperlink hyperlink15;

    @FXML
    private Hyperlink hyperlink16;

    @FXML
    private Hyperlink hyperlink17;

    @FXML
    private Hyperlink hyperlink18;

    @FXML
    private Hyperlink hyperlink19;

    @FXML
    private Hyperlink hyperlink2;

    @FXML
    private Hyperlink hyperlink20;

    @FXML
    private Hyperlink hyperlink21;

    @FXML
    private Hyperlink hyperlink22;

    @FXML
    private Hyperlink hyperlink23;

    @FXML
    private Hyperlink hyperlink24;

    @FXML
    private Hyperlink hyperlink25;

    @FXML
    private Hyperlink hyperlink26;

    @FXML
    private Hyperlink hyperlink27;

    @FXML
    private Hyperlink hyperlink28;

    @FXML
    private Hyperlink hyperlink29;

    @FXML
    private Hyperlink hyperlink3;

    @FXML
    private Hyperlink hyperlink30;

    @FXML
    private Hyperlink hyperlink31;

    @FXML
    private Hyperlink hyperlink32;

    @FXML
    private Hyperlink hyperlink33;

    @FXML
    private Hyperlink hyperlink34;

    @FXML
    private Hyperlink hyperlink35;

    @FXML
    private Hyperlink hyperlink36;

    @FXML
    private Hyperlink hyperlink37;

    @FXML
    private Hyperlink hyperlink38;

    @FXML
    private Hyperlink hyperlink39;

    @FXML
    private Hyperlink hyperlink4;

    @FXML
    private Hyperlink hyperlink5;

    @FXML
    private Hyperlink hyperlink6;

    @FXML
    private Hyperlink hyperlink7;

    @FXML
    private Hyperlink hyperlink8;

    @FXML
    private Hyperlink hyperlink9;
    
    @FXML
    private Hyperlink hyperlink;
    
    @FXML
    private Tab tab1;

    @FXML
    private Tab tab2;

    @FXML
    private Tab tab3;

    @FXML
    private ListView<String> videolist1;

    @FXML
    private ListView<String> videolist2;

    @FXML
    private ListView<String> videolist3;
    
    @FXML
    private StackPane stack1;

    @FXML
    private StackPane stack2;

    @FXML
    private StackPane stack3;

    @FXML
    private StackPane stack4;
    
	@FXML
    void OnMouseClick1(MouseEvent event) {
    	try {
    		Hyperlink[] linklist = {hyperlink0,hyperlink1,hyperlink2,hyperlink3,hyperlink4,hyperlink5,hyperlink6,hyperlink7,hyperlink8,hyperlink9};
    		Stage primaryStage = new Stage();
			FileChooser file = new FileChooser();
			file.setTitle("Open File");
			List<File> list = file.showOpenMultipleDialog(primaryStage); 
			if(list!=null) {	
				for (int i = 0; i < list.size(); i++) {
					if (!data.contains(list.get(i).toString()))
						data.add(list.get(i).toString());
				}
				for(int i = 0; i < data.size(); i++) {
					map1.put(data.get(i),linklist[i]);
				}
				videolist1.setItems(data);
			}
		}catch(Exception e){	
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setContentText("The video tool box is error");
			alert.show();
		}  		
    }

    @FXML
    void OnMouseClick2(MouseEvent event) {
    	try {
    		Hyperlink[] hyperlinklist = {hyperlink10,hyperlink11,hyperlink12,hyperlink13,hyperlink14,hyperlink15,hyperlink16,hyperlink17,hyperlink18,hyperlink19};
    		Stage primaryStage = new Stage();
			FileChooser file = new FileChooser();
			file.setTitle("Open File");
			List<File> list = file.showOpenMultipleDialog(primaryStage); 
			if(list!=null) {
				for (int i = 0; i < list.size(); i++) {
					if (!data2.contains(list.get(i).toString()))
						data2.add(list.get(i).toString());
				}
				for(int i = 0; i < data2.size(); i++) {
					map2.put(data2.get(i),hyperlinklist[i]);
				}
				videolist2.setItems(data2);
			} 	
		}catch(Exception e){	
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setContentText("The video tool box is error");
			alert.show();
		}  	
    }

    @FXML
    void OnMouseClick3(MouseEvent event) {
    	try {
    		Hyperlink[] mulist = {hyperlink30,hyperlink31,hyperlink32,hyperlink33,hyperlink34,hyperlink35,hyperlink36,hyperlink37,hyperlink38,hyperlink39};
    		Hyperlink[] videolist = {hyperlink20,hyperlink21,hyperlink22,hyperlink23,hyperlink24,hyperlink25,hyperlink26,hyperlink27,hyperlink28,hyperlink29};
			Stage primaryStage = new Stage();
			FileChooser file = new FileChooser();
			file.setTitle("Open File");
			List<File> list = file.showOpenMultipleDialog(primaryStage); 
			if(list!=null) {				
				for (int i = 0; i < list.size(); i++) {
					String[] strs = list.get(i).toString().split("\\.");
					if(strs[strs.length-1].equals("mp3")) {
						if (!music.contains(list.get(i).toString()))
							music.add(list.get(i).toString());				
					}else {
						if (!video3.contains(list.get(i).toString()))
							video3.add(list.get(i).toString());		
					}
				}
				for(int i = 0; i < music.size(); i++) {
					map4.put(music.get(i),mulist[i]);
				}
				
				for(int i = 0; i < video3.size(); i++) {
					map3.put(video3.get(i),videolist[i]);
				}
				if(video3!=null) {
					videolist3.setItems(video3);

				}
				if(music!=null) {
					musiclist.setItems(music);
				}
				
			}
		}catch(Exception e){	
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setContentText("The video tool box is error");
			alert.show();
		}  	  	
    }

    @FXML
    void Outputevent1(MouseEvent event) {
    	try {
			Stage primaryStage = new Stage();
			FileChooser file = new FileChooser();
			file.setTitle("Save File");
			file.showSaveDialog(primaryStage);
		}catch(Exception e){	
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setContentText("The video tool box is error");
			alert.show();
		}  	
    }
    
    @FXML
    void Outputevent2(MouseEvent event) {
    	try {
			Stage primaryStage = new Stage();
			FileChooser file = new FileChooser();
			file.setTitle("Save File");
			file.showSaveDialog(primaryStage);
		}catch(Exception e){	
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setContentText("The video tool box is error");
			alert.show();
		}  	
    }
    
    @FXML
    void Outputevent3(MouseEvent event) {
    	try {
			Stage primaryStage = new Stage();
			FileChooser file = new FileChooser();
			file.setTitle("Save File");
			file.showSaveDialog(primaryStage);
		}catch(Exception e){	
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setContentText("The video tool box is error");
			alert.show();
		}  	
    }
        
    @FXML
    void deletelabel(ActionEvent event) {
    	Hyperlink source = (Hyperlink) event.getSource();
    	Integer id = Integer.parseInt(source.getId().split("hyperlink")[1]);
    	videolist1.getItems().remove(id.intValue());
    	Hyperlink[] hyperlinklist = {hyperlink0,hyperlink1,hyperlink2,hyperlink3,hyperlink4,hyperlink5,hyperlink6,hyperlink7,hyperlink8,hyperlink9};    	
    	hyperlinklist[videolist1.getItems().size()].setVisible(false);
    	System.out.print("1");
    }
    
    @FXML
    void OnMouseEntered1(MouseEvent event) {
    	videolist1.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    	videolist1.setEditable(true);
		videolist1.setCellFactory(TextFieldListCell.forListView());	
		Set<String> set=map1.keySet();
		Iterator<String> it=set.iterator();
		while(it.hasNext()){
			Hyperlink link = map1.get(it.next());
			link.setVisible(true);
		}
    }
    
    @FXML
    void OnMouseEntered2(MouseEvent event) {
    	videolist2.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    	videolist2.setEditable(true);
		videolist2.setCellFactory(TextFieldListCell.forListView());	
		Set<String> set=map2.keySet();
		Iterator<String> it=set.iterator();
		while(it.hasNext()){
			Hyperlink link = map2.get(it.next());
			link.setVisible(true);
		}
    }

    @FXML
    void OnMouseEntered3(MouseEvent event) {
    	videolist3.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    	videolist3.setEditable(true);
		videolist3.setCellFactory(TextFieldListCell.forListView());	
		Set<String> set=map3.keySet();
		Iterator<String> it=set.iterator();
		while(it.hasNext()){
			Hyperlink link = map3.get(it.next());
			link.setVisible(true);
		}
    }
    
    @FXML
    void OnMouseEntered4(MouseEvent event) {
    	musiclist.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    	musiclist.setEditable(true);
    	musiclist.setCellFactory(TextFieldListCell.forListView());	
		Set<String> set=map4.keySet();
		Iterator<String> it=set.iterator();
		while(it.hasNext()){
			Hyperlink link = map4.get(it.next());
			link.setVisible(true);
		}
    }
 
    
    @FXML
    void OnMouseExited(MouseEvent event) {
    	Set<String> set1=map1.keySet();
		Iterator<String> it1=set1.iterator();
		while(it1.hasNext()){
			Hyperlink link1 = map1.get(it1.next());
			link1.setVisible(false);
		}
		
		Set<String> set2=map2.keySet();
		Iterator<String> it2=set2.iterator();
		while(it2.hasNext()){
			Hyperlink link2 = map2.get(it2.next());
			link2.setVisible(false);
		}
		
		Set<String> set3=map3.keySet();
		Iterator<String> it3=set3.iterator();
		while(it3.hasNext()){
			Hyperlink link3 = map3.get(it3.next());
			link3.setVisible(false);
		}
		
		Set<String> set4=map4.keySet();
		Iterator<String> it4=set4.iterator();
		while(it4.hasNext()){
			Hyperlink link4 = map4.get(it4.next());
			link4.setVisible(false);
		}
    }

}

