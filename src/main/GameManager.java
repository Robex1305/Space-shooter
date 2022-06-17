package main;

import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.classes.*;
import main.classes.Character;

import java.awt.Point;
import java.util.*;

public class GameManager {
    protected Stage stage;
    protected GraphicManager graphicManager;
    protected PlayerManager playerManager;
    protected NpcManager npcManager;

    protected Timer timer;
    private boolean gameOver;
    protected double time;

    private Character player;

    public static double globalMultiplier = 1.0;
    public static final double timeUntilEndgameInSeconds = 900.0;

    public GameManager(Stage primaryStage) {
        this.stage = primaryStage;
        init();
    }

    public void init() {
        globalMultiplier = 1.0;
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }
        time = 0.0;
        gameOver = false;
        graphicManager = new GraphicManager(stage);
        playerManager = new PlayerManager(graphicManager);
        npcManager = new NpcManager(graphicManager, playerManager.getPlayer());
    }

    public void initTimer() {
        time = 0.0;
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                update();
                time += GraphicManager.FRAME_TIME;
                graphicManager.setTime(time);
            }
        }, 0, Math.round(GraphicManager.FRAME_TIME * 1000));
    }

    public void update() {
        player = playerManager.getPlayer();
        if (globalMultiplier < Enemy.MAX_LEVEL) {
            globalMultiplier = 1.0 + (time / timeUntilEndgameInSeconds * 4.0);
        }
        if (player != null) {
            this.graphicManager.updatePlayerLife();
            if (playerManager.getPlayer().isAlive()) {
                spawnEnemies();
                checkSpawnableItems();
                manageHit();
            } else {
                if (!gameOver) {
                    gameOver = true;
                    Label message = graphicManager.showEndOfGameScreen();
                    message.setOnMouseClicked(event -> reset());
                }
            }
        }
    }

    private void manageHit() {
        // Manage bullets
        List<Bullet> bullets = new ArrayList<>(graphicManager.getBullets());
        List<Character> characters = new ArrayList<>(graphicManager.getCharacters());

        //We retrieve each bullets on screen and check if they are colliding with something
        for (Bullet b : bullets) {
            for (Character c : characters) {
                //"checkCollides(...)" checks collision but also applies damages
                if (b.checkColides(c)) {
                    //if the enemy ha sto be deleted (ex: is dead), 4% chances it drops a health item
                    if (CharacterType.ENEMY.equals(c.getCharacterType())) {
                        Enemy e = (Enemy) c;
                        if (c.isToDelete()) {
                            if (Math.random() > 0.9) {
                                dropHealth(c);
                            }
                            playerManager.addPlayerScore((int) ((e.getLevel() < 1 ? 150.0 : 100.0 * e.getLevel()) * (globalMultiplier / 2.0)));
                        }
                    }
                }
            }
        }
    }

    private void checkSpawnableItems() {
        //Manage health items
        for (Health h : graphicManager.getHealths()) {
            if (!h.isToDelete() && h.colide(player)) {
                if (player.getLife() < 10) {
                    player.setLife(player.getLife() + 1);
                }
                else {
                    playerManager.addPlayerScore(25);
                }
                ResourcesManager.getInstance().playSound(FilesName.HEAL, 100);
                h.setToDelete(true);
            }
        }
    }

    public int calculateEnemyLevel() {
        return (int) (Enemy.MAX_LEVEL * Math.round((Math.random() * globalMultiplier)) / Enemy.MAX_LEVEL);
    }

    private void spawnEnemies() {
        if (hasTimePassed(3500 / (globalMultiplier))) {
            npcManager.spawnEnemy(calculateEnemyLevel()).addSpeed(Math.random() * globalMultiplier);
            //0 to 25% chance of spawning an additional enemy over time
            if (Math.random() <= globalMultiplier / 20) {
                npcManager.spawnEnemy(calculateEnemyLevel()).addSpeed(Math.random() * globalMultiplier);
            }
        }
    }


    public void dropHealth(Character c) {
        Point p = new Point();
        p.setLocation(c.getX(), c.getY() + c.getHeight() / 2);
        Health health = new Health(c.getPosition(), 1, 1, SpriteType.HEART);
        graphicManager.add(health);
    }

    public void reset() {
        ResourcesManager.getInstance().reset();
        init();
        start();
    }

    private double lastTime = 0;

    public boolean hasTimePassed(double millisecond) {
        millisecond /= 1000;
        if (time == 0 || lastTime + millisecond <= time) {
            lastTime = time;
            return true;
        }
        return false;
    }

    public void start() {
        ResourcesManager.getInstance().startSoundtrack(15, false);

        playerManager.setPlayerScore(0);
        graphicManager.setPlayer(playerManager.getPlayer());
        graphicManager.updatePlayerScore(playerManager.getPlayerScore() + "/" + playerManager.getNextScoreRewardStep());
        graphicManager.updatePlayerLife();
        graphicManager.start();

        Rectangle veil = new Rectangle(graphicManager.getScreenWidth(), graphicManager.getScreenHeight());
        veil.setId("startingVeil");
        veil.setFill(Color.BLACK);
        veil.setX(0);
        veil.setY(0);
        graphicManager.add(veil);

        FadeTransition fadeTransition = new FadeTransition(Duration.millis(3000), veil);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);
        fadeTransition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                playerManager.enableMouseControl(true);
                playerManager.enableKeyboardControl(false);
                graphicManager.remove(veil);
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            sleep(5500);
                            initTimer();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        super.run();
                    }
                }.start();
            }
        });
        fadeTransition.play();


    }


}
