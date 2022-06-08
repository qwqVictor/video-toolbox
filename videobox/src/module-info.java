module videobox {
	requires javafx.controls;
	requires javafx.fxml;
	requires mysql.connector.java;
	requires java.sql;
	
	opens application to javafx.graphics, javafx.fxml;
}
