package main.classes;

import main.FilesName;
import main.ResourcesManager;

import java.awt.*;

public class Health extends Sprite {
    public Health(Point position, double scale, double speed, SpriteType spriteType) {
        super(position, scale, speed, spriteType);
        this.setMovingXcoefficient(-1);
        this.life = 1;
    }

}
