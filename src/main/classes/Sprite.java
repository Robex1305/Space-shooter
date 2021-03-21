package main.classes;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import main.FilesName;
import main.ResourcesManager;

import java.awt.*;
import java.io.InputStream;

public class Sprite extends Rectangle {
    protected ImageView skin;
    protected Type type;
    protected double speed;

    private double movingXcoefficient;
    private double movingYcoefficient;
    private boolean isShooting;

    private double timer;

    private ResourcesManager resourcesManager;

    public Sprite(Point position, double size, double speed, Type type) {
        this(position, size*75, size*27, speed, type);
    }
    public Sprite(Point position, double width, double height, double speed, Type type) {
        super(position.getX(), position.getY(), width, height);
        this.resourcesManager = ResourcesManager.getInstance();
        this.speed = speed;
        this.type = type;

        InputStream imageStream = null;
        switch (type) {
            case PLAYER:
                imageStream = ResourcesManager.getInstance().getFile(FilesName.PLAYER);
                break;
            case ENEMY:
                break;
        }
        if(imageStream != null) {
            this.skin = new ImageView(new Image(imageStream, width * 2, height * 2, true, true));
        }
        this.setFill(Color.RED);
        this.setOpacity(100);
    }

    public void move(){
        setX(getX() + movingXcoefficient * speed);
        setY(getY() + movingYcoefficient * speed);
        updateImagePosition();
    }

    public void updateImagePosition(){
        if(this.skin != null) {
            double diffX = skin.getImage().getWidth() - getWidth();
            double diffY = skin.getImage().getHeight() - getHeight();
            skin.setX(getX() - diffX / 2);
            skin.setY(getY() - diffY / 2);
        }
    }

    public void increaseTimer(){
        this.timer += 0.0167;
    }

    public void setMovingXcoefficient(double movingXcoefficient) {
        this.movingXcoefficient = movingXcoefficient;
    }

    public void setMovingYcoefficient(double movingYcoefficient) {
        this.movingYcoefficient = movingYcoefficient;
    }

    public ImageView getSkin() {
        return skin;
    }

    public Point getPosition(){
        Point position = new Point();
        position.setLocation(getX(), getY());
        return position;
    }

    public Sprite getBullet(){
        Bullet bullet = new Bullet(this);
        return bullet;
    }

    public void setShooting(boolean shooting){
        this.isShooting = shooting;
    }

    public boolean isShooting(){
        return this.isShooting;
    }
}

