package cz.schovjan.warehouseman.model;

import cz.schovjan.warehouseman.Game;
import cz.schovjan.warehouseman.core.Canvas;
import cz.schovjan.warehouseman.core.Framework;
import static cz.schovjan.warehouseman.model.AbstractObject.direction;
import cz.schovjan.warehouseman.util.Constant;
import cz.schovjan.warehouseman.util.Util;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author schovjan
 */
public class John extends AbstractObject {

    private static Logger log = LogManager.getLogger();
    private BufferedImage images[];
    private Game game;
    private int jumpIndex = -1;
    private static int jumpsHeights[] = new int[]{-15, -20, -20, -20, -10, -10, -5, 0, 0};

    public John(Game game) {
        super();
        this.game = game;
        y = Framework.frameHeight - Constant.IMG_WIDTH;
    }

    @Override
    public void draw(Graphics2D g2d) {
        g2d.drawImage(img, x, y, null);
    }

    public void loadContent() {
        images = new BufferedImage[3];
        images[0] = Util.loadImage("john.png");
        images[1] = Util.loadImage("john_l.png");
        images[2] = Util.loadImage("john_r.png");

        img = images[0];
    }

    /**
     * Pohyb Johna
     *
     */
    @Override
    public void update() {
        direction = Constant.DIRECTION_NOT_MOVE;
        img = images[0];
        Box box = null;
        int nx = x;
        int ny = y;

        if (Canvas.keyboardKeyState(KeyEvent.VK_LEFT) && Canvas.keyboardKeyState(KeyEvent.VK_RIGHT)) {
            return;
        }

        // pohyb do strany vlevo
        if (Canvas.keyboardKeyState(KeyEvent.VK_LEFT) && nx - Framework.speed >= 0) {

            // zda nestoji pred krabici
            box = game.getBoxOnCoords(this, nx - Framework.speed, ny);
            if (box == null) {
                direction |= Constant.DIRECTION_LEFT;
                img = images[1];
            }
        }
        // pohyb doprava
        if (Canvas.keyboardKeyState(KeyEvent.VK_RIGHT) && nx + img.getWidth() + Framework.speed <= Framework.frameWidth) {
            // zda nestoji pred krabici
            box = game.getBoxOnCoords(this, nx + Framework.speed, ny);
            if (box == null) {
                direction |= Constant.DIRECTION_RIGH;
                img = images[2];
            }
        }

        // stoji pred krabici, zjisti se jestli lze krabici posunout
        if (box != null) {
            // kontrola jestli muze krabici posunout
            if (Canvas.keyboardKeyState(KeyEvent.VK_RIGHT) && box.canMoveToSide(Constant.DIRECTION_RIGH)) {
                box.update(box.x + (box.img.getWidth()), box.y);
                direction |= Constant.DIRECTION_RIGH;
                img = images[2];
            } else if (Canvas.keyboardKeyState(KeyEvent.VK_LEFT) && box.canMoveToSide(Constant.DIRECTION_LEFT)) {
                box.update(box.x - (box.img.getWidth()), box.y);
                direction |= Constant.DIRECTION_LEFT;
                img = images[1];
            }
        }

        if (Constant.DIRECTION_LEFT == (direction & Constant.DIRECTION_LEFT)) {
            nx -= Framework.speed;
        }
        if (Constant.DIRECTION_RIGH == (direction & Constant.DIRECTION_RIGH)) {
            nx += Framework.speed;
        }

        if (jumpIndex != -1) {
            if (ny + img.getHeight() + jumpsHeights[jumpIndex] <= Framework.frameHeight) {
                if (!game.isAcrossOtherBox(this, nx, ny + jumpsHeights[jumpIndex])) {
                    ny += jumpsHeights[jumpIndex];
                    jumpIndex++;
                } else {
                    jumpIndex = -1;
                }
            } else {
                jumpIndex = -1;
            }
        } else if (Canvas.keyboardKeyState(KeyEvent.VK_UP)
                && (game.isAcrossOtherBox(this, x, y + Framework.speed) || y + Framework.speed + img.getHeight() >= Framework.frameHeight)) {
            jumpIndex = 0;
            ny += jumpsHeights[jumpIndex];
        }

        if (jumpIndex != -1) {
            img = images[0];
            if (jumpIndex == jumpsHeights.length) {
                jumpIndex = -1;
            }
        } else if (!game.isAcrossOtherBox(this, nx, ny + Framework.speed) && ny + img.getHeight() + Framework.speed <= Framework.frameHeight) {
            ny += Framework.speed;
        }

        x = nx;
        y = ny;
    }
}
