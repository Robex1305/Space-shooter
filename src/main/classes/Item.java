package main.classes;

import java.awt.*;

public class Item extends Sprite{
    SpriteModificator modificator;
    public Item(Point position, double scale, double speed, SpriteType spriteType, SpriteModificator modificator) {
        super(position, scale, speed, spriteType);
        this.setMovingXcoefficient(-1);
        this.modificator = modificator;
        this.life = 1;
    }

    public void applyEffect(Sprite sprite){
        modificator.applyEffect(sprite);
    }

}
