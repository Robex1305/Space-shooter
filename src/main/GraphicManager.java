package main;

import javafx.animation.AnimationTimer;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import main.classes.Bullet;
import main.classes.Character;
import main.classes.Sprite;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class GraphicManager {
    private ResourcesManager resourcesManager;

    private Stage stage;
    private Pane pane;

    private AnimationTimer animationTimer;

    private List<Node> toAdd;
    private List<Node> toRemove;

    private List<Bullet> bullets;
    private List<Character> characters;

    private Text playerLife;
    private Text playerScore;

    private Character player;

    final public static double FRAME_TIME = 0.0167;
    final public static double SCREEN_WIDTH = 1280;
    final public static double SCREEN_HEIGHT = 720;

    public GraphicManager(Stage stage){
        stage.setWidth(1280);
        stage.setHeight(720);
        this.stage = stage;
        this.resourcesManager = ResourcesManager.getInstance();

        pane = new Pane();
        pane.setPrefSize(stage.getWidth(),stage.getHeight());

        Scene scene = new Scene(pane);
        this.stage.setTitle("Galaxy Fighter");
        this.stage.setScene(scene);
        this.stage.setResizable(false);
        this.stage.show();

        toAdd = new ArrayList<>();
        toRemove = new ArrayList<>();
        characters = new ArrayList<>();
        bullets = new ArrayList<>();

        ImageView background = getImage(FilesName.BACKGROUND, 0, 0, pane.getWidth(), pane.getHeight());
        add(background);
        background.toBack();

        scene.setCursor(Cursor.NONE);

        this.animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
            }
        };

        playerLife = new Text();
        playerLife.setX(SCREEN_WIDTH/2);
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

    public void setPlayer(Character player) {
        this.player = player;
        add(player);
    }

    public void updatePlayerScore(Integer score){
        this.playerScore.setText("Score: " + score);
    }

    public void updatePlayerLife(){
        if(player.getLife() > 0) {
            StringBuilder lifeBar = new StringBuilder();
            for (int i = 0; i < player.getLife(); i++) {
                lifeBar.append("♥");
            }
            playerLife.setText(lifeBar.toString());
        }
        else{
            playerLife.setText("GAME OVER");
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
    }

    public void update(){
        pane.getChildren().forEach(node -> {
            if(node instanceof Sprite){
                Sprite sprite = (Sprite) node;

                if(sprite instanceof Character){
                    Character character = (Character) sprite;
                    character.getSpritesToAdd().forEach(this::add);
                }

                if(sprite.isToDelete()){
                    remove(sprite);
                    remove(sprite.getSkin());
                }
            }
        });

        toRemove.forEach(n -> {
            if(n instanceof Sprite){
                ((Sprite) n).stopTimer();
            }
            else if(n instanceof Character){
                characters.remove(n);
            }
            else if(n instanceof Bullet){
                bullets.remove(n);
            }
            pane.getChildren().remove(n);
        });
        for (Node node : toAdd) {
            if(!pane.getChildren().contains(node)){
                pane.getChildren().add(node);
            }
        }

        toRemove.clear();
        toAdd.clear();
    }

    public ImageView getImage(String name, double x, double y, double width, double height){
        InputStream imageInputStream = resourcesManager.getFileStream(name);
        Image image = new Image(imageInputStream, width, height, false, true);
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        imageView.setFitWidth(imageView.getImage().getWidth());
        imageView.setFitHeight(imageView.getImage().getHeight());
        return imageView;
    }
}
