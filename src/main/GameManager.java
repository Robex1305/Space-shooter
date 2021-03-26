package main;

import javafx.stage.Stage;
import main.classes.Bullet;
import main.classes.PlayerManager;
import main.classes.Sprite;
import main.classes.Type;

public class GameManager {
    protected GraphicManager graphicManager;
    protected PlayerManager playerManager;

    public GameManager(Stage primaryStage){
        graphicManager = new GraphicManager(primaryStage);
        playerManager = new PlayerManager(graphicManager);
    }

    public void start(){
        graphicManager.add(playerManager.getPlayer());
        graphicManager.start();
    }

}
