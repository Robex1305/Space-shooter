package main.classes;

import javafx.animation.AnimationTimer;
import main.GraphicManager;

public class Weapon {
    private double rateOfFire;
    private Integer damages;
    private AnimationTimer timer;
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
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
            }
        };

        timer.start();
    }

    public SpriteType getBulletType() {
        return bulletType;
    }

    private void update(){
        if(cooldown > 0){
            cooldown -= GraphicManager.FRAME_TIME;
        }

    }

    public double getVolume() {
        return volume;
    }


    public void setVolume(double volume) {
        this.volume = volume;
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

    public void setDamages(Integer damages) {
        this.damages = damages;
    }

    public Integer getDamages() {
        return damages;
    }

    public double getRateOfFire() {
        return rateOfFire;
    }

    public void setRateOfFire(double rateOfFire) {
        this.rateOfFire = rateOfFire;
    }

    public boolean canShoot() {
        return cooldown <= 0;
    }
}
