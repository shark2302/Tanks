package Game;

import Display.Display;
import Game.Level.Level;
import IO.Input;
import Utils.Time;
import Graphics.*;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;
import java.awt.*;
import java.util.List;

public class Game implements Runnable, MouseListener{

    public static final int WIDTH = 800;
    public static final int HEIGHT = 592;
    public static final String TITLE = "Tanks";
    public static final int CLEAR_COLOUR = 0xff000000;
    public static final int NUM_BUFFERS = 3;

    public static final float UPDATE_RATE = 60.0f;
    public static final float UPDATE_INTERVAL = Time.SECOND / UPDATE_RATE;
    public static final long IDLE_TIME = 1;

    public static final String ATLAS_FILE_NAME = "texture_atlas.png";

    private boolean running;
    private Thread gameThread;
    private Graphics2D graphics;
    private Input input;
    private TextureAtlas atlas;
    private Level lvl;
    private List<Bullet> bullets;
    private List<Player> players;
    private List<Flag> flags;
    private List<Explosion> explosions;
    private Integer clickX;
    private Integer clickY;
    private MouseListener mouseListener;

    public Game(){
        clickX = null;
        clickY = null;
        running = false;
        Display.create(WIDTH, HEIGHT, TITLE, CLEAR_COLOUR, NUM_BUFFERS, this);
        graphics = Display.getGraphics();
        players = new ArrayList<>();
        flags = new ArrayList<>();
        explosions = new ArrayList<>();
        input = new Input();
        Display.addInputListener(input);
        atlas = new TextureAtlas(ATLAS_FILE_NAME);
        players.add(new Player(4, 4, 2, 0.3f, atlas, this,
                new Integer[]{KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_SPACE}));
        players.add(new Player(44, 31, 2, 0.3f, atlas, this,
                new Integer[]{KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_ENTER}));
        lvl = new Level(atlas);
        bullets = new ArrayList<Bullet>();
        flags.add(new Flag(1, 1, 2, players.get(0).getPlayerId(), atlas));
        flags.add(new Flag(47, 34, 2, players.get(1).getPlayerId(), atlas));


    }

    public synchronized void start(){
        if(running)
            return;
        running = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    public synchronized void stop() {
        if(!running)
            return;
        running = false;
        try {
            gameThread.join();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        cleanUp();
    }

    private void update() {
        Set<Bullet> removing = new HashSet<>();
        for(Bullet bul : bullets) {
            bul.update(input, lvl);
            for(Flag flag : flags) {
                if (bul.collisionWithEntity(flag) && !bul.getId().equals(flag.getId())) {
                    flag.decrHealth(50);
                    removing.add(bul);
                }
            }
            for(Player player : players) {
                if(bul.collisionWithEntity(player) && !bul.getId().equals(player.getPlayerId())) {
                    explosions.add(new Explosion(bul.getX(), bul.getY(), 1, atlas));
                    player.decrHealth(50);
                    removing.add(bul);
                }
            }
            if(bul.collision(lvl)) {
                    lvl.getTileMap()[Math.round(bul.getY())][Math.round(bul.getX())].decrHealth(50);
                    removing.add(bul);
                explosions.add(new Explosion(bul.getX(), bul.getY(), 1, atlas));

                }
        }
        for(Bullet bullet : removing)
            bullets.remove(bullet);
        for(Player player : players) {
                player.update(input, lvl);
                if(player.getHealth() <= 0) {
                    explosions.add(new Explosion(player.getX(), player.getY(), 2, atlas));
                    players.set(players.indexOf(player), null);
                }

        }
        lvl.update();

       for(Flag flag : flags) {
           if(flag.getHealth() <= 0)
               System.exit(1);
       }
        players.remove(null);

    }

    private void render() {
        Display.clear();
        lvl.render(graphics);
        for(Player player : players) {
            player.render(graphics);
        }
        lvl.renderGrass(graphics);
        for(Flag flag : flags) {
            flag.render(graphics);
        }

        for(Bullet bul : bullets)
            bul.render(graphics);
        for(Explosion exp : explosions) {
            if(exp != null)
            exp.render(graphics);
        }
        for(Explosion exp : explosions) {
            if(exp != null && exp.isReadyToDelete())
                explosions.set(explosions.indexOf(exp), null);
        }
        explosions.remove(null);
        Display.swapBuffers();

    }

    public void run(){
        int fps = 0;
        int upd = 0;
        int updl = 0;
        long count = 0;

        float delta = 0;
        long lastTime = Time.get();
        while(running) {
            long now = Time.get();
            long elapsedTime = now - lastTime;
            lastTime = now;
            count += elapsedTime;
            boolean render = false;
            delta += (elapsedTime / UPDATE_INTERVAL);
            while (delta > 1) {
                update();
                upd++;
                delta--;
                if (render) {
                    updl++;
                } else {
                    render = true;
                }
            }
            if(render){
                render();
                fps++;
            }else {
                try {
                    Thread.sleep(IDLE_TIME);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
            if(count >= Time.SECOND){
                Display.setTitle(TITLE + " || FPS: " + fps +" | UPD: " + upd + " | UPDL : " + updl);
                upd = 0;
                fps = 0;
                updl = 0;
                count = 0;
            }
        }
    }

    private void cleanUp() {
        Display.destroy();
    }

    public List<Bullet> getBullets() {
        return bullets;
    }

    public List<Player> getPlayers() {
        return players;
    }



    public Integer getClickX() {
        return clickX;
    }

    public void setClickX(Integer clickX) {
        this.clickX = clickX;
    }

    public Integer getClickY() {
        return clickY;
    }

    public void setClickY(Integer clickY) {
        this.clickY = clickY;
    }

    public MouseListener getMouseListener() {
        return mouseListener;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        clickX = e.getX() / 16;
        clickY = e.getY() / 16;
        System.out.println(1);
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
