package main.classes;

import main.GraphicManager;
import main.ResourcesManager;

public class Weapon {
    private double rateOfFire;
    private Integer damages;
    private double cooldown;
    private String shootingSound;
    private double volume;
    private SpriteType bulletType;

    private int ammo;

    private Character source;

    public Weapon(Character source, double rateOfFire, Integer damages, String shootingSound, SpriteType bulletSkin, int volume){
        this(source, rateOfFire, damages, shootingSound, bulletSkin);
        this.volume = volume;
    }

    public Weapon(Character source, double rateOfFire, Integer damages, String shootingSound, SpriteType bulletSkin){
        this(source, rateOfFire, damages);
        this.shootingSound = shootingSound;
        this.bulletType = bulletSkin;
        this.source = source;
        this.ammo = -1;
    }

    public Weapon(Character source, double rateOfFire, Integer damages){
        this.damages = damages;
        this.rateOfFire = rateOfFire;
        this.volume = 5;
        this.source = source;
        this.ammo = -1;
    }

    public Character getSource() {
        return source;
    }

    public int getAmmo(){
        return ammo;
    }

    public void shoot(){
        if(getAmmo() == 0){
            return;
        }
        else if(getAmmo() > 0){
            setAmmo(getAmmo() - 1);
        }
        Bullet bullet = null;

        if(GeneralType.PLAYER.equals(this.source.getSpriteType().getGeneralType())) {
            bullet = new Bullet(this, 1, this.getBulletType());
            bullet.setMovingXcoefficient(1);
            bullet.setSpriteType(this.getBulletType());
            ResourcesManager.getInstance().playSound(this.getShootingSound(), this.getVolume());
        }
        else {

            switch (this.source.getSpriteType()) {
                case ENEMY1:
                    bullet = new Bullet(this, 1, SpriteType.ENEMY1_BULLET);
                    break;
                case ENEMY2:
                    bullet = new Bullet(this, 2, SpriteType.ENEMY2_BULLET);
                    break;
                case ENEMY3:
                    bullet = new Bullet(this, 3, SpriteType.ENEMY3_BULLET);
                    break;
                case ENEMY4:
                    bullet = new Bullet(this, 4, SpriteType.ENEMY4_BULLET);
                    break;
                default:
                    bullet = new Bullet(this, 4, SpriteType.DEFAULT);
                    break;
            }

            bullet.setMovingXcoefficient(-1);

        }

        if(this.source instanceof Enemy){
            if(!((Enemy) this.source).getTarget().isAlive()){
                this.source.setIsShooting(true);
            }
        }

        if(this.source.isShooting || this.source.isShootingSecondary) {
            this.source.getSpritesToAdd().add(bullet);
            setOnCooldown();
        }
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
        //Ammo = -1 --> infinite ammo;
        return cooldown <= 0 && (getAmmo() > 0 || getAmmo() == -1);
    }

    public void setAmmo(int amount) {
        this.ammo = amount;
    }
}
