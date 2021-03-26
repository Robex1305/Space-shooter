package main.classes;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Character extends Sprite {
    private Weapon weapon;
    protected boolean isShooting;
    private Integer life;
    private List<Sprite> spritesToAdd;

    public Character(Point position, double size, double speed, Type type) {
        super(position, size, speed, type);
        spritesToAdd = new ArrayList<>();
        this.weapon = new Weapon(1,1);
        this.life = 10;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public void setIsShooting(boolean shooting){
        this.isShooting = shooting;
    }

    public void shoot(){
        if(this.weapon.canShoot()) {
            Sprite bullet = new Bullet(this);
            if(Type.PLAYER.equals(type)) {
                bullet.setMovingXcoefficient(1);
            } else if(Type.ENEMY.equals(type)) {
                bullet.setMovingXcoefficient(-1);
            }
            spritesToAdd.add(bullet);
            this.weapon.setOnCooldown();
        }
    }

    public List<Sprite> getSpritesToAdd(){
        List<Sprite> toReturn = new ArrayList<>();
        toReturn.addAll(spritesToAdd);
        spritesToAdd.clear();
        return toReturn;
    }

    @Override
    protected void update() {
        if(isShooting){
            shoot();
        }
    }

    @Override
    public boolean isToDelete() {
        return this.life <= 0;
    }
}
