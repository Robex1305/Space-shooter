package main;

import javafx.animation.AnimationTimer;
import javafx.stage.Stage;
import main.classes.Bullet;
import main.classes.PlayerManager;
import main.classes.Sprite;
import main.classes.Type;

import java.awt.*;

public class GameManager {
    protected GraphicManager graphicManager;
    protected PlayerManager playerManager;
    protected AnimationTimer timer;

    public GameManager(Stage primaryStage){
        graphicManager = new GraphicManager(primaryStage);
        playerManager = new PlayerManager(graphicManager);
        playerManager.enableMouseControl(true);

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
            }
        };
    }

    public void update(){
        if(Math.random() < 0.1) {
            Sprite star = new Sprite(1, 10, Type.STAR);
            star.toBack();
            graphicManager.add(star);
        }
    }

    public void start(){
        graphicManager.add(playerManager.getPlayer());
        graphicManager.start();
        timer.start();
    }

}
