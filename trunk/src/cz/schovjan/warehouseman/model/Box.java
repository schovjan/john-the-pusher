package cz.schovjan.warehouseman.model;

import cz.schovjan.warehouseman.Game;
import cz.schovjan.warehouseman.core.Framework;
import cz.schovjan.warehouseman.util.Constant;
import cz.schovjan.warehouseman.util.Util;
import java.awt.Graphics2D;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author schovjan
 */
public class Box extends AbstractObject {

    private static Logger log = LogManager.getLogger();
    private int id;
    private Game game;
    public boolean onMachine;

    public Box(Game game) {
        this.game = game;
        id = game.boxes.size() + 1;
        onMachine = true;
        img = Util.loadImage("box" + (Util.rand(4) + 1) + ".png");
    }

    @Override
    public void draw(Graphics2D g2d) {
        super.draw(g2d);
    }

    /**
     * Update boxu ktery neni pripevnen na stroj
     */
    @Override
    public void update() {
        direction = Constant.DIRECTION_NOT_MOVE;

        if (game.isAcrossOtherBox(this, x, y + Framework.speed)) {
            return;
        }

        if (y + Framework.speed + Constant.IMG_WIDTH <= Framework.frameHeight) {
            direction |= Constant.DIRECTION_DOWN;
        }

        super.update();
    }

    /**
     * Update boxu na stroji
     *
     * @param machX
     * @param machY
     */
    public void update(int machX, int machY) {
        x = machX;
        y = machY;
    }

    /**
     * @param otherBox
     * @return true-pokud by po vykonani pohybu byla krabice v kolizi s jinou
     */
    public boolean isAcrossOtherBox(int otherX, int otherY) {
        if (otherX == x && otherY == y) {
            return true;
        }

        if (Math.abs(otherX - x) < img.getWidth()
                && Math.abs(otherY - y) < img.getHeight()) {
            return true;
        }
        return false;
    }

    /**
     * Vraci true, pokud lze krabici posunout do pozadovaneho smeru
     *
     * @param direction
     * @return
     */
    public boolean canMoveToSide(int direction) {

        if (game.isAcrossOtherBox(this, x, y - Framework.speed)) {
            return false;
        }

        if (direction == Constant.DIRECTION_LEFT) {
            if (x - Framework.speed < 0) {
                return false;
            }
            return !game.isAcrossOtherBox(this, x - Framework.speed, y);
        }

        if (direction == Constant.DIRECTION_RIGH) {
            if (x + img.getWidth() + Framework.speed > Framework.frameWidth) {
                return false;
            }
            return !game.isAcrossOtherBox(this, x + Framework.speed, y);
        }

        return true;
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }
}
