package main.classes;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import main.GameManager;
import main.ResourcesManager;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

//TODO: make it extends circle for more accurate hitbox
public class Sprite extends Rectangle {
    protected ImageView skin;
    protected SpriteType spriteType;
    protected double speed;
    protected double scale;
    protected double rotationSpeed;

    protected double movingXcoefficient;
    protected double movingYcoefficient;
    protected boolean isToDelete;

    protected double createdAt;

    protected ResourcesManager resourcesManager;
    protected Integer life;

    private List<Sprite> spritesToAdd;

    public Sprite(double scale, double speed, SpriteType spriteType) {
        this(new Point(), scale, speed, spriteType);
    }

    public Sprite(Point position, double scale, double speed, SpriteType spriteType) {
        super(position.getX(), position.getY(), 0, 0);
        createdAt = GameManager.getTime();
        spritesToAdd = new ArrayList<>();
        this.resourcesManager = ResourcesManager.getInstance();
        this.speed = speed;
        this.scale = scale;
        this.spriteType = spriteType;

        this.skin = new ImageView();
        loadSkin();

        //DEBUG: hitbox
        this.setOpacity(0.0);
        this.setFill(Color.RED);
        this.life = 1;
    }

    public void setLife(Integer life) {
        this.life = life;
    }

    public Integer getLife() {
        return this.life;
    }

    public List<Sprite> getSpritesToAdd() {
        return spritesToAdd;
    }

    public void update() {
        if (!isToDelete()) {
            move();
            if (!SpriteType.PLAYER.equals(spriteType)) {
                if (getX() + getWidth() < 0) {
                    setToDelete(true);
                }
            }
        }
    }

    public void setToDelete(boolean toDelete) {
        isToDelete = toDelete;
    }

    public boolean isToDelete() {
        return isToDelete || !isAlive();
    }

    public void move() {
        setX(getX() + movingXcoefficient * speed);
        setY(getY() + movingYcoefficient * speed);
        updateImagePosition();
        if (this.rotationSpeed != 0) {
            this.getSkin().setRotate(this.skin.getRotate() + this.rotationSpeed);
        }
    }

    public void applyRotation(double delta) {
        this.rotationSpeed = delta;
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

    public void addSpeed(double speed) {
        this.speed += speed;
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

    public void adjustHitboxSize(double ratioX, double ratioY) {
        this.setScaleX(ratioX);
        this.setScaleY(ratioY);
    }

    public void adjustImageSize(double ratioX, double ratioY) {
        this.getSkin().setScaleX(ratioX);
        this.getSkin().setScaleY(ratioY);
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

    public boolean isSameTypeAs(Sprite s) {
        return this.getSpriteType().getGeneralType().equals(s.getSpriteType().getGeneralType());
    }

    public boolean colides(Sprite sprite) {
        if (!sprite.isToDelete() && !this.isToDelete() && !this.isSameTypeAs(sprite)) {
            if (this.getBoundsInParent().intersects(sprite.getBoundsInParent())) {
                return true;
            }
        }
        return false;
    }

    public boolean isAlive() {
        return getLife() > 0;
    }

}

