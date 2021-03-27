package main;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

public class ResourcesManager {

    private MediaPlayer mediaPlayer;

    private static ResourcesManager instance;

    public static ResourcesManager getInstance(){
        if(instance == null){
            instance = new ResourcesManager();
        }

        return instance;
    }

    private ResourcesManager(){}

    public InputStream getFileStream(String fileName){
        return this.getClass().getResourceAsStream(fileName);
    }

    public File getFile(String fileName){
        return new File(this.getClass().getResource(fileName).getFile());
    }

    public void playSound(String fileName){
        Media media = new Media(getFile(FilesName.SHOOT).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setVolume(0.02);
        mediaPlayer.play();
    }

}

