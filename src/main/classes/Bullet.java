package main.classes;

public class Bullet extends Sprite {
    private final Weapon source;
    private boolean isExplosive;
    public Bullet(Weapon source, double scale, SpriteType spriteType) {
        super(source.getSource().getPosition(), scale, 6, spriteType);
        this.source = source;
        this.setLife(source.getDamages());


        if (SpriteType.PLAYER.equals(source.getSource().spriteType)) {
            this.setMovingXcoefficient(1);
            this.speed = 10;
            this.setPositionY(source.getSource().getY() + source.getSource().getHeight() / 2);
            this.setPositionX(source.getSource().getX() + source.getSource().getSkin().getImage().getWidth());
            if(spriteType.equals(SpriteType.PLAYER_ROCKET)){
                this.isExplosive = true;
                this.scale = 0.2;
                this.speed = 15;
            }

        } else {
            this.getSkin().setRotate(180);
            this.setMovingXcoefficient(-1);
            this.setPositionY(source.getSource().getY() + source.getSource().getHeight() / 2);
            this.setPositionX(source.getSource().getX());
        }

        updateImagePosition();
    }

    public Weapon getSource() {
        return source;
    }

    @Override
    public boolean colides(Sprite sprite) {
        if((sprite instanceof Bullet)){
            return false;
        }
        return super.colides(sprite);
    }

    public boolean isExplosive() {
        return isExplosive;
    }
}
