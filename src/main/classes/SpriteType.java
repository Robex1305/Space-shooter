package main.classes;

import main.FilesName;

public enum SpriteType {
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
    EXPLOSION(FilesName.EXPLOSION_GIF);

    String model;

    public String getModel() {
        return model;
    }

    SpriteType(String model) {
        this.model = model;
    }
}
