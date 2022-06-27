package main;

import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.classes.Character;
import main.classes.*;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class GameManager {
    protected Stage stage;
    protected GraphicManager graphicManager;
    protected PlayerManager playerManager;
    protected NpcManager npcManager;

    protected AnimationTimer timer;
    private boolean gameOver;
    protected static double time;

    private Character player;

    public static double globalMultiplier = 1.0;
    public static final double timeUntilEndgameInSeconds = 900.0;

    public GameManager(Stage primaryStage) {
        this.stage = primaryStage;
        init();
    }

    public static double getTime() {
        return time;
    }

    public void init() {
        globalMultiplier = 1.0;
        if (timer != null) {
            timer.stop();
        }
        time = 0.0;
        lastTime = 0.0;
        timer = new AnimationTimer() {
            @Override
            public void handle(long l) {

                if(!playerManager.isGamePaused() || gameOver) {
                    graphicManager.hidePause();
                    update();
                    graphicManager.update();
                    time += GraphicManager.FRAME_TIME;
                }
                else {
                    graphicManager.showPause();
                }
            }
        };
        graphicManager = new GraphicManager(stage);
        playerManager = new PlayerManager(graphicManager);
        npcManager = new NpcManager(graphicManager, playerManager.getPlayer());
        time = 0.0;
        gameOver = false;
        timer.start();
    }

    public void update() {
        player = playerManager.getPlayer();
        if (globalMultiplier < Enemy.MAX_LEVEL) {
            globalMultiplier = 1.0 + (time / timeUntilEndgameInSeconds * 4.0);
        }
        if (player != null) {
            this.graphicManager.updatePlayerLife();
            if (playerManager.getPlayer().isAlive()) {
                graphicManager.updatePlayerAmmo(playerManager.getPlayer().getSecondaryWeapon().getAmmo());
                spawnEnemies();
                manageColisions();
            } else {
                if (!gameOver) {
                    gameOver = true;
                    graphicManager.explodeAt(player);
                    Label message = graphicManager.showEndOfGameScreen();
                    message.setOnMouseClicked(event -> reset());
                }
            }
        }
    }

    // TODO: Find a better way to manage colision
    private void manageColisions() {
        // Manage bullets
        List<Bullet> bullets = graphicManager.getSprites().stream().filter(s -> s instanceof Bullet).map(s -> (Bullet) s).collect(Collectors.toList());
        List<Character> characters = graphicManager.getSprites().stream().filter(s -> s instanceof Character).map(s -> (Character) s).collect(Collectors.toList());
        List<Item> items = graphicManager.getSprites().stream().filter(s -> s instanceof Item).map(s -> (Item) s).collect(Collectors.toList());

        for (Item item : items) {
            if (item.colides(player)){
                item.applyEffect(player);
                item.setToDelete(true);
            }
        }
        //We retrieve each bullets on screen and check if they are colliding with something

        for (Character c : characters) {
            boolean isPlayer = c == player;
            boolean isHit = false;
            //Loop on bullet to apply damages if character colides with
            for (Bullet b : bullets) {
                if (b.colides(c)) {
                    c.setLife(c.getLife() - b.getLife());
                    b.setLife(0);
                    if(isPlayer){
                        isHit = true;
                    }
                    if(b.isExplosive()){
                        graphicManager.explodeAt(b, 3, 15);
                    }
                }
            }
            //Check if player has crashed into an enemy
            if(c instanceof Enemy) {
                Enemy e = (Enemy) c;
                if(e.isToDelete()){
                    // 10% Chance to drop life
                    if (Math.random() > 0.9) {
                        dropHealth(c);
                    }
                    // 10% chance to drop ammo
                    if (Math.random() > 0.9) {
                        dropAmmo(c);
                    }
                    playerManager.addPlayerScore((int) ((e.getLevel() < 1 ? 150.0 : 100.0 * e.getLevel()) * (globalMultiplier / 2.0)));
                }
                //No score points/health on crashing
                else if (e.colides(player)) {
                    int tmpPlayerLife = player.getLife();
                    player.setLife(player.getLife() - e.getLife());
                    e.setLife(e.getLife() - tmpPlayerLife);
                    isHit = true;
                }
            }
            if(c.isToDelete()){
                graphicManager.explodeAt(c);
            }
            if(isHit){
                ResourcesManager.getInstance().playSound(FilesName.HIT, 100);
            }
        }
    }

    public int calculateEnemyLevel() {
        return (int) (Enemy.MAX_LEVEL * Math.round((Math.random() * globalMultiplier)) / Enemy.MAX_LEVEL);
    }

    private void spawnEnemies() {
        if (hasTimePassed(8500, 0) && hasTimePassed(3500 / (globalMultiplier))) {
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
        Item item = new Item(c.getPosition(), 1, 1, SpriteType.HEART, new SpriteModificator() {
            @Override
            public void applyEffect(Sprite sprite) {
                if(sprite.getSpriteType().equals(SpriteType.PLAYER) && sprite.getLife() < 10) {
                    sprite.setLife(sprite.getLife() + 1);
                    ResourcesManager.getInstance().playSound(FilesName.HEAL, 100);
                }
            }
        });
        graphicManager.add(item);
    }

    public void dropAmmo(Character c) {
        Point p = new Point();
        p.setLocation(c.getX(), c.getY() + c.getHeight() / 2);
        Item item = new Item(c.getPosition(), 0.2, 1, SpriteType.PLAYER_ROCKET_AMMO, new SpriteModificator() {
            @Override
            public void applyEffect(Sprite sprite) {
                System.out.println("APPLIED EFFECT");
                if(sprite.getSpriteType().equals(SpriteType.PLAYER)) {
                    ResourcesManager.getInstance().playSound(FilesName.HEAL, 100);
                    ((Character)sprite).getSecondaryWeapon().setAmmo(((Character)sprite).getSecondaryWeapon().getAmmo() + 1);
                }
            }
        });
        graphicManager.add(item);
    }

    public void reset() {
        ResourcesManager.getInstance().reset();
        init();
        start();
    }

    private double lastTime = 0;

    public boolean hasTimePassed(double millisecond) {
        if (hasTimePassed(millisecond, lastTime)) {
            lastTime = time;
            return true;
        }
        return false;
    }

    public boolean hasTimePassed(double millisecond, double since) {
        millisecond /= 1000;
        return since + millisecond <= time;
    }

    public void start() {
        ResourcesManager.getInstance().startSoundtrack(15, false);

        playerManager.setPlayerScore(0);
        graphicManager.setPlayer(playerManager.getPlayer());
        graphicManager.updatePlayerScore(playerManager.getPlayerScore() + "/" + playerManager.getNextScoreRewardStep());
        graphicManager.updatePlayerAmmo(playerManager.getPlayer().getSecondaryWeapon().getAmmo());
        graphicManager.updatePlayerLife();

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
                playerManager.enableKeyboardControl(true);
                graphicManager.remove(veil);

            }
        });
        fadeTransition.play();


    }


}
