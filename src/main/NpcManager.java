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

    public Character spawnEnemy(int level){
        Enemy enemy = new Enemy(level);

        assert enemy != null;
        enemy.setTarget(currentPlayer);
        enemy.setPosition(graphicManager.getScreenWidth(), 50 + Math.random() * (graphicManager.getScreenHeight()-100));
        enemy.setIsShooting(true);
        graphicManager.add(enemy);
        return enemy;
    }
}
