module it.polimi.ingsw.is24am03 {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires java.rmi;
    requires java.desktop;

    opens it.polimi.ingsw.is24am03 to javafx.fxml;
    exports it.polimi.ingsw.is24am03;
    exports it.polimi.ingsw.is24am03.server.model.game to java.rmi;
    exports it.polimi.ingsw.is24am03.Subscribers;
    exports it.polimi.ingsw.is24am03.client.clientModel;
    opens it.polimi.ingsw.is24am03.client.clientModel to javafx.fxml;
    exports it.polimi.ingsw.is24am03.client.connection;
    opens it.polimi.ingsw.is24am03.client.connection to javafx.fxml;
    exports it.polimi.ingsw.is24am03.client.view.CLI;
    opens it.polimi.ingsw.is24am03.client.view.CLI to javafx.fxml;
    exports it.polimi.ingsw.is24am03.client.view.GUI;
    opens it.polimi.ingsw.is24am03.client.view.GUI to javafx.fxml;
    exports it.polimi.ingsw.is24am03.server;
    exports it.polimi.ingsw.is24am03.server.model.cards;
    exports it.polimi.ingsw.is24am03.server.model.enums;
    exports it.polimi.ingsw.is24am03.server.model.player;
    exports it.polimi.ingsw.is24am03.server.model.chat;
    exports it.polimi.ingsw.is24am03.server.model.decks;
    exports it.polimi.ingsw.is24am03.server.model.exceptions;
   exports it.polimi.ingsw.is24am03.messages;
    opens it.polimi.ingsw.is24am03.server to javafx.fxml;
   // exports it.polimi.ingsw.is24am03.client;
    //opens it.polimi.ingsw.is24am03.client to javafx.fxml;
    exports it.polimi.ingsw.is24am03.client.view;
    opens it.polimi.ingsw.is24am03.client.view to javafx.fxml;
}