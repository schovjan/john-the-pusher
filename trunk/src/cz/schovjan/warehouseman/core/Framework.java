package cz.schovjan.warehouseman.core;

import cz.schovjan.warehouseman.Game;
import cz.schovjan.warehouseman.util.Util;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author schovjan
 */
public class Framework extends Canvas {

    private Logger log = LogManager.getLogger();
    public static int frameWidth;
    public static int frameHeight;
    public static final long secInNanosec = 1000000000L;
    public static final long milisecInNanosec = 1000000L;
    public static int speed = 5;
    private final int GAME_FPS = 40;
    private final long GAME_UPDATE_PERIOD = secInNanosec / GAME_FPS;
    private long lastTime;

    public static enum GameState {

        STARTING, VISUALIZING, GAME_CONTENT_LOADING, MAIN_MENU, OPTIONS, PLAYING, GAMEOVER, DESTROYED
    }
    public static GameState gameState;
    private Game game;

    public Framework(int width, int height) {
        super(width, height);

        gameState = GameState.VISUALIZING;

        //hra zacina v novem vlakne
        Thread gameThread = new Thread() {
            @Override
            public void run() {
                gameLoop();
            }
        };
        gameThread.start();
    }

    /**
     * Starts new game.
     */
    private void newGame() {
        lastTime = System.nanoTime();

        game = new Game();
    }

    private void gameLoop() {
        // This two variables are used in VISUALIZING state of the game. We used them to wait some time so that we get correct frame/window resolution.
        long visualizingTime = 0, lastVisualizingTime = System.nanoTime();

        // This variables are used for calculating the time that defines for how long we should put threat to sleep to meet the GAME_FPS.
        long beginTime, timeTaken, timeLeft;

        while (true) {
            beginTime = System.nanoTime();

            switch (gameState) {
                case PLAYING:
                    game.updateGame();

                    lastTime = System.nanoTime();
                    break;
                case GAMEOVER:
                    //...
                    break;
                case MAIN_MENU:
                    //...
                    break;
                case OPTIONS:
                    //...
                    break;
                case GAME_CONTENT_LOADING:
                    //...
                    break;
                case STARTING:
//                    // When all things that are called above finished, we change game status to main menu.
                    gameState = GameState.MAIN_MENU;
                    break;
                case VISUALIZING:
                    //osetreni pro Ubuntu
                    if (this.getWidth() > 1 && visualizingTime > secInNanosec) {
                        frameWidth = this.getWidth();
                        frameHeight = this.getHeight();

                        // When we get size of frame we change status.
                        gameState = GameState.STARTING;
                    } else {
                        visualizingTime += System.nanoTime() - lastVisualizingTime;
                        lastVisualizingTime = System.nanoTime();
                    }
                    break;
            }

            repaint();

            // Here we calculate the time that defines for how long we should put threat to sleep to meet the GAME_FPS.
            timeTaken = System.nanoTime() - beginTime;
            timeLeft = (GAME_UPDATE_PERIOD - timeTaken) / milisecInNanosec; // In milliseconds
            // If the time is less than 10 milliseconds, then we will put thread to sleep for 10 millisecond so that some other thread can do some work.
            if (timeLeft < 10) {
                timeLeft = 10; //set a minimum
            }
            try {
                //Provides the necessary delay and also yields control so that other thread can do work.
                Thread.sleep(timeLeft);
            } catch (InterruptedException ex) {
            }
        }
    }

    @Override
    public void draw(Graphics2D g2d) {
        switch (gameState) {
            case PLAYING:
                game.draw(g2d);
                break;
            case GAMEOVER:
                game.draw(g2d);
                g2d.drawImage(Util.loadImage("game_over.png"), 0, 0, Framework.frameWidth, Framework.frameHeight, null);
                g2d.setColor(Color.white);
                g2d.drawString("Konec hry.", frameWidth / 2 - 100, frameHeight / 2);
                g2d.drawString("Pro spuštění nové hry stiskni enter nebo mezerník.", frameWidth / 2 - 100, frameHeight / 2 + 30);
                break;
            case MAIN_MENU:
                g2d.setColor(Color.white);
                g2d.drawString("Pro spuštění hry stiskni libovolnou klávesu.", frameWidth / 2 - 100, frameHeight / 2 + 30);
                break;
            case OPTIONS:
                //...
                break;
            case GAME_CONTENT_LOADING:
                g2d.setColor(Color.white);
                g2d.drawString("Načítám hru", frameWidth / 2 - 50, frameHeight / 2);
                break;
        }
    }

    @Override
    public void keyReleasedFramework(KeyEvent e) {
        switch (gameState) {
            case MAIN_MENU:
                newGame();
                break;
            case GAMEOVER:
                if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_ENTER) {
                    newGame();
                }
                break;
        }
    }
}
