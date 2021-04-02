package main.classes;

import javafx.animation.AnimationTimer;
import main.GraphicManager;

public class Weapon {
    private double rateOfFire;
    private Integer damages;
    private AnimationTimer timer;
    private double cooldown;

    public Weapon(){}

    public Weapon(double rateOfFire, Integer damages){
        this.damages = damages;
        this.rateOfFire = rateOfFire;

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
            }
        };

        timer.start();
    }

    private void update(){
        if(cooldown > 0){
            cooldown -= GraphicManager.FRAME_TIME;
        }

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
