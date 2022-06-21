package main.classes;

import main.GameManager;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class Enemy extends Character{

    public static final int MAX_LEVEL = 4;
    private Character target;
    private Integer level;
    private boolean folowTarget;

    public Enemy(Integer level){
        super(0, getSpriteType(level));
        this.level = level;
        folowTarget = false;
        isShooting = false;

        if(level != 0){
            isShooting = true;
        }
        if(level == 0){
            setWeapon(new Weapon(0, 0));
            double delta = 0.2 + Math.random(); //allows to have small/fast asteroids or big/slow ones (min: 0.2)
            setLife((int) (10.0*delta));
            setSpeed(1.5/delta);
            adjustHitboxSize(0.3*delta, 0.3*delta);
            adjustImageSize(0.5*delta,0.5*delta);
            applyRotation(-getSpeed());
        }
        else if(level == 1) {
            setWeapon(new Weapon(0.15, 1));
            setLife(4);
            setSpeed(1.7);
            applyRotation(-getSpeed());
        } else if(level == 2) {
            setWeapon(new Weapon(0.4, 1));
            setLife(6);
            setSpeed(2);
            folowTarget = true;
        } else if(level == 3) {
            setWeapon(new Weapon(0.24, 2));
            setLife(15);
            setSpeed(1);
        } else if(level == 4) {
            setWeapon(new Weapon(0.18, 3));
            setSpriteType(SpriteType.ENEMY4);
            setLife(30);
            setSpeed(2);
        }
        else{
            throw new IllegalArgumentException("Enemy of level " + level + " are not defined");
        }
    }

    public static SpriteType getSpriteType(int enemyLevel){
        switch (enemyLevel) {
            case 0:
                return SpriteType.ASTEROID;
            case 1:
                return SpriteType.ENEMY1;
            case 2:
                return SpriteType.ENEMY2;
            case 3:
                return SpriteType.ENEMY3;
            case 4:
                return SpriteType.ENEMY4;
            default:
                return SpriteType.DEFAULT_ENEMY;
        }
    }

    public boolean isFolowTarget() {
        return folowTarget;
    }

    @Override
    public void move() {
        super.move();
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
    public void update() {
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
