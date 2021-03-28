package main;

import com.sun.xml.internal.ws.api.message.ExceptionHasMessage;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import jdk.internal.util.xml.impl.Input;
import main.classes.Type;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;

public class ResourcesManager {

    private MediaPlayer mediaPlayer;
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

    public Image getAssociatedImage(Type type, double scale) {
        InputStream imageStream = null;
        InputStream imageStreamForAnalizys = null;
        switch (type) {
            case PLAYER:
                imageStream = ResourcesManager.getInstance().getFileStream(FilesName.PLAYER);
                imageStreamForAnalizys = ResourcesManager.getInstance().getFileStream(FilesName.PLAYER);
                break;
            case PLAYER_BULLET:
                imageStream = ResourcesManager.getInstance().getFileStream(FilesName.PLAYER_BULLET);
                imageStreamForAnalizys = ResourcesManager.getInstance().getFileStream(FilesName.PLAYER_BULLET);
                break;
            case ENEMY1_BULLET:
                imageStream = ResourcesManager.getInstance().getFileStream(FilesName.ENEMY1_BULLET);
                imageStreamForAnalizys = ResourcesManager.getInstance().getFileStream(FilesName.ENEMY1_BULLET);
                break;
            case ENEMY2_BULLET:
                imageStream = ResourcesManager.getInstance().getFileStream(FilesName.ENEMY2_BULLET);
                imageStreamForAnalizys = ResourcesManager.getInstance().getFileStream(FilesName.ENEMY2_BULLET);
                break;
            case ENEMY3_BULLET:
                imageStream = ResourcesManager.getInstance().getFileStream(FilesName.ENEMY3_BULLET);
                imageStreamForAnalizys = ResourcesManager.getInstance().getFileStream(FilesName.ENEMY3_BULLET);
                break;
            case ENEMY4_BULLET:
                imageStream = ResourcesManager.getInstance().getFileStream(FilesName.ENEMY4_BULLET);
                imageStreamForAnalizys = ResourcesManager.getInstance().getFileStream(FilesName.ENEMY4_BULLET);
                break;
            case STAR:
                imageStream = ResourcesManager.getInstance().getFileStream(FilesName.STAR);
                imageStreamForAnalizys = ResourcesManager.getInstance().getFileStream(FilesName.STAR);
                break;
            case ENEMY1:
                imageStream = ResourcesManager.getInstance().getFileStream(FilesName.ENEMY1);
                imageStreamForAnalizys = ResourcesManager.getInstance().getFileStream(FilesName.ENEMY1);
                break;
            case ENEMY2:
                imageStream = ResourcesManager.getInstance().getFileStream(FilesName.ENEMY2);
                imageStreamForAnalizys = ResourcesManager.getInstance().getFileStream(FilesName.ENEMY2);
                break;
            case ENEMY3:
                imageStream = ResourcesManager.getInstance().getFileStream(FilesName.ENEMY3);
                imageStreamForAnalizys = ResourcesManager.getInstance().getFileStream(FilesName.ENEMY3);
                break;
            case ENEMY4:
                imageStream = ResourcesManager.getInstance().getFileStream(FilesName.ENEMY4);
                imageStreamForAnalizys = ResourcesManager.getInstance().getFileStream(FilesName.ENEMY4);
                break;
        }

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

    public void playSound(String fileName){
        media = new Media(getFile(FilesName.SHOOT).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setVolume(0.02);
        mediaPlayer.play();
        mediaPlayer.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
            }
        });
    }

}

