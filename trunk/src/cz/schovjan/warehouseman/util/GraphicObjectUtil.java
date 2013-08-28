package cz.schovjan.warehouseman.util;

import cz.schovjan.warehouseman.model.Box;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 *
 * @author schovjan
 */
public class GraphicObjectUtil {

    public static List<Box> getBoxesByY(Set<Box> boxes, int boxY) {
        List<Box> result = new ArrayList<>();
        for (Box b : boxes) {
            if (b.y == boxY) {
                result.add(b);
            }
        }
        return result;
    }
}
