package main.classes;

import main.FilesName;
import main.ResourcesManager;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Character extends Sprite {
    private Weapon weapon;
    protected boolean isShooting;
    private Integer life;
    private List<Sprite> spritesToAdd;
    private CharacterType characterType;

    public Character(double speed, SpriteType spriteType){
        this(new Point(), speed, spriteType);
    }
    public Character(Point position, double speed, SpriteType spriteType) {
        this(position, 1.5, speed, spriteType);
    }
    public Character(Point position, double scale, double speed, SpriteType spriteType) {
        super(position, scale, speed, spriteType);
        spritesToAdd = new ArrayList<>();
        this.life = 10;

        if(SpriteType.PLAYER.equals(spriteType)){
            characterType = CharacterType.PLAYER;
            this.weapon = new Weapon(2.5,1, FilesName.SHOOT1, SpriteType.PLAYER_BULLET1);
        } else {
            this.movingXcoefficient = -1;
            characterType = CharacterType.ENEMY;
            this.weapon = new Weapon(1,1);
        }
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    public void setLife(Integer life) {
        this.life = life;
    }

    public Integer getLife() {
        return this.life;
    }

    public void setIsShooting(boolean shooting){
        this.isShooting = shooting;
    }

    //TODO Rework this shit
    public void shoot(){
        if(this.weapon.canShoot()) {
            Bullet bullet = null;

            if(CharacterType.PLAYER.equals(this.characterType)) {
                bullet = new Bullet(this, 1, getWeapon().getBulletType());
                bullet.setMovingXcoefficient(1);
                bullet.setSpriteType(getWeapon().getBulletType());
                ResourcesManager.getInstance().playSound(getWeapon().getShootingSound(), getWeapon().getVolume());
            }
            else {

                switch (spriteType) {
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
                }

                //Nullpointer on bullet... HOW? THE? FUCK?
                if(bullet != null) {
                    bullet.setMovingXcoefficient(-1);
                }
            }

            boolean doShoot = true;
            if(this instanceof Enemy){
                if(!((Enemy) this).getTarget().isAlive()){
                    doShoot = false;
                }
            }

            if(doShoot && bullet != null) {
                spritesToAdd.add(bullet);
                this.weapon.setOnCooldown();
            }
        }
    }

    public boolean isAlive(){
        return life > 0;
    }

    public boolean checkColides(Character character) {
        if (character.getCharacterType() != getCharacterType()) {
            boolean colides = super.colide(character);
            if (colides) {
                character.takeDamages(life);
                takeDamages(life);
            }
            return colides;
        } else {
            return false;
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

    public CharacterType getCharacterType() {
        return characterType;
    }

    @Override
    public boolean isToDelete() {
        if(this.life <= 0){
            isToDelete = true;
        }
        return  isToDelete;
    }


    public void takeDamages(int damages){
        if(CharacterType.PLAYER.equals(characterType)){
            ResourcesManager.getInstance().playSound(FilesName.HIT, 50);
        }
        this.life -= damages;
    }
}

