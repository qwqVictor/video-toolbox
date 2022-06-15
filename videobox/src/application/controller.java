package application;


import java.io.File;
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
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.DirectoryChooser;
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
	FFBridge Bridge = new FFBridge();
	String tab1OutputFormat = null;
	String tab1Outputcodec = null;
	String tab1OutputTran = null;

	
    @FXML
    private SplitMenuButton CodecButton;

    @FXML
    private SplitMenuButton FormatButton;
    
	@FXML
	private TextField Height;

	@FXML
	private TextField Rate;

    @FXML
	private TextField Width;
	    
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
				Set<String> set=map1.keySet();
				Iterator<String> it=set.iterator();
				while(it.hasNext()){
					Hyperlink link = map1.get(it.next());
					link.setVisible(true);
				}
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
				Set<String> set=map2.keySet();
				Iterator<String> it=set.iterator();
				while(it.hasNext()){
					Hyperlink link = map2.get(it.next());
					link.setVisible(true);
				}
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
				Set<String> set=map3.keySet();
				Iterator<String> it=set.iterator();
				while(it.hasNext()){
					Hyperlink link = map3.get(it.next());
					link.setVisible(true);
				}
				Set<String> mset=map4.keySet();
				Iterator<String> mit=mset.iterator();
				while(mit.hasNext()){
					Hyperlink link = map4.get(mit.next());
					link.setVisible(true);
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
    	//try {
			Stage primaryStage = new Stage();
			DirectoryChooser file = new DirectoryChooser();
			file.setTitle("Save File");
			File dir = file.showDialog(primaryStage);
			System.out.print(Width.getText());
			if (dir != null) {
				Integer width = Width.getText().length() == 0 ? null : Integer.parseInt(Width.getText());
				Integer height = Height.getText().length() == 0 ? null : Integer.parseInt(Height.getText());
				Integer bitrate = Rate.getText().length() == 0 ? null : Integer.parseInt(Rate.getText());
				Bridge.transform(data, dir.toString(), this.tab1OutputFormat, this.tab1Outputcodec, width, height, bitrate);
			}
//		}catch(Exception e){	
//			Alert alert = new Alert(Alert.AlertType.INFORMATION);
//			alert.setContentText("The video tool box is error");
//			alert.show();
//		}  	
    }
    
    @FXML
    void Outputevent2(MouseEvent event) {
    	try {
			Stage primaryStage = new Stage();
			DirectoryChooser file = new DirectoryChooser();
			file.setTitle("Save File");
			file.showDialog(primaryStage);
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
    void deletelabel1(ActionEvent event) {
    	Hyperlink source = (Hyperlink) event.getSource();
    	Integer id = Integer.parseInt(source.getId().split("hyperlink")[1]);
    	videolist1.getItems().remove(id.intValue());
    	Hyperlink[] hyperlinklist = {hyperlink0,hyperlink1,hyperlink2,hyperlink3,hyperlink4,hyperlink5,hyperlink6,hyperlink7,hyperlink8,hyperlink9};    	
    	hyperlinklist[videolist1.getItems().size()].setVisible(false);
    }
    
    @FXML
    void deletelabel2(ActionEvent event) {
    	Hyperlink source = (Hyperlink) event.getSource();
    	Integer id = Integer.parseInt(source.getId().split("hyperlink")[1]);
    	videolist2.getItems().remove(id.intValue()- 10);
    	Hyperlink[] hyperlinklist = {hyperlink10,hyperlink11,hyperlink12,hyperlink13,hyperlink14,hyperlink15,hyperlink16,hyperlink17,hyperlink18,hyperlink19};    	
    	hyperlinklist[videolist2.getItems().size()].setVisible(false);
    }
    
    @FXML
    void deletelabel3(ActionEvent event) {
    	Hyperlink source = (Hyperlink) event.getSource();
    	Integer id = Integer.parseInt(source.getId().split("hyperlink")[1]);
    	videolist3.getItems().remove(id.intValue()-20);
    	Hyperlink[] hyperlinklist = {hyperlink20,hyperlink21,hyperlink22,hyperlink23,hyperlink24,hyperlink25,hyperlink26,hyperlink27,hyperlink28,hyperlink29};    	
    	hyperlinklist[videolist3.getItems().size()].setVisible(false);
    }
    
    @FXML
    void deletelabel4(ActionEvent event) {
    	Hyperlink source = (Hyperlink) event.getSource();
    	Integer id = Integer.parseInt(source.getId().split("hyperlink")[1]);
    	musiclist.getItems().remove(id.intValue()-30);
    	Hyperlink[] hyperlinklist = {hyperlink30,hyperlink31,hyperlink32,hyperlink33,hyperlink34,hyperlink35,hyperlink36,hyperlink37,hyperlink38,hyperlink39};    	
    	hyperlinklist[musiclist.getItems().size()].setVisible(false);
    }
    
    @FXML
    void OnMouseEntered1(MouseEvent event) {
    	videolist1.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    	videolist1.setEditable(true);
		videolist1.setCellFactory(TextFieldListCell.forListView());
    }
    
    @FXML
    void OnMouseEntered2(MouseEvent event) {
    	videolist2.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    	videolist2.setEditable(true);
		videolist2.setCellFactory(TextFieldListCell.forListView());	
    }

    @FXML
    void OnMouseEntered3(MouseEvent event) {
    	videolist3.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    	videolist3.setEditable(true);
		videolist3.setCellFactory(TextFieldListCell.forListView());	
    }
    
    @FXML
    void OnMouseEntered4(MouseEvent event) {
    	musiclist.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    	musiclist.setEditable(true);
    	musiclist.setCellFactory(TextFieldListCell.forListView());	
    }
    

    @FXML
    void SelectCodec(ActionEvent event) {
    	Map<String, String> codecs = new HashMap<String, String>();
    	codecs.put("×Ô¶¯", null);
    	codecs.put("H.264", "h264");
    	codecs.put("H.265", "hevc");
    	MenuItem item = (MenuItem) event.getSource();
    	CodecButton.setText(item.getText());
    	this.tab1Outputcodec = codecs.get(item.getText());
    }

    @FXML
    void SelectFormat(ActionEvent event) {
    	Map<String, String> videoFormats = new HashMap<String, String>();
    	videoFormats.put("MPEG-4", "mp4");
    	videoFormats.put("QuickTime MOV", "mov");
    	videoFormats.put("FLV", "flv");
    	videoFormats.put("MKV", "mkv");
    	videoFormats.put("TS", "ts");
    	MenuItem item = (MenuItem) event.getSource();
    	FormatButton.setText(item.getText());
    	this.tab1OutputFormat = videoFormats.get(item.getText());
    }

    @FXML
    void SelectTransition(ActionEvent event) {
    	
    }



    
}

