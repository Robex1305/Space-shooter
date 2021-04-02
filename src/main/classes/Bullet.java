package main.classes;

import main.GraphicManager;

public class Bullet extends Sprite{
    private Character source;
    private double lifespan;
    private boolean lifespanElapsed;


    public Bullet(Character source, double scale, SpriteType spriteType) {
        super(source.getPosition(),scale, 6, spriteType);
        this.source = source;

        if(SpriteType.PLAYER.equals(source.spriteType)) {
            this.setMovingXcoefficient(1);
            this.setPositionY(source.getY() + source.getHeight()/2);
            this.setPositionX(source.getX() + source.getSkin().getImage().getWidth());
        }
        else {
            this.getSkin().setRotate(180);
            this.setMovingXcoefficient(-1);
            this.setPositionY(source.getY() + source.getHeight()/2);
            this.setPositionX(source.getX());
        }

        updateImagePosition();

        lifespan = 0;
        lifespanElapsed = false;
    }

    @Override
    public void update() {
        super.update();
        lifespan += GraphicManager.FRAME_TIME;
        if(lifespan >= 5){
            lifespanElapsed = true;
        }
    }

    @Override
    public boolean isToDelete() {
        return lifespanElapsed || getX() > GraphicManager.SCREEN_WIDTH;
    }


    public boolean checkColides(Character character) {
        if(!lifespanElapsed) {
            if (character.getCharacterType() != source.getCharacterType()) {

                boolean colides = super.colide(character);
                if (colides) {
                    lifespan = 0;
                    lifespanElapsed = true;
                    character.takeDamages(this);
                }
                return colides;
            }
        }
        else{
            return false;
        }
        return false;
    }

    public Character getSource(){
        return source;
    }
}
