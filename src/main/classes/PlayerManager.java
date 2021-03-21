package main.classes;

import com.sun.javafx.geom.Vec2d;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import main.GameManager;
import main.GraphicManager;

import java.awt.*;

public class PlayerManager {

    boolean isLeftClicking;
    private Sprite playerSprite;
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
        playerSprite = new Sprite(spawnPoint, 2, Type.PLAYER);

        isLeftClicking = false;
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
                }
                updatePlayerCoefficients();
            }
        });
        scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                isLeftClicking = true;
            }
        });
        scene.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                switch (event.getButton()){
                    case PRIMARY:
                        isLeftClicking = false;
                }

            }
        });
        scene.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                switch (event.getButton()){
                    case PRIMARY:
                        isLeftClicking = true;
                }
            }
        });
    }

    public void updatePlayerCoefficients() {
        if(isMovingRight ^ isMovingLeft){
            if(isMovingLeft) playerSprite.setMovingXcoefficient(-1);
            if(isMovingRight) playerSprite.setMovingXcoefficient(1);
        }
        else{
            playerSprite.setMovingXcoefficient(0);
        }

        if(isMovingUp ^ isMovingDown){
            if(isMovingUp) playerSprite.setMovingYcoefficient(-1);
            if(isMovingDown) playerSprite.setMovingYcoefficient(1);
        }
        else{
            playerSprite.setMovingYcoefficient(0);
        }
    }

    public Sprite getPlayerSprite() {
        return playerSprite;
    }

    public boolean isLeftClicking() {
        return isLeftClicking;
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
