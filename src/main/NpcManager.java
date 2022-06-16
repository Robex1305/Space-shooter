package main;

import main.classes.*;
import main.classes.Character;

import java.awt.*;

public class NpcManager {

    private Character currentPlayer;
    private GraphicManager graphicManager;

    public NpcManager(GraphicManager graphicManager, Character currentPlayer) {
        this.graphicManager = graphicManager;
        this.currentPlayer = currentPlayer;
    }

    public Enemy spawnEnemy(int level){
        Enemy enemy = new Enemy(level);
        enemy.setTarget(currentPlayer);
        Point p = graphicManager.getRandomSpawnpointOffscreenRight();
        enemy.setPosition(p.getX(),  p.getY());
        graphicManager.add(enemy);
        return enemy;
    }
}
