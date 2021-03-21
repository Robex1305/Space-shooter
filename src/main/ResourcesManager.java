package main;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

public class ResourcesManager {

    private static ResourcesManager instance;

    public static ResourcesManager getInstance(){
        if(instance == null){
            instance = new ResourcesManager();
        }

        return instance;
    }

    private ResourcesManager(){}

    public InputStream getFile(String fileName){
        return this.getClass().getResourceAsStream(fileName);
    }
}

