package main;

import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.classes.*;
import main.classes.Character;

import java.awt.*;

public class GameManager {
    protected GraphicManager graphicManager;
    protected PlayerManager playerManager;
    protected NpcManager npcManager;
    protected AnimationTimer timer;
    protected double time;

    public GameManager(Stage primaryStage) {
        graphicManager = new GraphicManager(primaryStage);
        playerManager = new PlayerManager(graphicManager);
        npcManager = new NpcManager(graphicManager, playerManager.getPlayer());
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
            }
        };
    }

    public void update() {
        if (Math.random() < 0.05) {
            double rand = Math.random();
            Sprite star = new Sprite(rand * 0.4, rand * 10, SpriteType.STAR);
            star.setMovingXcoefficient(-1);
            star.setPosition(getRandomSpawnpoint());
            graphicManager.add(star);
        }
        if (hasTimePassed(1500 - (time / 2) > 100 ? 1500 - (time * 2) : 100)) {
            int level;
            double difficulty = 1 + Math.random() * (time / 60);
            if (difficulty < 4) {
                level = (int) Math.round(difficulty);
            } else {
                level = 4;
            }
            Character enemy = npcManager.spawnEnemy(level);
            enemy.setSpeed(enemy.getSpeed() + Math.random() * (time / 300 < 2 ? time / 300 : 2));
        }

        /** Bullets Management **/
        for (Bullet b : graphicManager.getBullets()) {
            if (b.getX() > graphicManager.getScreenWidth() || b.getX() < 0) {
                b.setToDelete(true);
            } else {
                for (Character c : graphicManager.getCharacters()) {
                    if (b.checkColides(c)) {
                        if (CharacterType.ENEMY.equals(c.getCharacterType())) {
                            Enemy e = (Enemy) c;
                            if (c.isToDelete()) {
                                playerManager.addPlayerScore(100 * e.getLevel());
                                graphicManager.updatePlayerScore(playerManager.getPlayerScore());
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean hasTimePassed(double millisecond) {
        if (time != 0) {
            if (time % (millisecond / 1000) <= GraphicManager.FRAME_TIME) {
                return true;
            }
        }
        return false;
    }

    public void start() {
        playerManager.setPlayerScore(0);
        graphicManager.setPlayer(playerManager.getPlayer());
        graphicManager.updatePlayerScore(0);
        graphicManager.updatePlayerLife();
        graphicManager.start();

        Rectangle veil = new Rectangle(graphicManager.getScreenWidth(), graphicManager.getScreenHeight());
        veil.setFill(Color.BLACK);
        veil.setX(0);
        veil.setY(0);
        graphicManager.add(veil);

        FadeTransition fadeTransition = new FadeTransition(Duration.millis(5500), veil);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);
        fadeTransition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                playerManager.enableMouseControl(true);
                playerManager.enableKeyboardControl(true);
                graphicManager.remove(veil);
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            sleep(3000);
                            timer.start();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        super.run();
                    }
                }.start();
            }
        });
        fadeTransition.play();

        ResourcesManager.getInstance().startSoundtrack(15, false);

    }

    public Point getRandomSpawnpoint() {
        Point point = new Point();
        double x = graphicManager.getScreenWidth();
        double y = Math.random() * graphicManager.getScreenHeight() * 0.8;
        point.setLocation(x, y);
        return point;
    }
}
