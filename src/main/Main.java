package main;

import javafx.application.Application;
import javafx.stage.Stage;


public class Main extends Application {

    private GameManager gameManager;

    @Override
    public void start(Stage primaryStage) throws Exception{
        gameManager = new GameManager(primaryStage);
        gameManager.start();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
