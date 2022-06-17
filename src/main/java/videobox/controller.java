package videobox;

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
	Map<String, Hyperlink> map1 = new HashMap<>();
	Map<String, Hyperlink> map2 = new HashMap<>();
	Map<String, Hyperlink> map3 = new HashMap<>();
	Map<String, Hyperlink> map4 = new HashMap<>();
	FFBridge Bridge = new FFBridge();
	String tab1OutputFormat = null;
	String tab1Outputcodec = null;
	String tab1OutputTran = null;

	@FXML
	private SplitMenuButton CatButton;

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

	// 功能模块一：选择视频转码所需要的文件
	// 点击导入视频文件，实现对导入的视频进行显示操作
	// 通过Listview来显示文件信息
	@FXML
	void OnMouseClick1(MouseEvent event) {
		try {
			Hyperlink[] linklist = { hyperlink0, hyperlink1, hyperlink2, hyperlink3, hyperlink4, hyperlink5, hyperlink6,
					hyperlink7, hyperlink8, hyperlink9 };
			Stage primaryStage = new Stage();
			FileChooser file = new FileChooser();
			file.setTitle("Open File");
			// 打开文件夹，实现对文件的多选功能,将文件路径储存在list
			List<File> list = file.showOpenMultipleDialog(primaryStage);
			if (list != null) {
				// 将储存文件路径的list依次添加到ObservableList中
				for (int i = 0; i < list.size(); i++) {
					if (!data.contains(list.get(i).toString()))
						data.add(list.get(i).toString());
				}
				// 将ObservableList中的文件路径与删除链接存储为hasmap,形成键值对
				for (int i = 0; i < data.size(); i++) {
					map1.put(data.get(i), linklist[i]);
				}
				videolist1.setItems(data);
				// 设置导入文件显示时，删除链接按钮
				Set<String> set = map1.keySet();
				Iterator<String> it = set.iterator();
				while (it.hasNext()) {
					Hyperlink link = map1.get(it.next());
					link.setVisible(true);
				}
			}
		} catch (Exception e) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setContentText(e.getMessage());
			alert.show();
		}
	}

	// 功能模块二：选择所合并的视频（可选择转场）文件
	// 点击导入视频文件，实现对导入的视频进行显示操作
	// 通过Listview来显示文件信息
	@FXML
	void OnMouseClick2(MouseEvent event) {
		try {
			Hyperlink[] hyperlinklist = { hyperlink10, hyperlink11, hyperlink12, hyperlink13, hyperlink14, hyperlink15,
					hyperlink16, hyperlink17, hyperlink18, hyperlink19 };
			Stage primaryStage = new Stage();
			FileChooser file = new FileChooser();
			file.setTitle("Open File");
			// 将储存文件路径的list依次添加到ObservableList中
			List<File> list = file.showOpenMultipleDialog(primaryStage);
			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					if (!data2.contains(list.get(i).toString()))
						data2.add(list.get(i).toString());
				}
				// 将ObservableList中的文件路径与删除链接存储为hasmap,形成键值对
				for (int i = 0; i < data2.size(); i++) {
					map2.put(data2.get(i), hyperlinklist[i]);
				}
				videolist2.setItems(data2);
				// 设置导入文件时，显示删除链接按钮
				Set<String> set = map2.keySet();
				Iterator<String> it = set.iterator();
				while (it.hasNext()) {
					Hyperlink link = map2.get(it.next());
					link.setVisible(true);
				}
			}
		} catch (Exception e) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setContentText(e.getMessage());
			alert.show();
		}
	}

	// 功能模块三：选择实现视频音轨合并或分离的视频文件
	// 点击导入视频文件，实现对导入的视频进行显示操作
	// 通过Listview来显示文件信息
	@FXML
	void OnMouseClick3(MouseEvent event) {
		try {
			Hyperlink[] mulist = { hyperlink30, hyperlink31, hyperlink32, hyperlink33, hyperlink34, hyperlink35,
					hyperlink36, hyperlink37, hyperlink38, hyperlink39 };
			Hyperlink[] videolist = { hyperlink20, hyperlink21, hyperlink22, hyperlink23, hyperlink24, hyperlink25,
					hyperlink26, hyperlink27, hyperlink28, hyperlink29 };
			Stage primaryStage = new Stage();
			FileChooser file = new FileChooser();
			file.setTitle("Open File");
			// 将储存文件路径的list依次添加到ObservableList中
			List<File> list = file.showOpenMultipleDialog(primaryStage);
			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					String[] strs = list.get(i).toString().split("\\.");
					// 区分视频音频文件，进行分类显示
					if (strs[strs.length - 1].toLowerCase().equals("mp3")
							|| strs[strs.length - 1].toLowerCase().equals("m4a")
							|| strs[strs.length - 1].toLowerCase().equals("ogg")
							|| strs[strs.length - 1].toLowerCase().equals("flac")) {
						if (!music.contains(list.get(i).toString()))
							music.add(list.get(i).toString());
					} else {
						if (!video3.contains(list.get(i).toString()))
							video3.add(list.get(i).toString());
					}
				}
				// 将ObservableList中音频的文件路径与删除链接存储为hasmap,形成键值对
				for (int i = 0; i < music.size(); i++) {
					map4.put(music.get(i), mulist[i]);
				}
				// 将ObservableList中视频的文件路径与删除链接存储为hasmap,形成键值对
				for (int i = 0; i < video3.size(); i++) {
					map3.put(video3.get(i), videolist[i]);
				}
				if (video3 != null) {
					videolist3.setItems(video3);

				}
				if (music != null) {
					musiclist.setItems(music);
				}
				// 设置导入文件时，显示删除链接按钮
				Set<String> set = map3.keySet();
				Iterator<String> it = set.iterator();
				while (it.hasNext()) {
					Hyperlink link = map3.get(it.next());
					link.setVisible(true);
				}
				// 设置导入文件时，显示删除链接按钮
				Set<String> mset = map4.keySet();
				Iterator<String> mit = mset.iterator();
				while (mit.hasNext()) {
					Hyperlink link = map4.get(mit.next());
					link.setVisible(true);
				}
			}
		} catch (Exception e) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setContentText(e.getMessage());
			alert.show();
		}
	}

	// 功能一：输出转码后的视频文件
	@FXML
	void Outputevent1(MouseEvent event) {
		try {
			Stage primaryStage = new Stage();
			DirectoryChooser file = new DirectoryChooser();
			file.setTitle("Save File");
			// 选择目标文件夹
			File dir = file.showDialog(primaryStage);
			if (dir != null) {
				// 判断用户输入值是否为空，为空则传入null
				Integer width = Width.getText().length() == 0 ? null : Integer.parseInt(Width.getText());
				Integer height = Height.getText().length() == 0 ? null : Integer.parseInt(Height.getText());
				Integer bitrate = Rate.getText().length() == 0 ? null : Integer.parseInt(Rate.getText());
				// 调用transform函数进行转码操作，tab1OutputFormat是视频格式，tab1Outputcodec是编码格式，分辨率宽和高为width， height，码率为bitrate
				// 
				Bridge.transform(data, dir.toString(), this.tab1OutputFormat, this.tab1Outputcodec, width, height,
						bitrate);
				// 提示转码完成
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setContentText("视频转换完成，输出路径为:"+dir.getAbsolutePath());
				alert.show();
			}
		} catch (Exception e) {
			// 处理相应异常
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setContentText(e.getMessage());
			alert.show();
		}
	}

	// 功能二：输出合并后的视频文件
	@FXML
	void Outputevent2(MouseEvent event) {
		try {
			Stage primaryStage = new Stage();
			FileChooser file = new FileChooser();
			file.setTitle("Save File");
			// 命名并保存合并后的视频文件
			File filename = file.showSaveDialog(primaryStage);
			if (filename != null) {
				// 调用cat函数进行合并操作，tab1OutputTran选择转场形式，filename.getAbsolutePath()保存文件路径
				Bridge.cat(data2, filename.getAbsolutePath(), tab1OutputTran);
				// 提示视频合并完成
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setContentText("视频合并完成，输出路径为:"+filename.getAbsolutePath());
				alert.show();
			}
		} catch (Exception e) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setContentText(e.getMessage());
			alert.show();
		}
	}

	// 输出视频音频合并或分离后的文件
	// 合并的视频文件以_joined结尾，分离的视频文件以_VideoOnly结尾，音频文件以原视频文件命名，格式为MP3
	// 合并后的视频文件以_merged结尾
	@FXML
	void Outputevent3(MouseEvent event) {
		try {
			Stage primaryStage = new Stage();
			DirectoryChooser file = new DirectoryChooser();
			file.setTitle("Save File");
			// 保存文件至目标文件夹
			File dir = file.showDialog(primaryStage);
			if (dir != null) {
				if (music == null || (music != null && music.size() == 0)) {
					// 调用splitAudio函数进行视频音轨分离操作，dir.getAbsolutePath()所选择的文件夹路径
					Bridge.splitAudio(video3, dir.getAbsolutePath());
					// 提示视频音频分离完成
					Alert alert = new Alert(Alert.AlertType.INFORMATION);
					alert.setContentText("视频音轨分离完成，输出路径为:"+dir.getAbsolutePath() + "，无音频视频文件以_videoOnly结尾，音频为原来视频的名字");
					alert.show();
				} else {
					Bridge.joinVideoAudio(video3, music, dir.getAbsolutePath());
					// 提示视频音频合并完成
					Alert alert = new Alert(Alert.AlertType.INFORMATION);
					alert.setContentText("视频音轨合并完成，输出路径为:"+dir.getAbsolutePath() + "合并的视频文件以_merged结尾");
					alert.show();
				}
			}
		} catch (Exception e) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setContentText(e.getMessage());
			alert.show();
		}
	}

	// 功能一：设置hyperlink的点击事件，点击 × 删除功能模块一中的视频文件
	@FXML
	void deletelabel1(ActionEvent event) {
		Hyperlink source = (Hyperlink) event.getSource();
		Integer id = Integer.parseInt(source.getId().split("hyperlink")[1]);
		// 删除Listview中导入视频的路径信息
		videolist1.getItems().remove(id.intValue());
		Hyperlink[] hyperlinklist = { hyperlink0, hyperlink1, hyperlink2, hyperlink3, hyperlink4, hyperlink5,
				hyperlink6, hyperlink7, hyperlink8, hyperlink9 };
		// 设置最后一个hyperlink的内容为不可见
		hyperlinklist[videolist1.getItems().size()].setVisible(false);
	}

	// 设置hyperlink的点击事件，点击 × 删除功能模块二中的视频文件
	@FXML
	void deletelabel2(ActionEvent event) {
		Hyperlink source = (Hyperlink) event.getSource();
		Integer id = Integer.parseInt(source.getId().split("hyperlink")[1]);
		// 删除Listview中导入视频的路径信息
		videolist2.getItems().remove(id.intValue() - 10);
		Hyperlink[] hyperlinklist = { hyperlink10, hyperlink11, hyperlink12, hyperlink13, hyperlink14, hyperlink15,
				hyperlink16, hyperlink17, hyperlink18, hyperlink19 };
		// 设置最后一个hyperlink的内容为不可见
		hyperlinklist[videolist2.getItems().size()].setVisible(false);
	}

	// 设置hyperlink的点击事件，点击 × 删除功能模块三中的视频文件
	@FXML
	void deletelabel3(ActionEvent event) {
		Hyperlink source = (Hyperlink) event.getSource();
		Integer id = Integer.parseInt(source.getId().split("hyperlink")[1]);
		// 删除Listview中导入视频的路径信息
		videolist3.getItems().remove(id.intValue() - 20);
		Hyperlink[] hyperlinklist = { hyperlink20, hyperlink21, hyperlink22, hyperlink23, hyperlink24, hyperlink25,
				hyperlink26, hyperlink27, hyperlink28, hyperlink29 };
		// 设置最后一个hyperlink的内容为不可见
		hyperlinklist[videolist3.getItems().size()].setVisible(false);
	}

	// 设置hyperlink的点击事件，点击 × 删除功能模块三中的音频文件
	@FXML
	void deletelabel4(ActionEvent event) {
		Hyperlink source = (Hyperlink) event.getSource();
		Integer id = Integer.parseInt(source.getId().split("hyperlink")[1]);
		// 删除Listview中导入音频的路径信息
		musiclist.getItems().remove(id.intValue() - 30);
		Hyperlink[] hyperlinklist = { hyperlink30, hyperlink31, hyperlink32, hyperlink33, hyperlink34, hyperlink35,
				hyperlink36, hyperlink37, hyperlink38, hyperlink39 };
		// 设置最后一个hyperlink的内容为不可见
		hyperlinklist[musiclist.getItems().size()].setVisible(false);
	}

	// 设置鼠标进入Listview中可修改路径信息
	@FXML
	void OnMouseEntered1(MouseEvent event) {
		videolist1.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		videolist1.setEditable(true);
		videolist1.setCellFactory(TextFieldListCell.forListView());
	}

	// 设置鼠标进入Listview中可修改路径信息
	@FXML
	void OnMouseEntered2(MouseEvent event) {
		videolist2.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		videolist2.setEditable(true);
		videolist2.setCellFactory(TextFieldListCell.forListView());
	}

	// 设置鼠标进入Listview中可修改路径信息
	@FXML
	void OnMouseEntered3(MouseEvent event) {
		videolist3.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		videolist3.setEditable(true);
		videolist3.setCellFactory(TextFieldListCell.forListView());
	}

	// 设置鼠标进入Listview中可修改路径信息
	@FXML
	void OnMouseEntered4(MouseEvent event) {
		musiclist.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		musiclist.setEditable(true);
		musiclist.setCellFactory(TextFieldListCell.forListView());
	}

	// 功能一：选择编码格式
	@FXML
	void SelectCodec(ActionEvent event) {
		// 储存编码格式类型
		Map<String, String> codecs = new HashMap<String, String>();
		codecs.put("自动", null);
		codecs.put("H.264", "h264");
		codecs.put("H.265", "hevc");
		MenuItem item = (MenuItem) event.getSource();
		CodecButton.setText(item.getText());
		// 将选择的类型传入对应的参数
		this.tab1Outputcodec = codecs.get(item.getText());
	}

	// 功能一：选择视频格式
	@FXML
	void SelectFormat(ActionEvent event) {
		// 储存编码格式类型
		Map<String, String> videoFormats = new HashMap<String, String>();
		videoFormats.put("MPEG-4", "mp4");
		videoFormats.put("QuickTime MOV", "mov");
		videoFormats.put("FLV", "flv");
		videoFormats.put("MKV", "mkv");
		videoFormats.put("TS", "ts");
		MenuItem item = (MenuItem) event.getSource();
		FormatButton.setText(item.getText());
		// 将选择的类型传入对应的参数
		this.tab1OutputFormat = videoFormats.get(item.getText());
	}

	// 功能二：选择转场形式
	@FXML
	void SelectTransition(ActionEvent event) {
		// 储存转场形式
		Map<String, String> transitions = new HashMap<String, String>();
		transitions.put("无", null);
		transitions.put("渐隐", "fade");
		transitions.put("向左擦除", "wipeleft");
		transitions.put("向右擦除", "wiperight");
		transitions.put("向上擦除", "wipeup");
		transitions.put("向下擦除", "wipedown");
		transitions.put("左滑", "slideleft");
		transitions.put("右滑", "slideright");
		transitions.put("上滑", "slideup");
		transitions.put("下滑", "slidedown");
		transitions.put("圆形切割", "circlecrop");
		transitions.put("方形切割", "rectcrop");
		transitions.put("距离", "distance");
		transitions.put("黑色渐变", "fadeblack");
		transitions.put("白色渐变", "fadewhite");
		transitions.put("镭射", "radial");
		transitions.put("圆形打开", "circleopen");
		transitions.put("圆形关闭", "circleclose");
		transitions.put("溶解", "dissolve");
		transitions.put("像素化", "pixelize");
		MenuItem item = (MenuItem) event.getSource();
		CatButton.setText(item.getText());
		// 将选择的类型传入对应的参数
		this.tab1OutputTran = transitions.get(item.getText());
	}
}
