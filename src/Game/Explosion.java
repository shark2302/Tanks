package Game;

import Game.Level.Level;
import IO.Input;
import Graphics.*;

import java.util.ArrayList;
import java.util.List;

import java.awt.*;

public class Explosion extends Entity {

    private final int SPRITE_SCALE = 16;


    private int scale;
    private List<Sprite> sprites;
    private Long timeOfCreating;
    private boolean readyToDelete;
    protected Explosion(float x, float y, int scale, TextureAtlas atlas) {
        super(EntityType.Explosion, x, y);
        this.scale = scale;
        timeOfCreating = System.currentTimeMillis();
        readyToDelete = false;
        sprites = new ArrayList<>();
        for (int i = 16; i < 19; i++) {
            SpriteSheet sheet = new SpriteSheet(atlas.cut(i * SPRITE_SCALE, 8 * SPRITE_SCALE, SPRITE_SCALE, SPRITE_SCALE), 1, SPRITE_SCALE);
            sprites.add(new Sprite(sheet, scale));
        }

    }

    @Override
    public void update(Input input, Level level) {

    }

    @Override
    public void render(Graphics2D g) {
        if(System.currentTimeMillis() - timeOfCreating < 5)
            sprites.get(0).render(g, x * SPRITE_SCALE, y * SPRITE_SCALE);
        else if(System.currentTimeMillis() - timeOfCreating < 50)
            sprites.get(1).render(g, x * SPRITE_SCALE, y * SPRITE_SCALE);
        else if(System.currentTimeMillis() - timeOfCreating < 100)
            sprites.get(2).render(g, x * SPRITE_SCALE, y * SPRITE_SCALE);
        else  readyToDelete = true;
    }

    @Override
    public float getWidth() {
        return 0;
    }

    @Override
    public float getHeight() {
        return 0;
    }

    public boolean isReadyToDelete() {
        return readyToDelete;
    }
}
