package Game;

import Graphics.TextureAtlas;

import java.awt.image.BufferedImage;

public enum Heading {
    NORTH(0 * 16, 3 * 16,  16,  16),
    EAST(6 * 16, 3 * 16,  16,  16),
    SOUTH(4 * 16, 3 * 16,  16,  16),
    WEST(2 * 16, 3 * 16,  16,  16);

    private int x, y, h, w;

    Heading(int x, int y, int h, int w) {
        this.x = x;
        this.y = y;
        this.h = h;
        this.w = w;
    }

    protected BufferedImage texture(TextureAtlas atlas) {

        return atlas.cut(x, y, w, h);
    }
}
