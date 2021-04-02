package main;

import javafx.animation.AnimationTimer;
import javafx.stage.Stage;
import main.classes.*;
import main.classes.Character;

import java.awt.*;

public class GameManager {
    protected GraphicManager graphicManager;
    protected PlayerManager playerManager;
    protected NpcManager npcManager;
    protected AnimationTimer timer;
    protected double time;

    public GameManager(Stage primaryStage){
        graphicManager = new GraphicManager(primaryStage);
        playerManager = new PlayerManager(graphicManager);
        npcManager = new NpcManager(graphicManager, playerManager.getPlayer());
        playerManager.enableMouseControl(true);

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
            }
        };
    }

    public void update() {

        time += GraphicManager.FRAME_TIME;
        playerManager.getPlayer().getSkin().toFront();
        if (Math.random() < 0.05) {
            Point p = new Point();
            Sprite star = new Sprite(5, 5, SpriteType.STAR);
            graphicManager.add(star);
        }
        if (hasTimePassed(3000)) {
            int level = (int) (1 + Math.random() * 3);
            npcManager.spawnEnemy(level);
        }

        for(Bullet b : graphicManager.getBullets()){
            for(Character c : graphicManager.getCharacters()){
                if(b.checkColides(c)) {
                    if (CharacterType.PLAYER.equals(c.getCharacterType())) {
                        graphicManager.updatePlayerLife();
                    } else if (CharacterType.ENEMY.equals(c.getCharacterType())) {
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

    public boolean hasTimePassed(double millisecond){
        if(time != 0) {
            if (time % (millisecond/1000) <= GraphicManager.FRAME_TIME) {
                return true;
            }
        }
        return false;
    }

    public void start(){
        playerManager.setPlayerScore(0);
        graphicManager.setPlayer(playerManager.getPlayer());
        graphicManager.updatePlayerScore(0);
        graphicManager.updatePlayerLife();
        graphicManager.start();
        timer.start();

    }

}
