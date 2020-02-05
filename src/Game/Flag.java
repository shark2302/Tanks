package Game;

import Game.Level.Level;
import Graphics.TextureAtlas;
import IO.Input;
import Utils.Utils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.UUID;

public class Flag extends Entity {

    private final int TILE_SCALE = 16;
    private int health, scale;
    private UUID id;
    private BufferedImage image;


    public Flag(float x, float y, int scale, UUID id, TextureAtlas atlas) {
        super(EntityType.Flag, x, y);
        this.id = id;
        this.scale = scale;
        image = atlas.cut(19 * TILE_SCALE, 2 * TILE_SCALE, TILE_SCALE, TILE_SCALE );
        image = Utils.resize(image, image.getWidth() * scale, image.getHeight() * scale);
        health = 100;
    }

    @Override
    public void update(Input input, Level level) {

    }
    public int getHealth() {
        return health;
    }
    public void decrHealth(int damage) {
        this.health = getHealth() - damage;
    }

    public void render(Graphics2D g) {
        g.drawImage(image, (int)getX() * TILE_SCALE, (int)getY() * TILE_SCALE, null);
    }

    @Override
    public float getWidth() {
        return TILE_SCALE * scale/ 16;
    }

    @Override
    public float getHeight() {
        return TILE_SCALE * scale / 16;
    }

    public UUID getId() {
        return id;
    }
}
