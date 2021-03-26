package main.classes;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import main.GraphicManager;

import java.awt.*;

public class PlayerManager {

    private Character player;
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
        player = new Character(spawnPoint, 1, 3, Type.PLAYER);
        player.getWeapon().setRateOfFire(7);

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
                        player.setIsShooting(true);
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
                        player.setIsShooting(false);
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
