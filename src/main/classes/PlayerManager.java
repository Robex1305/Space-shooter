package main.classes;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import main.GraphicManager;

import java.awt.*;

public class PlayerManager {

    private Sprite player;
    private boolean isMovingUp;
    private boolean isMovingDown;
    private boolean isMovingLeft;
    private boolean isMovingRight;

    private Point spawnPoint;

    private GraphicManager graphicManager;

    public PlayerManager(GraphicManager graphicManager){
        this.graphicManager = graphicManager;
        Stage stage = graphicManager.getStage();
        Scene scene = stage.getScene();

        spawnPoint = new Point();
        spawnPoint.setLocation(50,graphicManager.getPane().getHeight()/2);
        player = new Sprite(spawnPoint, 1, 3, Type.PLAYER);

        isMovingUp = false;
        isMovingDown = false;
        isMovingLeft = false;
        isMovingRight = false;

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()){
                    case Z:
                        isMovingUp = true;
                        break;
                    case S:
                        isMovingDown = true;
                        break;
                    case Q:
                        isMovingLeft = true;
                        break;
                    case D:
                        isMovingRight = true;
                        break;
                    case SPACE:
                        player.setShooting(true);
                        break;
                }
                updatePlayerCoefficients();
            }
        });
        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case Z:
                        isMovingUp = false;
                        break;
                    case S:
                        isMovingDown = false;
                        break;
                    case Q:
                        isMovingLeft = false;
                        break;
                    case D:
                        isMovingRight = false;
                        break;
                    case SPACE:
                        player.setShooting(false);
                        break;
                }
                updatePlayerCoefficients();
            }
        });
    }

    public void updatePlayerCoefficients() {
        if(isMovingRight ^ isMovingLeft){
            if(isMovingLeft) player.setMovingXcoefficient(-1);
            if(isMovingRight) player.setMovingXcoefficient(1);
        }
        else{
            player.setMovingXcoefficient(0);
        }

        if(isMovingUp ^ isMovingDown){
            if(isMovingUp) player.setMovingYcoefficient(-1);
            if(isMovingDown) player.setMovingYcoefficient(1);
        }
        else{
            player.setMovingYcoefficient(0);
        }
    }

    public void shoot(){
        Sprite bullet = new Sprite(player.getPosition(),0.2, 3, Type.BULLET);
        bullet.setMovingXcoefficient(1);
        graphicManager.add(bullet);
    }

    public Sprite getPlayer() {
        return player;
    }

    public boolean isMovingUp() {
        return isMovingUp;
    }

    public boolean isMovingDown() {
        return isMovingDown;
    }

    public boolean isMovingLeft() {
        return isMovingLeft;
    }

    public boolean isMovingRight() {
        return isMovingRight;
    }
}
