package videobox;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;


public class Main extends Application {

	/* 设置程序入口，显示应用界面 */
	@Override
	public void start(Stage primaryStage) {
		try {
			//加载并fxml文件，声明root变量
			Parent root = FXMLLoader.load(
					Main.class.getResource("ToolUI.fxml")
			);
			//声明界面变量
			Scene scene = new Scene(root,640,480);
			primaryStage.setTitle("VideoToolBox");
			primaryStage.setScene(scene);
			//显示界面
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		launch(args);
	}
}
