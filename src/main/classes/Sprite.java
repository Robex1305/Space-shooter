package main.classes;

import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import main.GraphicManager;
import main.ResourcesManager;

import java.awt.*;

public class Sprite extends Rectangle {
    protected ImageView skin;
    protected SpriteType spriteType;
    protected double speed;
    protected double scale;

    protected double movingXcoefficient;
    protected double movingYcoefficient;
    protected boolean isToDelete;

    protected AnimationTimer timer;

    protected ResourcesManager resourcesManager;

    private Weapon weapon;

    public Sprite(double scale, double speed, SpriteType spriteType) {
        this(new Point(), scale, speed, spriteType);
    }

    public Sprite(Point position, double scale, double speed, SpriteType spriteType) {
        super(position.getX(), position.getY(), 0, 0);
        this.resourcesManager = ResourcesManager.getInstance();
        this.speed = speed;
        this.scale = scale;
        this.spriteType = spriteType;

        this.skin = new ImageView();
        loadSkin();

        //debug hitbox
        this.setOpacity(0);
        this.setFill(Color.RED);

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
            }
        };

        timer.start();
    }

    protected void update() {
        if(!isToDelete()) {
            move();
            if (!SpriteType.PLAYER.equals(spriteType)) {
                if (getX() + getWidth() < 0) {
                    isToDelete = true;
                }
            }
        }
    }

    public void stopTimer() {
        //Another unexplained nullpointer here...WTF IS GOING ON
        if(this.timer != null) {
            this.timer.stop();
        }
    }

    public void setToDelete(boolean toDelete) {
        isToDelete = toDelete;
    }

    public boolean isToDelete() {
        return isToDelete;
    }

    public void move() {
        setX(getX() + movingXcoefficient * speed);
        setY(getY() + movingYcoefficient * speed);
        updateImagePosition();
    }

    public void setPosition(double x, double y) {
        Point p = new Point();
        p.setLocation(x, y);
        this.setPosition(p);
    }

    public void setPosition(Point position) {
        this.setPositionX(position.getX());
        this.setPositionY(position.getY());
    }


    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getSpeed() {
        return speed;
    }

    public SpriteType getSpriteType() {
        return spriteType;
    }

    public void setSpriteType(SpriteType spriteType) {
        this.spriteType = spriteType;
        loadSkin();
    }

    public void loadSkin() {
        loadSkin(0, 0, 0);
    }

    public void loadSkin(double rotate, double dx, double dy) {
        Image image = ResourcesManager.getInstance().getAssociatedImage(spriteType, scale);
        this.skin.setFitWidth(image.getRequestedWidth());
        this.skin.setFitHeight(image.getRequestedHeight());
        this.setWidth(image.getRequestedWidth());
        this.setHeight(image.getRequestedHeight());

        if (this.skin == null) {
            this.skin = new ImageView();
        }
        this.getSkin().setImage(image);

        if (dx != 0) {
            this.getSkin().setX(this.getSkin().getX() + dx);
        }

        if (dy != 0) {
            this.getSkin().setX(this.getSkin().getY() + dy);
        }

        this.getSkin().setRotate(rotate);
        this.updateImagePosition();
    }

    public void updateImagePosition() {
        if (this.skin != null) {
            skin.setX(getX());
            skin.setY(getY());
        }
    }

    public void setPositionX(double x) {
        this.setX(x);
        updateImagePosition();
    }

    public void setPositionY(double y) {
        this.setY(y);
        updateImagePosition();
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

    public Point getPosition() {
        Point position = new Point();
        position.setLocation(getX(), getY());
        return position;
    }

    public boolean colide(Sprite sprite) {
        if (!sprite.isToDelete) {
            if (this.getBoundsInParent().intersects(sprite.getBoundsInParent())) {
                return true;
            }
        }
        return false;
    }

}

