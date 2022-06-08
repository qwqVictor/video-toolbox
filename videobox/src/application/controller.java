package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Tab;
import javafx.scene.input.MouseEvent;

public class controller {

    @FXML
    private Tab tab1;

    @FXML
    private Tab tab2;

    @FXML
    private Tab tab3;

    @FXML
    void OnMouseClick(MouseEvent event) {
		try {
			System.out.println("!");
		}catch(Exception e){	
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setContentText("The video tool box is error");
			alert.show();
		}
    }

}

