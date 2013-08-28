package cz.schovjan.warehouseman;

import cz.schovjan.warehouseman.core.Framework;
import cz.schovjan.warehouseman.model.AbstractObject;
import cz.schovjan.warehouseman.model.Box;
import cz.schovjan.warehouseman.model.John;
import cz.schovjan.warehouseman.model.Machine;
import cz.schovjan.warehouseman.util.Constant;
import cz.schovjan.warehouseman.util.GraphicObjectUtil;
import cz.schovjan.warehouseman.util.Util;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author schovjan
 */
public class Game {

    private static Logger log = LogManager.getLogger();
    private static final int LEVEL_1 = 0;
    private static final int LEVEL_2 = 1;
    private static final int LEVEL_3 = 2;
    private int level;
    private John john;
    private Machine machine;
    private Machine machine2;
    public Set<Box> boxes;
    private BufferedImage backgroundImg;
    private int score;

    public Game() {
        Framework.gameState = Framework.GameState.GAME_CONTENT_LOADING;

        Thread threadForInitGame = new Thread() {
            @Override
            public void run() {
                // Sets variables and objects for the game.
                initialize();
                // Load game files (images, sounds, ...)
                loadContent();

                Framework.gameState = Framework.GameState.PLAYING;
            }
        };
        threadForInitGame.start();
    }

    /**
     * inicializace modelu
     */
    private void initialize() {
        level = LEVEL_1;
        score = 0;
        boxes = new HashSet<>();
        john = new John(this);
        machine = new Machine(this);
        machine2 = null;
    }

    /**
     * nacteni obsahu - obrazku
     */
    private void loadContent() {
        backgroundImg = Util.loadImage("pozadi.jpg");

        john.loadContent();
    }

    /**
     * Vykresleni hry na obrazovku
     *
     * @param g2d Graphics2D
     */
    public void draw(Graphics2D g2d) {
        g2d.drawImage(backgroundImg, 0, 0, Framework.frameWidth, Framework.frameHeight, null);

        for (Box box : boxes) {
            box.draw(g2d);
        }

        if (machine.getBox() != null) {
            machine.getBox().draw(g2d);
        }

        if (machine2 != null && machine2.getBox() != null) {
            machine2.getBox().draw(g2d);
        }

        john.draw(g2d);

        machine.draw(g2d);
        if (machine2 != null) {
            machine2.draw(g2d);
        }

        printStatistics(g2d);
    }

    /**
     * Update game logic.
     *
     * @param gameTime gameTime of the game.
     * @param mousePosition current mouse position.
     */
    public void updateGame() {
        john.update();

        machine.update();
        if (machine2 != null) {
            machine2.update();
        }

        for (Box box : boxes) {
            box.update();
        }

        if (machine.getBox() != null) {
            updateMachineBox(machine, machine.getBox());
        }
        if (machine2 != null && machine2.getBox() != null) {
            updateMachineBox(machine2, machine2.getBox());
        }

        int sc = score;
        removeFullRows();

        checkJohnLive();

        if (score == LEVEL_2 && sc < LEVEL_2) {
            updateToLevel(LEVEL_2);
        }
        if (score == LEVEL_3 && sc < LEVEL_3) {
            updateToLevel(LEVEL_3);
        }
    }

    /**
     * Updatuje krabici na stroji
     *
     * @param m
     * @param b
     */
    private void updateMachineBox(Machine m, Box b) {
        b.update(m.x, m.y);
        if (b.x % 100 == 0 && b.x >= 0 && b.x <= Framework.frameWidth - Constant.IMG_WIDTH && Util.rand(10) > 8) {
            if (isAcrossOtherBox(b, b.x, b.y + Framework.speed)) {
                log.info("Nelze pustit krabici - konec hry");
//                Framework.gameState = Framework.GameState.GAMEOVER;
            } else {
                boxes.add(m.letFallBox());
            }
        }
    }

    private void updateToLevel(int newLevel) {
        level = newLevel;
        switch (level) {
            case LEVEL_2:
                machine2 = new Machine(this);
                break;
            case LEVEL_3:
                Framework.speed += 5;
                break;
        }
    }

    /**
     * Vraci krabici, ktera zasahuje do souradnic
     *
     * @param box
     * @return
     */
    public Box getBoxOnCoords(AbstractObject ao, int x, int y) {
        for (Box b : boxes) {

            if (b == ao) {
                continue;
            }

            if (b.isAcrossOtherBox(x, y)) {
                return b;
            }
        }
        return null;
    }

    /**
     * Vraci true, pokud na danych souradnicich se vyskytuje jiny objekt, nez
     * byl predan
     *
     * @param ao
     * @param x
     * @param y
     * @return
     */
    public boolean isAcrossOtherBox(AbstractObject ao, int x, int y) {
        return getBoxOnCoords(ao, x, y) != null;
    }

    /**
     * Odebrani plnych radku (vzdu bude max jeden)
     */
    private void removeFullRows() {
        for (int y = 0; y < Framework.frameHeight; y += Constant.IMG_WIDTH) {
            List<Box> bs = GraphicObjectUtil.getBoxesByY(boxes, y);
            if (bs.size() == Framework.frameWidth / Constant.IMG_WIDTH) {
                boxes.removeAll(bs);
                score++;
            }
        }
    }

    /**
     * zkontroluje zda je hrac ziv a pokud ne tak ukonci hru
     */
    private void checkJohnLive() {
        if (isAcrossOtherBox(john, john.x, john.y)) {
            Framework.gameState = Framework.GameState.GAMEOVER;
        }
    }

    /**
     * Vypise statisktiky hry (skore)
     *
     * @param g2d
     */
    private void printStatistics(Graphics2D g2d) {
        g2d.setFont(new Font("Serif", Font.BOLD, 14));
        g2d.setColor(Color.white);
        g2d.drawString("Skóre: " + score, 10, 20);
    }
}
