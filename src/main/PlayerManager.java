package main;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import main.classes.Character;
import main.classes.SpriteType;
import main.classes.Weapon;

import java.awt.*;
import java.util.LinkedHashMap;

public class PlayerManager {

    private Character player;
    private Integer playerScore;

    public int unlockedBonuses;

    private boolean isMovingUp;
    private boolean isMovingDown;
    private boolean isMovingLeft;
    private boolean isMovingRight;
    private boolean gamePaused;

    public boolean isGamePaused() {
        return gamePaused;
    }

    private LinkedHashMap<Integer, Weapon> rewardWeapons;
    private int nextScoreRewardStep;
    private Point spawnPoint;

    private GraphicManager graphicManager;
    private boolean highestRewardReached;

    public PlayerManager(GraphicManager graphicManager) {
        this.graphicManager = graphicManager;

        spawnPoint = new Point();

        player = new Character(5, SpriteType.PLAYER);
        player.setLife(10);
        player.setPosition(graphicManager.getScreenWidth() / 4, graphicManager.getPane().getHeight() / 2);

        isMovingUp = false;
        isMovingDown = false;
        isMovingLeft = false;
        isMovingRight = false;

        rewardWeapons = new LinkedHashMap<>();
        rewardWeapons.put(500, new Weapon(this.player, 3, 1, FilesName.SHOOT1, SpriteType.PLAYER_BULLET1));
        rewardWeapons.put(2000, new Weapon(this.player, 4, 1, FilesName.SHOOT1, SpriteType.PLAYER_BULLET1));
        rewardWeapons.put(7500, new Weapon(this.player, 2, 4, FilesName.SHOOT2, SpriteType.PLAYER_BULLET2));
        rewardWeapons.put(12500, new Weapon(this.player, 3, 4, FilesName.SHOOT2, SpriteType.PLAYER_BULLET2));
        rewardWeapons.put(20000, new Weapon(this.player, 4, 4, FilesName.SHOOT2, SpriteType.PLAYER_BULLET2));
        rewardWeapons.put(30000, new Weapon(this.player, 11, 2, FilesName.SHOOT3, SpriteType.PLAYER_BULLET3));
        rewardWeapons.put(60000, new Weapon(this.player, 11, 3, FilesName.SHOOT3, SpriteType.PLAYER_BULLET3));
        rewardWeapons.put(100000, new Weapon(this.player, 11, 5, FilesName.SHOOT3, SpriteType.PLAYER_BULLET3));
        rewardWeapons.put(150000, new Weapon(this.player, 16, 5, FilesName.SHOOT3, SpriteType.PLAYER_BULLET3));
        rewardWeapons.put(300000, new Weapon(this.player, 16, 8, FilesName.SHOOT3, SpriteType.PLAYER_BULLET3));
        rewardWeapons.put(500000, new Weapon(this.player, 20, 8, FilesName.SHOOT3, SpriteType.PLAYER_BULLET3));
        rewardWeapons.put(750000, new Weapon(this.player, 20, 15, FilesName.SHOOT3, SpriteType.PLAYER_BULLET3));
        rewardWeapons.put(1000000, new Weapon(this.player, 30, 15, FilesName.SHOOT3, SpriteType.PLAYER_BULLET3));
        nextScoreRewardStep = rewardWeapons.keySet().stream().mapToInt(value -> value).min().orElse(-1);
    }

    public Integer getPlayerScore() {
        return playerScore;
    }

    public void setPlayerScore(Integer playerScore) {
        this.playerScore = playerScore;
    }

    public void addPlayerScore(Integer scoreToAdd) {
        this.setPlayerScore(playerScore + scoreToAdd);
        checkForUpgrades();
        graphicManager.updatePlayerScore(getPlayerScore() + "/" + getNextScoreRewardStep());
    }

    public void checkForUpgrades() {
        if (nextScoreRewardStep <= playerScore && !highestRewardReached) {
            player.setMainWeapon(rewardWeapons.get(nextScoreRewardStep));
            ResourcesManager.getInstance().playSound(FilesName.UPGRADE, 5);
            rewardWeapons.remove(nextScoreRewardStep);
            nextScoreRewardStep = rewardWeapons.keySet().stream().mapToInt(value -> value).min().orElse(-1);
            if (nextScoreRewardStep == -1) {
                highestRewardReached = true;
            }
        }
    }

    public int getNextScoreRewardStep() {
        return nextScoreRewardStep;
    }

    public void enableKeyboardControl(boolean enabled) {
        if (enabled) {
            graphicManager.getStage().getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent event) {
                    switch (event.getCode()) {
                        case P:
                        case ESCAPE:
                            gamePaused = !gamePaused;
                            break;
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
        } else {
            graphicManager.getStage().getScene().setOnKeyPressed(null);
            graphicManager.getStage().getScene().setOnKeyReleased(null);
        }
    }

    public void enableMouseControl(boolean enabled) {
        Scene scene = graphicManager.getStage().getScene();

        if (enabled) {
            scene.setOnMouseMoved(this::moveToCursor);

            scene.setOnMouseDragged(this::moveToCursor);

            scene.setOnMousePressed(event -> {
                if (MouseButton.PRIMARY.equals(event.getButton()))
                    this.player.setIsShooting(true);
                if (MouseButton.SECONDARY.equals(event.getButton()))
                    this.player.setIsShootingSecondary(true);
                if(!graphicManager.getStage().isFocused()){
                }
            });

            scene.setOnMouseReleased(event -> {
                if (MouseButton.PRIMARY.equals(event.getButton()))
                    this.player.setIsShooting(false);
                if (MouseButton.SECONDARY.equals(event.getButton()))
                    this.player.setIsShootingSecondary(false);

            });

        } else {
            scene.setOnMouseMoved(null);
            scene.setOnMouseDragged(null);
            scene.setOnMousePressed(null);
            scene.setOnMouseReleased(null);
        }

    }

    public void moveToCursor(MouseEvent mouseEvent) {
        if (isGamePaused()) {
            return;
        }

        if(0 <= mouseEvent.getX() && mouseEvent.getX() + player.getWidth() <= graphicManager.getPane().getWidth())
            this.player.setX(mouseEvent.getX());
        if(0 <= mouseEvent.getY() && mouseEvent.getY() + player.getHeight() <= graphicManager.getPane().getHeight())
            this.player.setY(mouseEvent.getY());
    }

    public void updatePlayerCoefficients() {
        if (!isGamePaused()) {
            if (isMovingRight ^ isMovingLeft) {
                if (isMovingLeft) player.setMovingXcoefficient(-1);
                if (isMovingRight) player.setMovingXcoefficient(1);
            } else {
                player.setMovingXcoefficient(0);
            }

            if (isMovingUp ^ isMovingDown) {
                if (isMovingUp) player.setMovingYcoefficient(-1);
                if (isMovingDown) player.setMovingYcoefficient(1);
            } else {
                player.setMovingYcoefficient(0);
            }
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
