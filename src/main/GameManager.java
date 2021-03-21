package main;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import main.classes.PlayerManager;

import java.io.IOException;

public class GameManager {
    protected GraphicManager graphicManager;
    protected PlayerManager playerManager;

    public GameManager(Stage primaryStage){
        graphicManager = new GraphicManager(primaryStage);
        playerManager = new PlayerManager(graphicManager);


    }

    public void start(){
        graphicManager.add(playerManager.getPlayerSprite());
        graphicManager.start();
    }

}
