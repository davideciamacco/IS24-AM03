module it.polimi.ingsw.is24am03 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.rmi;

    opens it.polimi.ingsw.is24am03 to javafx.fxml;
    exports it.polimi.ingsw.is24am03;
    exports it.polimi.ingsw.is24am03.server.model.game to java.rmi;
    exports it.polimi.ingsw.is24am03.Subscribers;
}