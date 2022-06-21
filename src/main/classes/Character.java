package main.classes;

import main.FilesName;
import main.ResourcesManager;

import java.awt.*;

public class Character extends Sprite {

    //TODO: Add secondary weapon for player...?
    private Weapon mainWeapon;
    private Weapon secondaryWeapon;
    protected boolean isShooting;
    protected boolean isShootingSecondary;
    private GeneralType generalType;

    public Character(double speed, SpriteType spriteType){
        this(new Point(), speed, spriteType);
    }
    public Character(Point position, double speed, SpriteType spriteType) {
        this(position, 1.5, speed, spriteType);
    }
    public Character(Point position, double scale, double speed, SpriteType spriteType) {
        super(position, scale, speed, spriteType);
        generalType = spriteType.getGeneralType();
        switch (generalType) {
            case PLAYER:
                this.mainWeapon = new Weapon(this, 2,1, FilesName.SHOOT1, SpriteType.PLAYER_BULLET1);
                this.secondaryWeapon = new Weapon(this, 2, 100, FilesName.PLAYER_ROCKET_MP3, SpriteType.PLAYER_ROCKET, 15);
                this.secondaryWeapon.setAmmo(5);
                break;
            case ENEMY:
                this.movingXcoefficient = -1;
                this.mainWeapon = new Weapon(this, 1, 1);
                break;
            case MISC:
                this.movingXcoefficient = -1;
                this.mainWeapon = new Weapon(this, 0,0);
                break;
            default:
                throw new IllegalArgumentException("Invalid sprite type");
        }
    }

    public Weapon getMainWeapon() {
        return mainWeapon;
    }
    public Weapon getSecondaryWeapon() {
        return secondaryWeapon;
    }

    public void setMainWeapon(Weapon mainWeapon) {
        this.mainWeapon = mainWeapon;
    }

    public void setIsShooting(boolean shooting){
        this.isShooting = shooting;
    }
    public void setIsShootingSecondary(boolean shooting){
        this.isShootingSecondary = shooting;
    }

    public void shoot(){
        if(isShooting && this.mainWeapon.canShoot()) {
            this.mainWeapon.shoot();
        }

        if(isShootingSecondary && this.secondaryWeapon.canShoot()) {
            this.secondaryWeapon.shoot();
        }
    }


    @Override
    public void update() {
        super.update();
        mainWeapon.updateCooldown();
        if(secondaryWeapon != null)
            secondaryWeapon.updateCooldown();
        if(isShooting || isShootingSecondary){
            shoot();
        }
    }

    public GeneralType getCharacterType() {
        return generalType;
    }
}

