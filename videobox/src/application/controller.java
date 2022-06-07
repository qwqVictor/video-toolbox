package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class controller {

    @FXML
    private Button a;

    @FXML
    void setOnClick(ActionEvent event) {
    	System.out.print("!");
    }

}
