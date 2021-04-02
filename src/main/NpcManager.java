package main;

import main.classes.*;
import main.classes.Character;

public class NpcManager {

    private Sprite currentPlayer;
    private GraphicManager graphicManager;

    public NpcManager(GraphicManager graphicManager, Sprite currentPlayer) {
        this.graphicManager = graphicManager;
        this.currentPlayer = currentPlayer;
    }

    public Character spawnEnemy(int level){
        Enemy enemy = new Enemy(level);


        assert enemy != null;
        enemy.setTarget(currentPlayer);
        enemy.setIsShooting(true);
        graphicManager.add(enemy);
        return enemy;
    }
}
