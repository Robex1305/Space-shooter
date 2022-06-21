package main.classes;

import main.FilesName;

import java.util.Arrays;

public enum SpriteType {
    DEFAULT_ENEMY(FilesName.DEFAULT, GeneralType.ENEMY),
    DEFAULT(FilesName.DEFAULT, GeneralType.MISC),
    PLAYER(FilesName.PLAYER, GeneralType.PLAYER),
    ENEMY1(FilesName.ENEMY1, GeneralType.ENEMY),
    ENEMY2(FilesName.ENEMY2, GeneralType.ENEMY),
    ENEMY3(FilesName.ENEMY3, GeneralType.ENEMY),
    ENEMY4(FilesName.ENEMY4, GeneralType.ENEMY),
    PLAYER_BULLET1(FilesName.PLAYER_BULLET1, GeneralType.PLAYER),
    PLAYER_BULLET2(FilesName.PLAYER_BULLET2, GeneralType.PLAYER),
    PLAYER_BULLET3(FilesName.PLAYER_BULLET3, GeneralType.PLAYER),
    PLAYER_BULLET4(FilesName.ENEMY3_BULLET, GeneralType.PLAYER),
    ENEMY1_BULLET(FilesName.ENEMY1_BULLET, GeneralType.ENEMY),
    ENEMY2_BULLET(FilesName.ENEMY2_BULLET, GeneralType.ENEMY),
    ENEMY3_BULLET(FilesName.ENEMY3_BULLET, GeneralType.ENEMY),
    ENEMY4_BULLET(FilesName.ENEMY4_BULLET, GeneralType.ENEMY),
    ASTEROID(FilesName.ASTEROID, GeneralType.ENEMY),
    STAR(FilesName.STAR, GeneralType.MISC),
    EXPLOSION(FilesName.EXPLOSION_GIF, GeneralType.MISC),
    HEART(FilesName.HEART, GeneralType.MISC),
    PLAYER_ROCKET(FilesName.PLAYER_ROCKET, GeneralType.PLAYER ),
    PLAYER_ROCKET_AMMO(FilesName.PLAYER_ROCKET, GeneralType.MISC );

    private String model;
    private GeneralType generalType;
    public String getModel() {
        return model;
    }

    public GeneralType getGeneralType(){
        return generalType;
    }

    SpriteType(String model, GeneralType generalType) {
        this.model = model;
        this.generalType = generalType;
    }
}
