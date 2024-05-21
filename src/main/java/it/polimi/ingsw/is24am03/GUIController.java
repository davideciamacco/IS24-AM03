package it.polimi.ingsw.is24am03;
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
}