package main;

import javafx.animation.AnimationTimer;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import main.classes.*;
import main.classes.Character;

import java.awt.*;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class GraphicManager {
    private ResourcesManager resourcesManager;

    private Stage stage;
    private Pane pane;
    private double time;
    private AnimationTimer animationTimer;

    private List<Node> toAdd;
    private List<Node> toRemove;

    private List<Bullet> bullets;
    private List<Character> characters;
    private List<Health> healths;

    private Text playerLife;
    private Text playerScore;
    private ImageView background1;
    private ImageView background2;

    private Character player;

    final public static double FRAME_TIME = 0.0167;

    public GraphicManager(Stage stage){
        toAdd = Collections.synchronizedList(new ArrayList<>());
        toRemove = Collections.synchronizedList(new ArrayList<>());
        characters = Collections.synchronizedList(new ArrayList<>());
        bullets = Collections.synchronizedList(new ArrayList<>());
        healths = Collections.synchronizedList(new ArrayList<>());

        this.stage = stage;
        this.stage.setWidth(1280);
        this.stage.setHeight(720);

        this.resourcesManager = ResourcesManager.getInstance();

        pane = new Pane();
        pane.setPrefSize(stage.getWidth(),stage.getHeight());
        Scene scene = new Scene(pane);
        this.stage.setTitle("Space Shooter - v1.0");
        this.stage.setScene(scene);
        this.stage.setResizable(false);
        if(!this.stage.isShowing()) {
            this.stage.show();
        }

        background1 = getImage(FilesName.BACKGROUND, 0, 0, pane.getWidth(), pane.getHeight());
        add(background1);
        background1.toBack();

        background2 = getImage(FilesName.BACKGROUND, pane.getWidth(), 0, pane.getWidth(), pane.getHeight());
        add(background2);
        background2.toBack();

        scene.setCursor(Cursor.NONE);

        this.animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
            }
        };

        playerLife = new Text();
        playerLife.setX(getScreenWidth() /2);
        playerLife.setY(30);
        playerLife.setFill(Color.RED);

        playerScore = new Text();
        playerScore.setX(20);
        playerScore.setY(30);
        playerScore.setFill(Color.WHITE);

        this.playerScore.setFont(Font.loadFont(ResourcesManager.getInstance().getFileStream(FilesName.FONT), 20));
        this.playerLife.setFont(Font.loadFont(ResourcesManager.getInstance().getFileStream(FilesName.FONT), 20));
        add(playerScore);
        add(playerLife);
    }

    public void setTime(double time) {
        this.time = time;
    }

    public void setPlayer(Character player) {
        this.player = player;
        add(player);
    }

    public double getScreenWidth() {
        return getStage().getWidth();
    }

    public double getScreenHeight() {
        return getStage().getHeight();
    }

    public void updatePlayerScore(Integer score){
        this.playerScore.setText("SCORE: " + score);
    }

    public void updatePlayerLife(){
        if (player != null) {
            if (player.getLife() > 0) {
                StringBuilder lifeBar = new StringBuilder();
                for (int i = 0; i < player.getLife(); i++) {
                    lifeBar.append("♥");
                }
                playerLife.setText(lifeBar.toString());
            } else {
                playerLife.setText("GAME OVER");
            }
        }
    }

    public Pane getPane(){
        return this.pane;
    }

    public Stage getStage(){
        return this.stage;
    }

    public void start(){
        this.animationTimer.start();
    }

    public void stop(){
        this.animationTimer.stop();
    }

    public void add(Node node){
        if(!toAdd.contains(node)){
            if(node instanceof Sprite){
                Sprite sprite = (Sprite) node;
                toAdd.add(sprite);
                if(sprite.getSkin() != null) {
                    toAdd.add(sprite.getSkin());
                }
            }
            else {
                toAdd.add(node);
            }
        }

        if(node instanceof Character){
            Character c = (Character) node;
            if(!characters.contains(c)){
                characters.add(c);
            }
        }

        if(node instanceof Bullet){
            Bullet b = (Bullet) node;
            if(!bullets.contains(b)){
                bullets.add(b);
            }
        }

        if(node instanceof Health){
            Health h = (Health) node;
            if(!healths.contains(h)){
                healths.add(h);
            }
        }
    }

    public List<Bullet> getBullets() {
        return bullets;
    }

    public List<Character> getCharacters() {
        return characters;
    }

    public void remove(Node node){
        if(!toRemove.contains(node)){
            toRemove.add(node);
        }
        if(node instanceof Character){
            Character c = (Character) node;
            if(characters.contains(c)){
                characters.remove(c);
            }
        }

        if(node instanceof Bullet){
            Bullet b = (Bullet) node;
            if(bullets.contains(b)){
                bullets.remove(b);
            }
        }

        if(node instanceof Health){
            Health h = (Health) node;
            if(healths.contains(h)){
                healths.remove(h);
            }
        }
    }

    public List<Health> getHealths() {
        return healths;
    }

    //Unused
    public Point getRandomSpawnpoint() {
        Point point = new Point();
        double x = getScreenWidth();
        double y = Math.random() * getScreenHeight() * 0.8;
        point.setLocation(x, y);
        return point;
    }

    public void update(){
        //Make the background scrolls
        //It has 2 backgrounds. If one reaches the left edge, it is moved to the right, while the second keeps scrolling
        //to the left, until it reaches the edge, and so on.
        System.out.println(time);
        background1.setX(background1.getX() - (1 + GameManager.speedMultiplier));
        background2.setX(background2.getX() - (1 + GameManager.speedMultiplier));

        if(background1.getX() <= (-pane.getWidth())){
            background1.setX(pane.getWidth() - (1 + GameManager.speedMultiplier));
        }
        if(background2.getX() <= (-pane.getWidth())){
            background2.setX(pane.getWidth() - (1 + GameManager.speedMultiplier));
        }

        pane.getChildren().forEach(node -> {
            if(node instanceof Sprite){
                Sprite sprite = (Sprite) node;

                if(sprite instanceof Character){
                    Character character = (Character) sprite;
                    //Adds to the screen all sprites crated by the character. Calling "getSpritesToAdd" return the list but wipes it right after.
                    character.getSpritesToAdd().forEach(this::add);
                }

                if(sprite.isToDelete()){
                    remove(sprite);
                    remove(sprite.getSkin());
                }
            }
        });

        toRemove.iterator().forEachRemaining(n -> {
            if(n instanceof Sprite){
                ((Sprite) n).stopTimer();
            }
            if(n instanceof Character){
                Character c = (Character) n;
                characters.remove(c);
                if(c.getLife() <= 0) {
                    explodeAt(c);
                }
            }
            if(n instanceof Enemy){
                Enemy e = (Enemy) n;
                if(e.getX() < 0){
                    e.setToDelete(true);
                }
            }
            if(n instanceof Bullet){
                Bullet b = (Bullet) n;
                if (b.getX() > getScreenWidth() || b.getX() < 0) {
                    b.setToDelete(true);
                }
                bullets.remove(n);
            }
            pane.getChildren().remove(n);

        });
        toAdd.iterator().forEachRemaining(node -> {
            if(!pane.getChildren().contains(node)){
                pane.getChildren().add(node);
            }
        });

        toRemove.clear();
        toAdd.clear();
    }

    public void explodeAt(Sprite sprite){
        TemporarySprite explosion = new TemporarySprite(sprite.getPosition(), 1, 0, SpriteType.EXPLOSION, 1);
        //Using twice the width on purpose to make it square, dependless of the sprite's shape
        explosion.getSkin().setFitWidth(sprite.getWidth());
        explosion.getSkin().setFitHeight(sprite.getWidth());
        ResourcesManager.getInstance().playSound(FilesName.EXPLOSION_MP3,8);
        add(explosion);
    }

    public ImageView getImage(String name, double x, double y, double width, double height){
        InputStream imageInputStream = resourcesManager.getFileStream(name);
        Image image = new Image(imageInputStream, width, height, false, true);
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        imageView.setFitWidth(imageView.getImage().getWidth());
        imageView.setFitHeight(imageView.getImage().getHeight());
        imageView.setX(x);
        imageView.setY(y);
        return imageView;
    }

    public Label showEndOfGameScreen() {
        Label message = new Label("REPLAY");
        message.setTextFill(Color.WHITE);
        message.setFont(Font.loadFont(ResourcesManager.getInstance().getFileStream(FilesName.FONT), 50));
        message.setMinWidth(getScreenWidth());
        message.setMaxWidth(getScreenWidth());
        message.setAlignment(Pos.CENTER);
        message.setTranslateY(getScreenHeight()/2);

        message.setOnMouseEntered(event -> {
            message.setTextFill(Color.GOLD);
        });

        message.setOnMouseExited(event -> {
            message.setTextFill(Color.WHITE);
        });

        getStage().getScene().setCursor(Cursor.DEFAULT);

        add(message);
        return message;
    }
}
