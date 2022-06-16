package main;

import main.classes.*;
import main.classes.Character;

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
        enemy.setPosition(graphicManager.getScreenWidth(),  Math.random() * (graphicManager.getScreenHeight()-100) - 100);
        graphicManager.add(enemy);
        return enemy;
    }
}
