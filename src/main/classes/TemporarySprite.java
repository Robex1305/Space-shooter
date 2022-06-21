package main.classes;

import main.GraphicManager;

import java.awt.*;

public class TemporarySprite extends Sprite {

    protected double lifespan;
    protected boolean lifespanElapsed;
    protected double maxLifespan;

    public TemporarySprite(double scale, double speed, SpriteType spriteType, double maxLifespan) {
        super(scale, speed, spriteType);
        this.maxLifespan = maxLifespan;
    }

    public TemporarySprite(Point position, double scale, double speed, SpriteType spriteType, double maxLifespan) {
        super(position, scale, speed, spriteType);
        this.maxLifespan = maxLifespan;
    }

    @Override
    public boolean isToDelete() {
        return lifespanElapsed;
    }


    @Override
    public void update() {
        super.update();
        lifespan += GraphicManager.FRAME_TIME;
        if(lifespan >= maxLifespan){
            lifespanElapsed = true;
        }
    }
}
