package main.classes;

import main.FilesName;
import main.GraphicManager;
import main.ResourcesManager;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Character extends Sprite {
    private Weapon weapon;
    protected boolean isShooting;
    private Integer life;
    private List<Sprite> spritesToAdd;

    public Character(double speed, Type type){
        this(new Point(), speed, type);
    }
    public Character(Point position, double speed, Type type) {
        this(position, 2, speed, type);
    }
    public Character(Point position, double scale, double speed, Type type) {
        super(position, scale, speed, type);
        spritesToAdd = new ArrayList<>();
        this.weapon = new Weapon(1,1);
        this.life = 10;

        Point p = new Point();

        if(!Type.PLAYER.equals(type)){
            p.setLocation(GraphicManager.SCREEN_WIDTH, Math.random() * 720);
            this.movingXcoefficient = -1;
        } else {
            p.setLocation(0, GraphicManager.SCREEN_HEIGHT/2);
        }

        setPosition(p);
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    public void setIsShooting(boolean shooting){
        this.isShooting = shooting;
    }

    //TODO Rework this shit
    public void shoot(){
        if(this.weapon.canShoot()) {
            Bullet bullet = null;
            switch (type) {
                case PLAYER:
                    bullet = new Bullet(this, 1, Type.PLAYER_BULLET);
                    bullet.setMovingXcoefficient(1);
                    ResourcesManager.getInstance().playSound(FilesName.SHOOT);
                    break;
                case ENEMY1:
                    bullet = new Bullet(this,1, Type.ENEMY1_BULLET);
                    bullet.setMovingXcoefficient(-1);
                    break;
                case ENEMY2:
                    bullet = new Bullet(this,2, Type.ENEMY2_BULLET);
                    bullet.setMovingXcoefficient(-1);
                    break;
                case ENEMY3:
                    bullet = new Bullet(this,3, Type.ENEMY3_BULLET);
                    bullet.setMovingXcoefficient(-1);
                    break;
                case ENEMY4:
                    bullet = new Bullet(this,4, Type.ENEMY4_BULLET);
                    bullet.setMovingXcoefficient(-1);
                    break;
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
        super.update();
        if(isShooting){
            shoot();
        }
    }

    @Override
    public boolean isToDelete() {
        if(this.life <= 0){
            isToDelete = true;
        }
        return  isToDelete;
    }

    public void takeDamages(Bullet bullet){
        Integer damages = bullet.getSource().getWeapon().getDamages();
        this.life -= damages;
    }
}
