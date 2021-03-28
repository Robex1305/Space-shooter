package main.classes;

import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import main.GraphicManager;
import main.ResourcesManager;

import java.awt.*;
import java.io.InputStream;

public class Sprite extends Rectangle {
    protected ImageView skin;
    protected Type type;
    protected double speed;
    protected double scale;

    protected double movingXcoefficient;
    protected double movingYcoefficient;
    protected boolean isToDelete;

    protected AnimationTimer timer;

    protected ResourcesManager resourcesManager;

    private Weapon weapon;

    public Sprite(double scale, double speed, Type type){
        this(new Point(), scale, speed, type);
        this.setPosition(getRandomSpawnpoint());
    }

    public Sprite(Point position, double scale, double speed, Type type) {
        super(position.getX(), position.getY(), 0, 0);
        this.resourcesManager = ResourcesManager.getInstance();
        this.speed = speed;
        this.scale = scale;
        this.type = type;

        Image image = ResourcesManager.getInstance().getAssociatedImage(type, scale);

        if(image != null){
            this.skin = new ImageView(image);
            this.skin.setFitWidth(image.getRequestedWidth());
            this.skin.setFitHeight(image.getRequestedHeight());
            this.setWidth(image.getRequestedWidth());
            this.setHeight(image.getRequestedHeight());
            updateImagePosition();
        }

        //debug hitbox
        this.setOpacity(1);
        this.setFill(Color.RED);

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
            }
        };

        timer.start();
    }

    public Point getRandomSpawnpoint(){
        Point point = new Point();
        double x = GraphicManager.SCREEN_WIDTH;
        double y = Math.random() * GraphicManager.SCREEN_HEIGHT*0.8;
        point.setLocation(x,y);
        return point;
    }

    protected void update() {
        move();
        if (!Type.PLAYER.equals(type)) {
            if (getX() < 0) {
                isToDelete = true;
            }
        }
    }

    public void stopTimer(){
        this.timer.stop();
    }

    public boolean isToDelete() {
        return isToDelete;
    }

    public void move(){
        setX(getX() + movingXcoefficient * speed);
        setY(getY() + movingYcoefficient * speed);
        updateImagePosition();
    }

    public void setPosition(double x, double y){
        Point p = new Point();
        p.setLocation(x,y);
        this.setPosition(p);
    }

    public void setPosition(Point position){
        this.setPositionX(position.getX());
        this.setPositionY(position.getY());
    }

    public void changeSkin(InputStream imageStream, double rotate, double dx, double dy){
        Image image = new Image(imageStream, getWidth(), getHeight(), false, true);
        this.getSkin().setImage(image);

        if(dx != 0){
            this.getSkin().setX(this.getSkin().getX() + dx);
        }

        if(dy != 0){
            this.getSkin().setX(this.getSkin().getY() + dy);
        }

        this.getSkin().setRotate(rotate);

    }

    public void updateImagePosition(){
        if(this.skin != null) {
            skin.setX(getX());
            skin.setY(getY());
        }
    }

    public void setPositionX(double x){
        this.setX(x);
        updateImagePosition();
    }

    public void setPositionY(double y){
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

    public Point getPosition(){
        Point position = new Point();
        position.setLocation(getX(), getY());
        return position;
    }

    public boolean colide(Sprite sprite){
        if(this.getBoundsInParent().intersects(sprite.getBoundsInParent())){
            return true;
        }
        else{
            return false;
        }
    }
}

