package cz.schovjan.warehouseman.core;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JPanel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author schovjan
 */
public abstract class Canvas extends JPanel implements KeyListener {

    private Logger log = LogManager.getLogger();
    // Keyboard states - Here are stored states for keyboard keys - is it down or not.
    private static boolean[] keyboardState = new boolean[525];

    public Canvas(int width, int height) {
        // We use double buffer to draw on the screen.
        this.setDoubleBuffered(true);
        this.setFocusable(true);
        this.setBackground(Color.black);
        this.setPreferredSize(new Dimension(width, height));

        // Adds the keyboard listener to JPanel to receive key events from this component.
        this.addKeyListener(this);
    }

    // This method is overridden in Framework.java and is used for drawing to the screen.
    public abstract void draw(Graphics2D g2d);

    public abstract void keyReleasedFramework(KeyEvent e);

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        super.paintComponent(g2d);
        draw(g2d);
    }

    /**
     * Is keyboard key "key" down?
     *
     * @param key Number of key for which you want to check the state.
     * @return true if the key is down, false if the key is not down.
     */
    public static boolean keyboardKeyState(int key) {
        return keyboardState[key];
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        keyboardState[e.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keyboardState[e.getKeyCode()] = false;
        keyReleasedFramework(e);
    }
}
