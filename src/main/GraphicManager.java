package main;

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
import main.classes.Character;
import main.classes.Sprite;
import main.classes.SpriteType;
import main.classes.TemporarySprite;

import java.awt.*;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GraphicManager {
    private ResourcesManager resourcesManager;

    private Stage stage;
    private Pane pane;

    private List<Node> toAdd;
    private List<Node> toRemove;

    private List<Sprite> sprites;

    private Text playerLife;
    private Text playerScore;
    private ImageView background1;
    private ImageView background2;

    private Character player;

    final public static double FRAME_TIME = 0.0167;

    public GraphicManager(Stage stage) {
        toAdd = new ArrayList<>();
        toRemove = new ArrayList<>();
        sprites = new ArrayList<>();

        this.stage = stage;
        this.stage.setWidth(1280);
        this.stage.setHeight(720);

        this.resourcesManager = ResourcesManager.getInstance();

        pane = new Pane();
        pane.setPrefSize(stage.getWidth(), stage.getHeight());
        Scene scene = new Scene(pane);
        this.stage.setTitle("Space Shooter - v1.0");
        this.stage.setScene(scene);
        this.stage.setResizable(false);
        if (!this.stage.isShowing()) {
            this.stage.show();
        }

        background1 = getImage(FilesName.BACKGROUND, 0, 0, pane.getWidth(), pane.getHeight());
        add(background1);
        background1.toBack();

        background2 = getImage(FilesName.BACKGROUND, pane.getWidth(), 0, pane.getWidth(), pane.getHeight());
        add(background2);
        background2.toBack();

        scene.setCursor(Cursor.NONE);

        playerLife = new Text();
        playerLife.setX(getScreenWidth() / 2);
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

    public double getScreenWidth() {
        return getStage().getWidth();
    }

    public double getScreenHeight() {
        return getStage().getScene().getHeight();
    }

    public void updatePlayerScore(String score) {
        this.playerScore.setText("SCORE: " + score);
    }

    public void updatePlayerLife() {
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

    public Pane getPane() {
        return this.pane;
    }

    public Stage getStage() {
        return this.stage;
    }

    public void add(Node node) {
        if (!toAdd.contains(node)) {
            if (node instanceof Sprite) {
                Sprite sprite = (Sprite) node;
                toAdd.add(sprite);
                if (sprite.getSkin() != null) {
                    toAdd.add(sprite.getSkin());
                }
            } else {
                toAdd.add(node);
            }
        }
    }

    public List<Sprite> getSprites() {
        return sprites;
    }

    public void remove(Node node) {
        if (!toRemove.contains(node)) {
            toRemove.add(node);
        }
    }

    public boolean isCompletelyOffscreen(Sprite sprite) {
        return sprite.getX() < -sprite.getWidth() || sprite.getX() > getScreenWidth() + (2 * sprite.getWidth());
    }

    //Unused
    public Point getRandomSpawnpointOffscreenRight() {
        Point point = new Point();
        double x = getScreenWidth();
        double y = 50 + (Math.random() * (getScreenHeight() - 200));
        point.setLocation(x, y);
        return point;
    }

    public void update() {
        //Make the background scrolls
        //It has 2 backgrounds. If one reaches the left edge, it is moved to the right, while the second keeps scrolling
        //to the left, until it reaches the edge, and so on.
        background1.setX(background1.getX() - (2 * GameManager.globalMultiplier));
        background2.setX(background2.getX() - (2 * GameManager.globalMultiplier));

        if (background1.getX() <= (-pane.getWidth())) {
            background1.setX(background2.getX() + background2.getImage().getWidth());
        }
        if (background2.getX() <= (-pane.getWidth())) {
            background2.setX(background1.getX() + background1.getImage().getWidth());
        }

        pane.getChildren().forEach(node -> {
            if (node instanceof Sprite) {
                Sprite sprite = (Sprite) node;
                sprite.update();
                //Adds to the screen all sprites created by the character. Calling "getSpritesToAdd" return the list but wipes it right after.
                sprite.getSpritesToAdd().forEach(this::add);
                sprite.getSpritesToAdd().clear();
                if(isCompletelyOffscreen(sprite)){
                    System.out.println("sprite " + sprite.getSpriteType() + " is offscreen!");
                }
                if (sprite.isToDelete() || isCompletelyOffscreen(sprite)) {
                    remove(sprite);
                    remove(sprite.getSkin());;
                }

            }
        });

        for (Node n : toRemove) {
            pane.getChildren().remove(n);
            if (n instanceof Sprite) {
                sprites.remove(n);
            }
        }

        for (Node node : toAdd) {
            if (!pane.getChildren().contains(node)) {
                pane.getChildren().add(node);
            }
            if (node instanceof Sprite) {
                sprites.add((Sprite) node);
            }
        }

        toRemove.clear();
        toAdd.clear();
        sprites = pane.getChildren().stream().filter(n -> n instanceof Sprite).map(n -> (Sprite) n).collect(Collectors.toList());
    }

    public void explodeAt(Sprite sprite) {
        TemporarySprite explosion = new TemporarySprite(sprite.getPosition(), 0, 0, SpriteType.EXPLOSION, 1);
        //Using twice the width on purpose to make it square, dependless of the sprite's shape
        explosion.getSkin().setFitWidth(sprite.getWidth());
        explosion.getSkin().setFitHeight(sprite.getWidth());
        explosion.getSkin().setScaleX(sprite.getSkin().getScaleX());
        explosion.getSkin().setScaleY(sprite.getSkin().getScaleY());
        ResourcesManager.getInstance().playSound(FilesName.EXPLOSION_MP3, 8);
        add(explosion);
    }

    public ImageView getImage(String name, double x, double y, double width, double height) {
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
        message.setTranslateY(getScreenHeight() / 2);

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
