package main.classes;

import javafx.animation.AnimationTimer;
import main.GraphicManager;

public class Weapon {
    private double rateOfFire;
    private double damages;
    private AnimationTimer timer;
    private double cooldown;

    public Weapon(double rateOfFire, double damages){
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

    public void setRateOfFire(double rateOfFire) {
        this.rateOfFire = rateOfFire;
    }

    public boolean canShoot() {
        return cooldown <= 0;
    }
}
