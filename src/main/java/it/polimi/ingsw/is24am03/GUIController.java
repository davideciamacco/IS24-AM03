package it.polimi.ingsw.is24am03;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public abstract class GUIController {
    protected static Client clientController;
    protected Stage stage;
    private GUIView manager;

    public void setConnectionHandler( Client clientController){
        GUIController.clientController = clientController;
    }

    public void setManager(GUIView manager){
        this.manager = manager;
    }

    public abstract void postNotification(String title, String desc);
    protected  Client getClientController(){
        return GUIController.clientController;
    }

    protected GUIView getManager() {return manager;}

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void showAlert(String title, String content) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(title);
            alert.setContentText(content);
            alert.showAndWait();
        });
    }
}