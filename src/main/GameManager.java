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

    protected AnimationTimer animationTimer;
    protected Timer timer;
    private boolean gameOver;
    protected double time;

    private Character player;

    public static int speedMultiplier = 0;
    public static final int timeUntilEndgameInSeconds = 1800;

    public GameManager(Stage primaryStage) {
        this.stage = primaryStage;
        init();
    }

    public void init() {
        if(timer != null){
            timer.cancel();
            timer.purge();
        }
        time = 0;
        gameOver = false;
        graphicManager = new GraphicManager(stage);
        playerManager = new PlayerManager(graphicManager);
        npcManager = new NpcManager(graphicManager, playerManager.getPlayer());
    }

    public void initTimer(){
        time = 0;
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                update();
                time += GraphicManager.FRAME_TIME;
                graphicManager.setTime(time);
            }
        },0,Math.round(GraphicManager.FRAME_TIME*1000));
    }

    public void update() {
        player = playerManager.getPlayer();
        if (speedMultiplier < 5){
            speedMultiplier = (int) Math.round(time / timeUntilEndgameInSeconds * 5);
            System.out.println(speedMultiplier);
        }
        if(player != null) {
            this.graphicManager.updatePlayerLife();
            if (playerManager.getPlayer().isAlive()) {
                spawnEnemies();
                checkSpawnableItems();
                manageHit();
            } else {
                if (!gameOver) {
                    gameOver = true;
                    Label message = graphicManager.showEndOfGameScreen();
                    message.setOnMouseClicked(event -> {
                        reset();
                    });
                }
            }
        }
    }

    private void manageHit() {
        // Manage bullets
        List<Bullet> bullets = new ArrayList<Bullet>(graphicManager.getBullets());
        List<Character> characters = new ArrayList<Character>(graphicManager.getCharacters());

        //We retrieve each bullets on screen and check if they are coliding with something
        for (Bullet b : bullets) {
            for(Character c : characters) {
                //"checkColides(...)" checks colision but also applies damages
                if (b.checkColides(c)) {
                    //if the enemy ha sto be deleted (ex: is dead), 4% chances it drops a health item
                    if (CharacterType.ENEMY.equals(c.getCharacterType())) {
                        Enemy e = (Enemy) c;
                        System.out.println("HIT");
                        if (c.isToDelete()) {
                            if (Math.random() > 0.96) {
                                dropHealth(c);
                            }
                            playerManager.addPlayerScore(e.getLevel() < 1 ? 150 : 100 * e.getLevel());
                            graphicManager.updatePlayerScore(playerManager.getPlayerScore());
                        }
                    }
                }
            }
        }
    }

    private void checkSpawnableItems() {
        //Manage health items
        for (Health h : graphicManager.getHealths()) {
            if (h.colide(player) && player.getLife() < 10) {
                if (!h.isToDelete()) {
                    player.setLife(player.getLife() + 1);
                    ResourcesManager.getInstance().playSound(FilesName.HEAL, 100);
                    h.setToDelete(true);
                }
            }
        }
    }

    private void spawnEnemies() {
        // The delay between enemy spawning is 2250ms. As time passes, this delay is shortened (2250 - time) to increase difficulty. A
        // When time is 100 from the delay, it cannot go lower, 100 will be the limit
        // Ex: if you play for an hour, 2250(ms) - 3600(s) = -1350, causing problem.
        // Also, if you play for an hour, you're an absolute madlad and I thank you for enjoying it
        Enemy enemy = null;

        boolean additionalSpawningChance = Math.random() >= 0.7;

        if (hasTimePassed(2250.0 / speedMultiplier)) {
            if(additionalSpawningChance){
                npcManager.spawnEnemy(0);
            }
            double difficulty = 1 + Math.random() * (time / 90);
            if (difficulty < 4) { //while difficulty can increase, we spawn one ennemy at time
                enemy = npcManager.spawnEnemy((int) Math.round(difficulty));

            } else { //when we reach end-game, we always spawn level-4 enemies + an extra
                enemy = npcManager.spawnEnemy(4);
                if (additionalSpawningChance) {
                    Character additionalEnemy = npcManager.spawnEnemy((int) (Math.random() * speedMultiplier));
                    additionalEnemy.setSpeed(additionalEnemy.getSpeed() + Math.random() * speedMultiplier);
                }
            }
            //Speed is randomly set + modifier depending on time spent on the game
            enemy.setSpeed(enemy.getSpeed() + Math.random() * speedMultiplier);
        }
    }

    public void dropHealth(Character c){
        Point p = new Point();
        p.setLocation(c.getX(), c.getY() + c.getHeight()/2);
        Health health = new Health(c.getPosition(), 1, 1, SpriteType.HEART);
        graphicManager.add(health);
    }

    public void reset(){
        ResourcesManager.getInstance().reset();
        init();
        start();
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
        ResourcesManager.getInstance().startSoundtrack(15, false);

        playerManager.setPlayerScore(0);
        graphicManager.setPlayer(playerManager.getPlayer());
        graphicManager.updatePlayerScore(0);
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
