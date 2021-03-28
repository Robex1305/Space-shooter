package main;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import main.GraphicManager;
import main.classes.Character;
import main.classes.Sprite;
import main.classes.Type;

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
        player = new Character(spawnPoint, 1.5,5, Type.PLAYER);
        player.getWeapon().setRateOfFire(50);

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

    public void enableMouseControl(boolean enabled){
        Scene scene = graphicManager.getStage().getScene();

        if(enabled){
            scene.setOnMouseMoved(event -> {
                moveToCursor(event);
            });

            scene.setOnMouseDragged(event -> {
                moveToCursor(event);
            });

            scene.setOnMousePressed(event -> {
                this.player.setIsShooting(true);
            });

            scene.setOnMouseReleased(event -> {
                this.player.setIsShooting(false);
            });
        }
        else{
            scene.setOnMouseMoved(null);
            scene.setOnMouseDragged(null);
            scene.setOnMousePressed(null);
            scene.setOnMouseReleased(null);
        }

    }

    public void moveToCursor(MouseEvent mouseEvent){
        if(graphicManager.getPane().intersects(mouseEvent.getX(), mouseEvent.getY(), 1, 1))
        this.player.setPosition(mouseEvent.getX(), mouseEvent.getY());
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
