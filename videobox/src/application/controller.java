package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

public class controller {
	public static final ObservableList<String> data = FXCollections.observableArrayList();
	public static final ObservableList<String> data2 = FXCollections.observableArrayList();
	public static final ObservableList<String> video3 = FXCollections.observableArrayList();
	public static final ObservableList<String> music = FXCollections.observableArrayList();
	@FXML
    private ListView<String> musiclist;

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
			Stage primaryStage = new Stage();
			FileChooser file = new FileChooser();
			file.setTitle("Open File");
			List<File> list = file.showOpenMultipleDialog(primaryStage); 
			if(list!=null) {
				for (int i = 0; i < list.size(); i++) {
					data.add(list.get(i).toString());
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
			Stage primaryStage = new Stage();
			FileChooser file = new FileChooser();
			file.setTitle("Open File");
			List<File> list = file.showOpenMultipleDialog(primaryStage); 
			if(list!=null) {
				for (int i = 0; i < list.size(); i++) {
					data2.add(list.get(i).toString());
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
    		System.out.print("1");
			Stage primaryStage = new Stage();
			FileChooser file = new FileChooser();
			file.setTitle("Open File");
			List<File> list = file.showOpenMultipleDialog(primaryStage); 
			if(list!=null) {
				
				for (int i = 0; i < list.size(); i++) {
					String[] strs = list.get(i).toString().split("\\.");
					if(strs[strs.length-1].equals("mp3")) {
						music.add(list.get(i).toString());
					}else {
						video3.add(list.get(i).toString());
					}
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
    void Outputevent(MouseEvent event) {
    	try {
			Stage primaryStage = new Stage();
			FileChooser file = new FileChooser();
			file.setTitle("Save File");
			File file1 = file.showSaveDialog(primaryStage);
		}catch(Exception e){	
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setContentText("The video tool box is error");
			alert.show();
		}  	
    }
}

