package main.classes;

import main.GameManager;

import java.awt.*;

public class Enemy extends Character{
    private Character target;
    private Integer level;
    private boolean folowTarget;

    public Enemy(Integer level){
        super(0,SpriteType.DEFAULT);
        this.level = level;
        folowTarget = false;

        if(level == 1) {
            setWeapon(new Weapon(0.15, 1));
            setSpriteType(SpriteType.ENEMY1);
            setLife(4);
            setSpeed(1.7);
        } else if(level == 2) {
            setWeapon(new Weapon(0.4, 1));
            setSpriteType(SpriteType.ENEMY2);
            setLife(6);
            setSpeed(2.5);
            folowTarget = true;
        } else if(level == 3) {
            setWeapon(new Weapon(0.24, 2));
            setSpriteType(SpriteType.ENEMY3);
            setLife(15);
            setSpeed(1);
        } else if(level == 4) {
            setWeapon(new Weapon(0.18, 3));
            setSpriteType(SpriteType.ENEMY4);
            setLife(30);
            setSpeed(2);
        }
    }

    public boolean isFolowTarget() {
        return folowTarget;
    }

    @Override
    public void move() {
        super.move();
        checkColides(target);
    }

    public Character getTarget() {
        return target;
    }

    public void setTarget(Character target) {
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
        if(folowTarget && target.isAlive()) {
            if (getY() < target.getY()) {
                setMovingYcoefficient(0.3);
            } else if (getY() > target.getY()) {
                setMovingYcoefficient(-0.3);
            } else {
                setMovingYcoefficient(0);
            }
        }
    }
}
