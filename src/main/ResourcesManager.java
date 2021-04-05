package main;

import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import main.classes.SpriteType;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;

public class ResourcesManager {

    private MediaPlayer mediaPlayer;
    private MediaPlayer backgroundPlayer;
    private Media media;

    private static ResourcesManager instance;

    public static ResourcesManager getInstance(){
        if(instance == null){
            instance = new ResourcesManager();
        }

        return instance;
    }

    private ResourcesManager(){}

    public InputStream getFileStream(String fileName){
        InputStream is;

        try {
            is = new FileInputStream(getFile(fileName));
        } catch (FileNotFoundException e) {
            throw new RuntimeException("An error occured while retrieving the stream", e);
        }

        return is;
    }

    public File getFile(String fileName){
        try {
            URL url = this.getClass().getClassLoader().getResource(fileName);
            if (url == null) {
                throw new FileNotFoundException("Can't find: " + fileName);
            }
            return new File(url.toURI());
        }catch (Exception e) {
            throw new RuntimeException("An error occured while loading the resource", e);
        }
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
        media = new Media(getFile(fileName).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setVolume(volumePercentage/100);
        mediaPlayer.play();
    }

    public void startSoundtrack(double volumePercentage, boolean loopPart){
        if(!loopPart) {
            media = new Media(getFile(FilesName.SOUNDTRACK).toURI().toString());
        }else{
            media = new Media(getFile(FilesName.SOUNDTRACK_LOOP).toURI().toString());
        }
        backgroundPlayer = new MediaPlayer(media);
        backgroundPlayer.setVolume(volumePercentage/100);
        backgroundPlayer.play();
        backgroundPlayer.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                startSoundtrack(15, true);
            }
        });
    }

}

