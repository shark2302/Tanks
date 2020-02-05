package Game;

import Game.Level.Level;
import Game.Level.TileType;
import IO.Input;

import java.awt.*;
import java.util.*;

import Graphics.*;

public class Bullet extends Entity {


    private float[] speed;
    private Heading heading;
    private UUID id;




    public Bullet(float x, float y, Heading heading, UUID id, float speedX, float speedY) {
        super(EntityType.Bullet, x, y);
        this.heading = heading;
        this.id = id;
        speed= new float[]{speedX, speedY};
        norm();
    }

    @Override
    public void update(Input input, Level level) {
        float newX = x;
        float newY = y;
       /* if(heading == Heading.NORTH)
            newY -= speed;
        if(heading == Heading.SOUTH)
            newY += speed;
        if(heading == Heading.EAST)
            newX += speed;
        if(heading == Heading.WEST)
            newX -= speed;

        */
       newX += speed[0];
       newY += speed[1];
        x = newX;
        y = newY;

    }

    @Override
    public void render(Graphics2D g) {
        g.fillRect(Math.round(x * 16 + 2), Math.round(y * 16 + 2), 4, 4);

    }

    @Override
    public float getWidth() {
        return 4/ 16;
    }

    @Override
    public float getHeight() {
        return 4/ 16;
    }

    public boolean collision(Level lvl) {
        return lvl.getTileMap()[Math.round(y)][Math.round(x)].getTileType() == TileType.BRICK ||
                lvl.getTileMap()[Math.round(y)][Math.round(x)].getTileType() == TileType.BORDER;

        }



    private void norm() {
        float max = Math.max(Math.abs(speed[0]), Math.abs(speed[1]));
        speed[0] = speed[0] / max;
        speed[1] = speed[1] / max;
    }

    public UUID getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bullet bullet = (Bullet) o;
        return Arrays.equals(speed, bullet.speed) &&
                heading == bullet.heading &&
                Objects.equals(id, bullet.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(speed, heading, id);
    }
}
