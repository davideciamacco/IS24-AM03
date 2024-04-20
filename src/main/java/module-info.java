module it.polimi.ingsw.is24am03 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.testng;

    opens it.polimi.ingsw.is24am03 to javafx.fxml;
    exports it.polimi.ingsw.is24am03;
}