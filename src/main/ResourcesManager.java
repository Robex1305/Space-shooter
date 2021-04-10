package main;

import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import main.classes.SpriteType;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ResourcesManager {

    private MediaPlayer mediaPlayer;
    private MediaPlayer backgroundPlayer;
    private Media media;
    private List<Media> playing;

    private static ResourcesManager instance;

    public static ResourcesManager getInstance(){
        if(instance == null){
            instance = new ResourcesManager();
        }

        return instance;
    }

    private ResourcesManager(){
        playing = new ArrayList<>();
    }

    public InputStream getFileStream(String fileName){
       return getClass().getClassLoader().getResourceAsStream(fileName);
    }

    public Image getAssociatedImage(SpriteType spriteType, double scale) {
        InputStream imageStream = ResourcesManager.getInstance().getFileStream(spriteType.getModel());
        InputStream imageStreamForAnalizys = ResourcesManager.getInstance().getFileStream(spriteType.getModel());

        Image image = null;
        try {
            BufferedImage bimg = ImageIO.read(imageStreamForAnalizys);
            double width = bimg.getWidth() * scale;
            double height = bimg.getHeight() * scale;

            image = new Image(imageStream, width, height, false, true);
            imageStreamForAnalizys.close();
            imageStream.close();
        } catch (IOException e) {
            throw new RuntimeException("Error occured while loading resource", e);
        }

        return image;
    }


    public void playSound(String fileName, double volumePercentage){
        try {
            media = new Media(getClass().getClassLoader().getResource(fileName).toURI().toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setVolume(volumePercentage/100);
        mediaPlayer.play();
    }

    public void reset(){
        mediaPlayer.stop();
        backgroundPlayer.stop();

        mediaPlayer.dispose();
        backgroundPlayer.dispose();
        instance = null;
    }

    public void startSoundtrack(double volumePercentage, boolean loopPart) {
        try {
            if (!loopPart) {

                media = new Media(getClass().getClassLoader().getResource(FilesName.SOUNDTRACK).toURI().toString());

            } else {
                media = new Media(getClass().getClassLoader().getResource(FilesName.SOUNDTRACK_LOOP).toURI().toString());
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        backgroundPlayer = new MediaPlayer(media);
        backgroundPlayer.setVolume(volumePercentage / 100);
        backgroundPlayer.play();
        backgroundPlayer.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                startSoundtrack(15, true);
            }
        });
    }

}

