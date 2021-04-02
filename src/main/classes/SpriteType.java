package main.classes;

import main.FilesName;

public enum SpriteType {
    DEFAULT(FilesName.DEFAULT),
    PLAYER(FilesName.PLAYER),
    ENEMY1(FilesName.ENEMY1),
    ENEMY2(FilesName.ENEMY2),
    ENEMY3(FilesName.ENEMY3),
    ENEMY4(FilesName.ENEMY4),
    PLAYER_BULLET(FilesName.PLAYER_BULLET),
    ENEMY1_BULLET(FilesName.ENEMY1_BULLET),
    ENEMY2_BULLET(FilesName.ENEMY2_BULLET),
    ENEMY3_BULLET(FilesName.ENEMY3_BULLET),
    ENEMY4_BULLET(FilesName.ENEMY4_BULLET),
    STRONG_BULLET(FilesName.STRONG_BULLET),
    STAR(FilesName.STAR);

    String model;

    public String getModel() {
        return model;
    }

    SpriteType(String model) {
        this.model = model;
    }
}
