package main.classes;

import main.FilesName;

import java.util.Arrays;

public enum SpriteType {
    DEFAULT_ENEMY(FilesName.DEFAULT),
    DEFAULT(FilesName.DEFAULT),
    PLAYER(FilesName.PLAYER),
    ENEMY1(FilesName.ENEMY1),
    ENEMY2(FilesName.ENEMY2),
    ENEMY3(FilesName.ENEMY3),
    ENEMY4(FilesName.ENEMY4),
    PLAYER_BULLET1(FilesName.PLAYER_BULLET1),
    PLAYER_BULLET2(FilesName.PLAYER_BULLET2),
    PLAYER_BULLET3(FilesName.PLAYER_BULLET3),
    ENEMY1_BULLET(FilesName.ENEMY1_BULLET),
    ENEMY2_BULLET(FilesName.ENEMY2_BULLET),
    ENEMY3_BULLET(FilesName.ENEMY3_BULLET),
    ENEMY4_BULLET(FilesName.ENEMY4_BULLET),
    STAR(FilesName.STAR),
    EXPLOSION(FilesName.EXPLOSION_GIF),
    HEART(FilesName.HEART),
    ASTEROID(FilesName.ASTEROID);

    String model;

    public String getModel() {
        return model;
    }

    public CharacterType getCharacterType(){
        if(Arrays.asList(DEFAULT_ENEMY,  ENEMY1, ENEMY2, ENEMY3, ENEMY4, ASTEROID).contains(this)){
            return CharacterType.ENEMY;
        }
        else if(PLAYER.equals(this)){
            return CharacterType.PLAYER;
        }
        else{
            return CharacterType.MISC;
        }
    }

    SpriteType(String model) {
        this.model = model;
    }
}
