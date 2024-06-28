package it.polimi.ingsw.is24am03.client.view.GUI;
import it.polimi.ingsw.is24am03.client.connection.Client;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

/**
 * Abstract class for the GUIControllers
 */
public abstract class GUIController {
    protected static Client clientController;
    protected Stage stage;
    private GUIView manager;

    /**
     * Sets the connection handler
     * @param clientController connection manager for this client
     */
    public void setConnectionHandler( Client clientController){
        GUIController.clientController = clientController;
    }


    /**
     *
     * @return the gui manager
     */
    protected GUIView getManager() {return manager;}

    /**
     * Sets the current stage attribute
     * @param stage stage reference
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Shows an alert when an exception is thrown
     * @param title title of the alert
     * @param content alert's details
     */
    public void showAlert(String title, String content) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(title);
            alert.setContentText(content);
            alert.showAndWait();
        });
    }
}