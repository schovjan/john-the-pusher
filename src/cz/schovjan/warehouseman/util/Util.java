package cz.schovjan.warehouseman.util;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
import javax.imageio.ImageIO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author schovjan
 */
public class Util {

    private static Logger log = LogManager.getLogger();
    private static Random random = new Random();

    /**
     * Nacte a vrati obrazek nebo null
     *
     * @param name
     * @return
     */
    public static BufferedImage loadImage(String name) {
        BufferedImage img = null;
        try {
            URL imgUrl = Util.class.getResource(Constant.RESOURCE_PATH + "images/" + name);
            img = ImageIO.read(imgUrl);
        } catch (IOException ex) {
            log.trace(ex);
        }
        return img;
    }

    public static int rand(int to) {
        return random.nextInt(to);
    }
}
