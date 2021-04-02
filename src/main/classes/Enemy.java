package main.classes;

import main.GameManager;

import java.awt.*;

public class Enemy extends Character{
    private Sprite target;
    private Integer level;

    public Enemy(Integer level){
        super(0,SpriteType.DEFAULT);
        this.level = level;

        if(level == 1) {
            setWeapon(new Weapon(0.3, 1));
            setSpriteType(SpriteType.ENEMY1);
            setSpeed(1);
        } else if(level == 2) {
            setWeapon(new Weapon(0.6, 1));
            setSpriteType(SpriteType.ENEMY2);
            setSpeed(1.5);
        } else if(level == 3) {
            setWeapon(new Weapon(0.5, 2));
            setSpriteType(SpriteType.ENEMY3);
            setSpeed(0.7);
        } else if(level == 4) {
            setWeapon(new Weapon(0.5, 3));
            setSpriteType(SpriteType.ENEMY4);
            setSpeed(1.5);
        }

        loadSkin();
    }

    public Sprite getTarget() {
        return target;
    }

    public void setTarget(Sprite target) {
        this.target = target;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    @Override
    protected void update() {
        super.update();
        if(getY() < target.getY()){
            setMovingYcoefficient(0.3);
        }
        else if(getY() > target.getY()){
            setMovingYcoefficient(-0.3);
        }
        else {
            setMovingYcoefficient(0);
        }
    }
}
