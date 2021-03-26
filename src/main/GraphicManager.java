package main;

import javafx.animation.AnimationTimer;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
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

    final public static double FRAME_TIME = 0.0167;

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

        ImageView background = getImage(FilesName.BACKGROUND, 0, 0, pane.getWidth(), pane.getHeight());
        add(background);



        this.animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
            }
        };

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
    }



    public void remove(Node node){
        if(!toRemove.contains(node)){
            toRemove.add(node);
        }
    }

    public void update(){
        pane.getChildren().forEach(node -> {
            if(node instanceof Sprite){
                Sprite sprite = (Sprite) node;
                sprite.move();

                if(sprite instanceof Character){
                    Character character = (Character) sprite;
                    character.getSpritesToAdd().forEach(this::add);
                }

                if(sprite.isToDelete()){
                    toRemove.add(sprite);
                    toRemove.add(sprite.getSkin());
                }
            }
        });

        toRemove.forEach(n -> {
            if(n instanceof Sprite){
                ((Sprite) n).stopTimer();
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
        InputStream imageInputStream = resourcesManager.getFile(name);
        Image image = new Image(imageInputStream, width, height, false, true);
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        imageView.setFitWidth(imageView.getImage().getWidth());
        imageView.setFitHeight(imageView.getImage().getHeight());
        return imageView;
    }
}
