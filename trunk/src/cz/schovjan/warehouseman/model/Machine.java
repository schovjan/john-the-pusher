package cz.schovjan.warehouseman.model;

import cz.schovjan.warehouseman.Game;
import cz.schovjan.warehouseman.core.Framework;
import static cz.schovjan.warehouseman.model.AbstractObject.direction;
import cz.schovjan.warehouseman.util.Constant;
import cz.schovjan.warehouseman.util.Util;

/**
 *
 * @author schovjan
 */
public class Machine extends AbstractObject {

    private boolean leftToRight = true;
    private Game game;
    private Box box;

    public Machine(Game game) {
        super();
        this.game = game;
        y = 0;
        img = Util.loadImage("machine.png");
        createBox();
    }

    /**
     * Vytvoreni krabice na stroji
     */
    private void createBox() {
        box = new Box(this.game);
        box.x = x;
        box.y = y;
    }

    @Override
    public void update() {
        direction = Constant.DIRECTION_NOT_MOVE;

        if ((leftToRight && (x + Framework.speed >= Framework.frameWidth + Framework.speed))
                || (!leftToRight && (x - Framework.speed <= 0 - Constant.IMG_WIDTH - Framework.speed))) {

            // stroj odjel z platna
            leftToRight = Util.rand(2) == 0;
            if (leftToRight) {
                x = -Constant.IMG_WIDTH;
            } else {
                x = Framework.frameWidth + Constant.IMG_WIDTH;
            }
            createBox();
            return;
        }

        direction |= leftToRight ? Constant.DIRECTION_RIGH : Constant.DIRECTION_LEFT;
        super.update();
    }

    public Box letFallBox() {
        Box b = getBox();
        b.onMachine = false;
        setBox(null);
        return b;
    }

    public Box getBox() {
        return box;
    }

    public void setBox(Box box) {
        this.box = box;
    }
}
