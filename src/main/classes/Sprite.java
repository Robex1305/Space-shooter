package main.classes;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import main.FilesName;
import main.ResourcesManager;

import java.awt.*;

public class Sprite extends Rectangle {
    protected ImageView skin;
    protected Type type;
    protected double speed;

    private double movingXcoefficient;
    private double movingYcoefficient;

    private ResourcesManager resourcesManager;

    public Sprite(Point position, double speed, Type type) {
        this(position, 64, 32, speed, type);
    }
    public Sprite(Point position, double width, double height, double speed, Type type) {
        super(position.getX(), position.getY(), width, height);
        this.resourcesManager = ResourcesManager.getInstance();
        this.speed = speed;
        this.type = type;

        Image image = null;
        switch (type) {
            case PLAYER:
                image = new Image(ResourcesManager.getInstance().getFile(FilesName.PLAYER));
                break;
            case ENEMY:
                break;
        }
        this.skin = new ImageView(image);
        this.setOpacity(0);

    }

    public void move(){
        setX(getX() + movingXcoefficient * speed);
        setY(getY() + movingYcoefficient * speed);
        updateImagePosition();
    }

    public void updateImagePosition(){
        double diffX = skin.getImage().getWidth() - getWidth();
        double diffY = skin.getImage().getHeight() - getHeight();
        skin.setX(getX()-diffX/2);
        skin.setY(getY()-diffY/2);
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
}

enum Type {
    PLAYER,
    ENEMY,
}

