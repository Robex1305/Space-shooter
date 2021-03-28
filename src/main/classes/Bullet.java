package main.classes;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import main.GraphicManager;
import main.ResourcesManager;

import java.awt.*;

public class Bullet extends Sprite{
    private Character source;
    private double lifespan;
    private boolean lifespanElapsed;


    public Bullet(Character source, double scale, Type type) {
        super(source.getPosition(),scale, 6, type);
        this.source = source;

        if(Type.PLAYER.equals(source.type)) {
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


    public boolean checkColides(Sprite sprite) {
        if(!lifespanElapsed) {
            if ((sprite instanceof Character) && sprite != source) {

                boolean colides = super.colide(sprite);
                if (colides) {
                    lifespan = 0;
                    lifespanElapsed = true;
                    ((Character) sprite).takeDamages(this);
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
