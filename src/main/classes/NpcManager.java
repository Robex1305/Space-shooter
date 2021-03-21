package main.classes;

import javafx.scene.Scene;
import javafx.stage.Stage;
import main.GraphicManager;

public class NpcManager {

    private GraphicManager graphicManager;

    public NpcManager(GraphicManager graphicManager) {
        this.graphicManager = graphicManager;
        Stage stage = graphicManager.getStage();
        Scene scene = stage.getScene();
    }
}
