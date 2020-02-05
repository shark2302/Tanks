package Game.Level;

public class GameTile {


    private Tile tile;
    private int health;

    public GameTile(Tile tile) {
        this.tile = tile;
        health = generateHealth();
    }
    public int getHealth() {
        return health;
    }
    public void decrHealth(int damage) {
        this.health = getHealth() - damage;
    }
    public TileType getTileType() {
        return tile.getTileType();
    }

    public Tile getTile() {
        return tile;
    }
    private int generateHealth() {
        if(tile.getTileType() == TileType.BRICK)
            return 100;
        if(tile.getTileType() == TileType.BORDER)
            return 10000;
        if(tile.getTileType() == TileType.METAL)
            return 200;
        return 100;
    }
}
