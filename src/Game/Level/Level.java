package Game.Level;

import Game.Game;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import Graphics.*;
import Utils.Utils;
import jdk.swing.interop.SwingInterOpUtils;


public class Level {



    public static final int TILE_SCALE = 8;
    public static final int TILE_IN_GAME_SCALE = 2;
    public static final int SCALED_TILE_SIZE = TILE_SCALE * TILE_IN_GAME_SCALE;
    public static final int TILES_IN_WIDTH = Game.WIDTH / SCALED_TILE_SIZE;
    public static final int TILES_IN_HEIGHT = Game.HEIGHT / SCALED_TILE_SIZE;




    private Integer[][] tileArray;
    private Map<TileType, Tile> tiles;
    private List<Point> grassCords;
    private GameTile[][] tileMap;
    public Level(TextureAtlas atlas) {
        System.out.println(TILES_IN_WIDTH);
        System.out.println(TILES_IN_HEIGHT);
        tileArray = new Integer[TILES_IN_WIDTH][TILES_IN_HEIGHT];
        tiles = new HashMap<>();
        tileMap = new GameTile[TILES_IN_HEIGHT][TILES_IN_WIDTH];
        tiles.put(TileType.BRICK, new Tile(atlas.cut(32 * TILE_SCALE, 0 * TILE_SCALE, TILE_SCALE, TILE_SCALE),
                TILE_IN_GAME_SCALE, TileType.BRICK));
        tiles.put(TileType.METAL, new Tile(atlas.cut(32 * TILE_SCALE, 2 * TILE_SCALE, TILE_SCALE, TILE_SCALE),
                TILE_IN_GAME_SCALE, TileType.METAL));
        tiles.put(TileType.WATER, new Tile(atlas.cut(32 * TILE_SCALE, 4 * TILE_SCALE, TILE_SCALE, TILE_SCALE),
                TILE_IN_GAME_SCALE, TileType.WATER));
        tiles.put(TileType.GRASS, new Tile(atlas.cut(34 * TILE_SCALE, 4 * TILE_SCALE, TILE_SCALE, TILE_SCALE),
                TILE_IN_GAME_SCALE, TileType.GRASS));
        tiles.put(TileType.ICE, new Tile(atlas.cut(36 * TILE_SCALE, 4 * TILE_SCALE, TILE_SCALE, TILE_SCALE),
                TILE_IN_GAME_SCALE, TileType.ICE));
        tiles.put(TileType.EMPTY, new Tile(atlas.cut(36 * TILE_SCALE, 6 * TILE_SCALE, TILE_SCALE, TILE_SCALE),
                TILE_IN_GAME_SCALE, TileType.EMPTY));
        tiles.put(TileType.BORDER, new Tile(atlas.cut(32 * TILE_SCALE, 0 * TILE_SCALE, TILE_SCALE, TILE_SCALE),
                TILE_IN_GAME_SCALE, TileType.BORDER));

        tileArray = Utils.levelParser("res/level.lvl");
        grassCords = new ArrayList<>();
        for (int i = 0; i < tileArray.length; i++) {
            for (int j = 0; j < tileArray[i].length; j++) {
                Tile tile = tiles.get(TileType.fromNumeric(tileArray[i][j]));
                tileMap[i][j] = new GameTile(tile);
                if(tile.getTileType() == TileType.GRASS)
                    grassCords.add(new Point(j * SCALED_TILE_SIZE, i * SCALED_TILE_SIZE));

            }
        }
    }

    public void update() {

    }

    public void render(Graphics2D g) {
        for (int i = 0; i < tileMap.length; i++) {
            for (int j = 0; j < tileMap[i].length; j++) {
                if(tileMap[i][j].getHealth() <= 0) {
                    tileMap[i][j] = new GameTile(tiles.get(TileType.fromNumeric(0)));
                }
                GameTile tile = tileMap[i][j];

                if(tile.getTileType() != TileType.GRASS)
                    tile.getTile().render(g, j * SCALED_TILE_SIZE, i * SCALED_TILE_SIZE);
            }
        }
    }

    public void renderGrass(Graphics2D g) {
        for(Point p : grassCords) {
            tiles.get(TileType.GRASS).render(g, p.x, p.y);
        }
    }

    public Integer[][] getTileArray() {
        return tileArray;
    }

    public GameTile[][] getTileMap() {
        return tileMap;
    }
}
