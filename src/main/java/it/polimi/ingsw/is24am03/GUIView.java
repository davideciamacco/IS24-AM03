package it.polimi.ingsw.is24am03;

import it.polimi.ingsw.is24am03.server.model.cards.ObjectiveCard;
import it.polimi.ingsw.is24am03.server.model.cards.PlayableCard;
import it.polimi.ingsw.is24am03.server.model.cards.ResourceCard;
import it.polimi.ingsw.is24am03.server.model.cards.StartingCard;
import it.polimi.ingsw.is24am03.server.model.enums.Color;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import javax.swing.plaf.synth.SynthOptionPaneUI;
import java.awt.Taskbar;
import java.awt.Toolkit;
import java.awt.Taskbar.Feature;
import java.io.IOException;
import java.util.*;

/**
 * Gui Application Class
 */
public class GUIView extends Application implements ViewInterface {
    private static Client controller;
    private GUIController guiController;
    private FXMLLoader fxmlLoader;
    private Scene scene;
    private Stage stage;
    private String connectionType;

    public static void setClient(Client client) {
        controller = client;
    }
    
    @Override
    public void start(Stage stage) {
        Parameters parameters = getParameters();
        List<String> args = parameters.getRaw();
        controller.setGUI(this);

        this.stage = stage;
        this.stage.setFullScreen(true);
        this.stage.getIcons().add(new Image(getClass().getResourceAsStream("/it/polimi/ingsw/is24am03/logo.jpeg")));

        if (Taskbar.isTaskbarSupported()) {
            var taskbar = Taskbar.getTaskbar();
            if (taskbar.isSupported(Feature.ICON_IMAGE)) {
                final Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
                var dockIcon = defaultToolkit.getImage(getClass().getResource("/logo.jpeg"));
                taskbar.setIconImage(dockIcon);
            }
        }
        this.stage.setMinWidth(750);
        this.stage.setMinHeight(750);

        fxmlLoader = new FXMLLoader(GUIView.class.getResource("/it/polimi/ingsw/is24am03/login-view.fxml"));

        try {
            Parent root = fxmlLoader.load();
            scene = new Scene(root, 1500, 750);
            guiController = fxmlLoader.getController();
            guiController.setConnectionHandler(controller);
            this.stage.setTitle("Codex Naturalis");
            this.stage.setScene(scene);
            this.stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Loading Error", "Unable to load the FXML file.");
        }

    }

    private void showAlert(String title, String content) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(title);
            alert.setContentText(content);
            alert.showAndWait();
        });
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void drawScene(SceneType sceneType) {
        Platform.runLater(() -> {
            String fxmlPath;
            switch (sceneType) {

                default -> fxmlPath = "/it/polimi/ingsw/is24am03/login-view.fxml";
            }

            fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(GUIView.class.getResource(fxmlPath));

            try {
                Parent newRoot = fxmlLoader.load();
                scene.setRoot(newRoot);
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Loading Error", "Unable to load the FXML file.");
            }

            guiController = fxmlLoader.getController();
            guiController.setStage(stage);
            stage.setScene(scene);
            stage.show();
        });
    }

    @Override
    public void notify(String s) {

    }

    @Override
    public void drawBoard(PlayableCard[][] playableCards) {

    }

    @Override
    public void drawHand(ArrayList<ResourceCard> hand) {

    }

    @Override
    public void drawObjective(ObjectiveCard o) {

    }

    @Override
    public void drawStarting(StartingCard startingCard) {

    }

    @Override
    public void drawTable(Map<String, Integer> playerPoints, ResourceCard resourceDeck, ResourceCard goldDeck, ResourceCard card0, ResourceCard card1, ResourceCard card2, ResourceCard card3) {

    }

    @Override
    public void drawAvailableColors(ArrayList<Color> colors) {

    }

}
