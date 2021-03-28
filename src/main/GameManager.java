package main;

import javafx.animation.AnimationTimer;
import javafx.scene.Node;
import javafx.stage.Stage;
import main.classes.*;
import main.classes.Character;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class GameManager {
    protected GraphicManager graphicManager;
    protected PlayerManager playerManager;
    protected NpcManager npcManager;
    protected AnimationTimer timer;
    protected double time;

    public GameManager(Stage primaryStage){
        graphicManager = new GraphicManager(primaryStage);
        playerManager = new PlayerManager(graphicManager);
        npcManager = new NpcManager(graphicManager);

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
            Sprite star = new Sprite(5, 5, Type.STAR);
            graphicManager.add(star);
        }
        if (hasTimePassed(3000)) {
            int level = (int) (1 + Math.random() * 3);
            Character enemy = npcManager.spawnEnemy(level);
        }

        for(Bullet b : graphicManager.getBullets()){
            for(Character c : graphicManager.getCharacters()){
                b.checkColides(c);
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
        graphicManager.add(playerManager.getPlayer());
        graphicManager.start();
        timer.start();
    }

}
