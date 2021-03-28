package main;

import javafx.scene.Scene;
import javafx.stage.Stage;
import main.GraphicManager;
import main.classes.Character;
import main.classes.Type;
import main.classes.Weapon;

import java.awt.*;

public class NpcManager {

    private GraphicManager graphicManager;

    public NpcManager(GraphicManager graphicManager) {
        this.graphicManager = graphicManager;
    }

    public Character spawnEnemy(int level){
        Character enemy = null;

        if(level == 1){
            enemy = new Character(1, Type.ENEMY1);
            enemy.setWeapon(new Weapon(0.2,1));
        } else if(level == 2){
            enemy = new Character(1, Type.ENEMY2);
            enemy.setWeapon(new Weapon(0.5,1));
        } else if(level == 3){
            enemy = new Character(1, Type.ENEMY3);
            enemy.setWeapon(new Weapon(0.3,3));
        } else if(level == 4){
            enemy = new Character(1, Type.ENEMY4);
            enemy.setWeapon(new Weapon(0.5,5));
        }
        enemy.setIsShooting(true);
        graphicManager.add(enemy);
        return enemy;
    }
}
