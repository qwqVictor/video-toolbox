module videobox {
	requires javafx.controls;
	requires javafx.fxml;
	requires java.sql;
	requires javafx.graphics;
	
	opens videobox to javafx.graphics, javafx.fxml;
}
