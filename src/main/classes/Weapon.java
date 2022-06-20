package main.classes;

import main.GraphicManager;

public class Weapon {
    private double rateOfFire;
    private Integer damages;
    private double cooldown;
    private String shootingSound;
    private double volume;
    private SpriteType bulletType;

    public Weapon(double rateOfFire, Integer damages, String shootingSound, SpriteType bulletSkin){
        this(rateOfFire, damages);
        this.shootingSound = shootingSound;
        this.bulletType = bulletSkin;
    }

    public Weapon(double rateOfFire, Integer damages){
        this.damages = damages;
        this.rateOfFire = rateOfFire;
        this.volume = 5;
    }

    public SpriteType getBulletType() {
        return bulletType;
    }

    public void updateCooldown(){
        if(cooldown > 0){
            cooldown -= GraphicManager.FRAME_TIME;
        }
    }

    public double getVolume() {
        return volume;
    }

    public String getShootingSound() {
        return shootingSound;
    }

    private double calculateCooldown(){
        return 1/rateOfFire;
    }

    public void setOnCooldown(){
        this.cooldown = calculateCooldown();
    }

    public Integer getDamages() {
        return damages;
    }

    public boolean canShoot() {
        return cooldown <= 0;
    }

}
