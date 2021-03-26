package main.classes;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import main.GraphicManager;
import main.ResourcesManager;

import java.awt.*;

public class Bullet extends Sprite{
    private Sprite source;
    private double lifespan;
    private boolean lifespanElapsed;

    public Bullet(Sprite source) {
        super(source.getPosition(),0.2, 20, Type.BULLET);
        this.source = source;

        this.setY(source.getY() + source.getHeight());
        updateImagePosition();

        if(Type.PLAYER.equals(this.source.type)){
            this.setMovingXcoefficient(1);
        }
        else{
            this.setMovingXcoefficient(-1);
        }
        this.setFill(Color.DARKRED);
        lifespan = 0;
        lifespanElapsed = false;
    }

    @Override
    public void update() {
        lifespan += GraphicManager.FRAME_TIME;
        if(lifespan >= 2){
            lifespanElapsed = true;
        }
    }

    @Override
    public boolean isToDelete() {
        return lifespanElapsed;
    }

    @Override
    public void updateImagePosition(){
        if(this.skin != null) {
            skin.setX(getX());
            skin.setY(getY());
        }
    }
}
