package main.classes;

import java.awt.*;

public class Health extends Sprite{
    public Health(double scale, double speed, SpriteType spriteType) {
        super(scale, speed, spriteType);
        this.setMovingXcoefficient(-1);
    }

    public Health(Point position, double scale, double speed, SpriteType spriteType) {
        super(position, scale, speed, spriteType);
        this.setMovingXcoefficient(-1);
    }
}
