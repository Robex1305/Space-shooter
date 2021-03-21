package main.classes;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.awt.*;

public class Bullet extends Sprite{
    private Sprite source;
    private boolean lifespanElapsed;

    public Bullet(Sprite source) {
        super(source.getPosition(),0.2, 5, Type.BULLET);
        this.source = source;
        if(Type.PLAYER.equals(this.source.type)){
            this.setMovingXcoefficient(1);
        }
        else{
            this.setMovingXcoefficient(-1);
        }
        this.setFill(Color.DARKRED);
        lifespanElapsed = false;

        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(2000);
                    lifespanElapsed = true;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public boolean hasExpired(){
        return lifespanElapsed;
    }

}
