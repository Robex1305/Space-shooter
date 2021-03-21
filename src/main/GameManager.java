package main;

import javafx.stage.Stage;
import main.classes.PlayerManager;
import main.classes.Sprite;
import main.classes.Type;

public class GameManager {
    protected GraphicManager graphicManager;
    protected PlayerManager playerManager;

    public GameManager(Stage primaryStage){
        graphicManager = new GraphicManager(primaryStage);
        playerManager = new PlayerManager(graphicManager);

        if(playerManager.getPlayer().isShooting()){
            Sprite bullet = new Sprite(playerManager.getPlayer().getPosition(),0.2, 3, Type.BULLET);
            bullet.setMovingXcoefficient(1);
            graphicManager.add(bullet);
        }
    }

    public void start(){
        graphicManager.add(playerManager.getPlayer());
        graphicManager.start();
    }

}
