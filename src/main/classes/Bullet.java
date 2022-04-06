package main.classes;

import main.GraphicManager;

public class Bullet extends TemporarySprite {
    private Character source;


    public Bullet(Character source, double scale, SpriteType spriteType) {
        super(source.getPosition(), scale, 6, spriteType, 5);
        this.source = source;

        if (SpriteType.PLAYER.equals(source.spriteType)) {
            this.setMovingXcoefficient(1);
            this.speed = 10;
            this.setPositionY(source.getY() + source.getHeight() / 2);
            this.setPositionX(source.getX() + source.getSkin().getImage().getWidth());
        } else {
            this.getSkin().setRotate(180);
            this.setMovingXcoefficient(-1);
            this.setPositionY(source.getY() + source.getHeight() / 2);
            this.setPositionX(source.getX());
        }

        updateImagePosition();

        lifespan = 0;
        lifespanElapsed = false;
    }


    /**
     * Check if bullet colides with a character and apply damages if it does.
     * It also verify that the character who shot "this" bullet is not the same type as the character it collides with
     * to avoid scenarios where enemies shoot each others
     *
     * @param character
     * @return
     */
    public boolean checkColides(Character character) {
        if (!lifespanElapsed) {
            if (character.getCharacterType() != source.getCharacterType()) {
                boolean colides = super.colide(character);
                if (colides) {
                    lifespan = 0;
                    lifespanElapsed = true;
                    character.takeDamages(this.getSource().getWeapon().getDamages());
                }
                return colides;
            }
        } else {
            return false;
        }
        return false;
    }

    public Character getSource() {
        return source;
    }
}
