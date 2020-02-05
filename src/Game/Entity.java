package Game;

import Game.Level.Level;
import IO.Input;

import java.awt.*;

public abstract class Entity {

    public final EntityType type;

    protected float x;
    protected float y;

    protected Entity(EntityType type, float x, float y) {
        this.type = type;
        this.x = x;
        this.y = y;
    }

    public abstract void update(Input input, Level level);
    public abstract void render(Graphics2D g);
    public abstract float getWidth();
    public abstract float getHeight();

    public boolean collisionWithEntity(Entity entity) {
        return collisionWithEntity(x, y, entity);
   }

   public boolean collisionWithEntity(float newX, float newY, Entity entity) {
       if(entity.pointInEntityArea(newX, newY, entity) || entity.pointInEntityArea(newX + getWidth(), newY, entity) ||
               entity.pointInEntityArea(newX + getWidth(), newY + getHeight(), entity) || entity.pointInEntityArea(newX, newY + getHeight(), entity))
           return true;
       return false;
   }

   public boolean pointInEntityArea(float x, float y, Entity entity) {
        if(x >= entity.x && x < entity.x + getWidth() && y >= entity.y && y < entity.y + getHeight())
            return true;
        return false;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
