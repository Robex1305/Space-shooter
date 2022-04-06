package main;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import main.classes.Character;
import main.classes.Sprite;
import main.classes.SpriteType;
import main.classes.Weapon;

import java.awt.*;

public class PlayerManager {

    private Character player;
    private Integer playerScore;

    public int unlockedBonuses;

    private boolean isMovingUp;
    private boolean isMovingDown;
    private boolean isMovingLeft;
    private boolean isMovingRight;



    private Point spawnPoint;

    private GraphicManager graphicManager;

    public PlayerManager(GraphicManager graphicManager){
        this.graphicManager = graphicManager;

        spawnPoint = new Point();

        player = new Character(5, SpriteType.PLAYER);
        player.setPosition(graphicManager.getScreenWidth()/4,graphicManager.getPane().getHeight()/2);

        isMovingUp = false;
        isMovingDown = false;
        isMovingLeft = false;
        isMovingRight = false;
    }

    public Integer getPlayerScore() {
        return playerScore;
    }

    public void setPlayerScore(Integer playerScore) {
        this.playerScore = playerScore;
    }

    //TODO: rework this to make upgrades manageable more easily
    public void addPlayerScore(Integer scoreToAdd){
        int unlockedBonusesTmp = unlockedBonuses;
        this.setPlayerScore(playerScore + scoreToAdd);
        if(unlockedBonuses == 0 && getPlayerScore() >= 2000){
            this.player.setWeapon(new Weapon(4,1, FilesName.SHOOT1, SpriteType.PLAYER_BULLET1));
            unlockedBonuses++;
        }
        else if(unlockedBonuses == 1 && getPlayerScore() >= 10000){
            this.player.setWeapon(new Weapon(2,4, FilesName.SHOOT2, SpriteType.PLAYER_BULLET2));
            unlockedBonuses++;
        }
        else if(unlockedBonuses == 2 && getPlayerScore() >= 17500){
            this.player.setWeapon(new Weapon(4,4, FilesName.SHOOT2, SpriteType.PLAYER_BULLET2));
            unlockedBonuses++;
        }
        else if(unlockedBonuses == 3 && getPlayerScore() >= 30000){
            this.player.setWeapon(new Weapon(7,3, FilesName.SHOOT3, SpriteType.PLAYER_BULLET3));
            unlockedBonuses++;
        }
        else if(unlockedBonuses == 4 && getPlayerScore() >= 60000){
            this.player.setWeapon(new Weapon(10,3, FilesName.SHOOT3, SpriteType.PLAYER_BULLET3));
            unlockedBonuses++;
        }
        else if(unlockedBonuses == 5 && getPlayerScore() >= 100000){
            this.player.setWeapon(new Weapon(10,5, FilesName.SHOOT3, SpriteType.PLAYER_BULLET3));
            unlockedBonuses++;
        }
        else if(unlockedBonuses == 6 && getPlayerScore() >= 150000){
            this.player.setWeapon(new Weapon(15,5, FilesName.SHOOT3, SpriteType.PLAYER_BULLET3));
            unlockedBonuses++;
        }
        else if(unlockedBonuses == 7 && getPlayerScore() >= 300000){
            this.player.setWeapon(new Weapon(20,8, FilesName.SHOOT3, SpriteType.PLAYER_BULLET3));
            unlockedBonuses++;
        }
        else if(unlockedBonuses == 8 && getPlayerScore() >= 500000){
            this.player.setWeapon(new Weapon(20,12, FilesName.SHOOT3, SpriteType.PLAYER_BULLET3));
            unlockedBonuses++;
        }
        else if(unlockedBonuses == 8 && getPlayerScore() >= 1000000){
            this.player.setWeapon(new Weapon(30,15, FilesName.SHOOT3, SpriteType.PLAYER_BULLET3));
            unlockedBonuses++;
        }

        if(unlockedBonusesTmp != unlockedBonuses) {
            ResourcesManager.getInstance().playSound(FilesName.UPGRADE, 5);
        }
    }

    public void enableKeyboardControl(boolean enabled){
        if(enabled){
            graphicManager.getStage().getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
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
            graphicManager.getStage().getScene().setOnKeyReleased(new EventHandler<KeyEvent>() {

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
        else{
            graphicManager.getStage().getScene().setOnKeyPressed(null);
            graphicManager.getStage().getScene().setOnKeyReleased(null);
        }
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

    public Character getPlayer() {
        return player;
    }

    public void setPlayer(Character player) {
        this.player = player;
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
