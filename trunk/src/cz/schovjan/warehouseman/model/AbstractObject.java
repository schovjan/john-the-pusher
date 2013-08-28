package cz.schovjan.warehouseman.model;

import cz.schovjan.warehouseman.core.Framework;
import cz.schovjan.warehouseman.util.Constant;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 *
 * @author schovjan
 */
public abstract class AbstractObject {

    public int x;
    public int y;
    public BufferedImage img;
    public static int direction;

    public AbstractObject() {
    }

    /**
     * Aktualizace souradnic objektu. Nutno volat po overidu.
     */
    public void update() {
        if (Constant.DIRECTION_DOWN == (direction & Constant.DIRECTION_DOWN)) {
            y += Framework.speed;
        }
        if (Constant.DIRECTION_UP == (direction & Constant.DIRECTION_UP)) {
            y -= Framework.speed;
        }
        if (Constant.DIRECTION_LEFT == (direction & Constant.DIRECTION_LEFT)) {
            x -= Framework.speed;
        }
        if (Constant.DIRECTION_RIGH == (direction & Constant.DIRECTION_RIGH)) {
            x += Framework.speed;
        }
    }

    /**
     * Vykresleni objektu na platno
     *
     * @param g2d
     */
    public void draw(Graphics2D g2d) {
        g2d.drawImage(img, x, y, null);
    }
}
