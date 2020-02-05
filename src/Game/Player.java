package Game;

import Game.Level.Level;
import Game.Level.TileType;
import IO.Input;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import Graphics.*;

public class Player extends Entity {

    public static final int SPRITE_SCALE = 16;
    public static final int SPRITES_PER_HEADING = 1;



    private Heading heading;
    private Map<Heading, Sprite> spriteMap;
    private Map<Control, Integer> control;
    private float scale;
    private float speed;
    private UUID playerId;
    private Game game;
    private long lastTimeShot;
    private int health;



    public Player(float x, float y, float scale, float speed, TextureAtlas atlas, Game game, Integer[] controls) {
        super(EntityType.Player, x, y);

        heading = Heading.NORTH;
        spriteMap = new HashMap<>();
        control = new HashMap<>();
        control.put(Control.MOVE_UP, controls[0]);
        control.put(Control.MOVE_DOWN, controls[1]);
        control.put(Control.MOVE_LEFT, controls[2]);
        control.put(Control.MOVE_RIGHT, controls[3]);
        control.put(Control.SHOT, controls[4]);
        this.scale = scale;
        this.speed = speed;
        this.game = game;
        health = 250;
        playerId = UUID.randomUUID();
        for(Heading h : Heading.values()) {
            SpriteSheet sheet = new SpriteSheet(h.texture(atlas), SPRITES_PER_HEADING, SPRITE_SCALE );
            Sprite sprite = new Sprite(sheet, scale);
            spriteMap.put(h, sprite);
        }
        lastTimeShot = 0;

    }

    @Override
    public void update(Input input, Level level) {
        float newX = x;
        float newY = y;
        if(input.getKey(control.get(Control.MOVE_UP))) {

          if(!collisionWithTileUp(level))
            newY -= speed;
            heading = Heading.NORTH;
        }else if(input.getKey(control.get(Control.MOVE_RIGHT)) ){

           if(!collisionWithTileRight(level))
            newX += speed;
            heading = Heading.EAST;
        }else if(input.getKey(control.get(Control.MOVE_DOWN)) ){
            if(!collisionWithTileDown(level))
            newY += speed;
            heading = Heading.SOUTH;
        }else if(input.getKey(control.get(Control.MOVE_LEFT)) ) {
            if(!collisionWithTileLeft(level))
            newX -= speed;
            heading = Heading.WEST;
        }
        for(Player player : game.getPlayers()) {
            if(player != null && !getPlayerId().equals(player.getPlayerId()) && this.collisionWithEntity(newX, newY, player)) {
                newX = x;
                newY = y;
            }
        }
        x = newX;
        y = newY;

        if(canShot() && game.getClickY() != null && game.getClickX() != null) {
            System.out.println(1);
            lastTimeShot = System.currentTimeMillis();
            game.getBullets().add(shot());
            game.setClickX(null);
            game.setClickY(null);
        }
    }

    @Override
    public void render(Graphics2D g) {
        spriteMap.get(heading).render(g, x * SPRITE_SCALE, y * SPRITE_SCALE);
    }

    @Override
    public float getWidth() {
        return SPRITE_SCALE * scale / SPRITE_SCALE;
    }

    @Override
    public float getHeight() {
        return SPRITE_SCALE * scale / SPRITE_SCALE;
    }

    private Bullet shot(){
      //  if(heading == Heading.WEST)
            return new Bullet((int)x + 1, (int)y + 1, heading, playerId,  game.getClickX() - (x + 1), game.getClickY() - (y + 1));
       /* else if(heading == Heading.EAST)
            return new Bullet((int)x + 2, (int)y + 1, heading, playerId );
        else if(heading == Heading.SOUTH)
            return new Bullet((int)x + 1, (int)y + 2, heading, playerId);
        else
            return new Bullet(x + 1, y, heading, playerId);

        */
    }

    private boolean canShot() {
        if(lastTimeShot == 0) return true;
        if(System.currentTimeMillis() - lastTimeShot > 500) return true;
        else return false;
    }

    public UUID getPlayerId() {
        return playerId;
    }
    public int getHealth() {
        return health;
    }
    public void decrHealth(int damage) {
        this.health = getHealth() - damage;
    }

    private boolean collisionWithTileUp(Level level) {
        if(level.getTileMap()[Math.round(y - 0.8f)][Math.round(x)].getTileType() == TileType.BRICK ||
                level.getTileMap()[Math.round(y - 0.8f)][Math.round(x)].getTileType() == TileType.BORDER ||
                level.getTileMap()[Math.round(y - 0.8f)][Math.round(x + 1.3f)].getTileType() == TileType.BRICK ||
                        level.getTileMap()[Math.round(y - 0.8f)][Math.round(x + 1.3f)].getTileType() == TileType.BORDER )
            return true;
       return false;
    }

    private boolean collisionWithTileDown(Level level) {
        if(level.getTileMap()[Math.round(y  + 1.5f)][Math.round(x)].getTileType() == TileType.BRICK ||
                level.getTileMap()[Math.round(y  + 1.55f)][Math.round(x)].getTileType() == TileType.BORDER ||
                level.getTileMap()[Math.round(y + 1.55f)][Math.round(x + 1.3f)].getTileType() == TileType.BRICK ||
                        level.getTileMap()[Math.round(y  +1.55f)][Math.round(x + 1.3f)].getTileType() == TileType.BORDER
              )
            return true;
        return false;
    }
    private boolean collisionWithTileRight(Level level) {
        if(level.getTileMap()[Math.round(y)][Math.round(x  + 1.55f)].getTileType() == TileType.BRICK ||
                level.getTileMap()[Math.round(y)][Math.round(x  + 1.55f)].getTileType() == TileType.BORDER ||
                level.getTileMap()[Math.round(y + 1.3f)][Math.round(x  + 1.55f)].getTileType() == TileType.BRICK ||
                        level.getTileMap()[Math.round(y + 1.3f)][Math.round(x  + 1.55f)].getTileType() == TileType.BORDER
              )
            return true;
        return false;
    }
    private boolean collisionWithTileLeft(Level level) {
        if(level.getTileMap()[Math.round(y)][Math.round(x - 0.7f)].getTileType() == TileType.BRICK ||
                level.getTileMap()[Math.round(y)][Math.round(x- 0.7f)].getTileType() == TileType.BORDER ||
                level.getTileMap()[Math.round(y + 1.3f)][Math.round(x - 0.7f)].getTileType() == TileType.BRICK ||
                        level.getTileMap()[Math.round(y + 1.3f)][Math.round(x - 0.7f)].getTileType() == TileType.BORDER
               )
            return true;
        return false;
    }
}
