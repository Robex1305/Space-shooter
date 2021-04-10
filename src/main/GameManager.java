package main;

import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.classes.*;
import main.classes.Character;

public class GameManager {
    protected Stage stage;
    protected GraphicManager graphicManager;
    protected PlayerManager playerManager;
    protected NpcManager npcManager;
    protected AnimationTimer timer;
    private boolean gameOver;
    protected double time;

    public GameManager(Stage primaryStage) {
        this.stage = primaryStage;
        init();
    }

    public void init() {
        gameOver = false;
        graphicManager = new GraphicManager(stage);
        playerManager = new PlayerManager(graphicManager);
        npcManager = new NpcManager(graphicManager, playerManager.getPlayer());
        time = 0;
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
            }
        };


        start();
    }

    public void update() {

        this.graphicManager.updatePlayerLife();
        if (playerManager.getPlayer().isAlive()) {
            time += GraphicManager.FRAME_TIME;
            if (hasTimePassed(2000 - (time / 2) > 100 ? 2000 - (time * 2) : 100)) {
                double difficulty = 1 + Math.random() * (time / 60);
                Character enemy = null;
                if (difficulty < 4) {
                    enemy = npcManager.spawnEnemy((int) Math.round(difficulty));
                } else {
                    enemy = npcManager.spawnEnemy(4);
                    boolean additionalSpawning = Math.random() >= 0.5;
                    if(additionalSpawning){
                        Character additionalEnemy = npcManager.spawnEnemy((int) (Math.random() * 4));
                        additionalEnemy.setSpeed(additionalEnemy.getSpeed() + Math.random() * (time / 300 < 2 ? time / 300 : 2));
                    }
                }
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
                                    playerManager.addPlayerScore(150 * e.getLevel());
                                    graphicManager.updatePlayerScore(playerManager.getPlayerScore());
                                }
                            }
                        }
                    }
                }
            }
        }
        else{
            if(!gameOver) {
                gameOver = true;
                Label message = graphicManager.showEndOfGameScreen();
                message.setOnMouseClicked(event -> {
                    reset();
                });
                timer.stop();
            }
        }
    }


    public void reset(){
        ResourcesManager.getInstance().reset();
        init();
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

        FadeTransition fadeTransition = new FadeTransition(Duration.millis(3500), veil);
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
                            sleep(2000);
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



    }


}
