package main.classes;

public class Bullet extends Sprite {
    private final Character source;

    public Bullet(Character source, double scale, SpriteType spriteType) {
        super(source.getPosition(), scale, 6, spriteType);
        this.source = source;
        this.setLife(source.getWeapon().getDamages());

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
    }

    public Character getSource() {
        return source;
    }

    @Override
    public boolean colides(Sprite sprite) {
        if((sprite instanceof Bullet)){
            return false;
        }
        return super.colides(sprite);
    }
}
