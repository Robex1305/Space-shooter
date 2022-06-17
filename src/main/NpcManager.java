package main;

import jdk.nashorn.internal.ir.debug.JSONWriter;
import jdk.nashorn.internal.runtime.Context;
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

    public Enemy spawnEnemy(int level) {
        if (level > Enemy.MAX_LEVEL) {
            level = Enemy.MAX_LEVEL;
        }
        //TODO: Use factory to handle unsupported cases like level not supported
        Enemy enemy = new Enemy(level);
        enemy.setLife((int) (enemy.getLife().doubleValue() * (1 + GameManager.globalMultiplier/5)));
        enemy.setTarget(currentPlayer);
        Point p = graphicManager.getRandomSpawnpointOffscreenRight();
        enemy.setPosition(p.getX(), p.getY());
        graphicManager.add(enemy);
        return enemy;
    }
}
