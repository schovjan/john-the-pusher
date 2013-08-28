package cz.schovjan.warehouseman;

import cz.schovjan.warehouseman.core.Framework;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author schovjan
 */
public class JohnTheWarehouseman extends JFrame {

    private static Logger log = LogManager.getLogger(JohnTheWarehouseman.class.getName());

    private JohnTheWarehouseman() {
        setTitle("John The Builder");
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(new Framework(800, 600));
        pack();
        setVisible(true);
        setLocationRelativeTo(null);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        // Use the event dispatch thread to build the UI for thread-safety.
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new JohnTheWarehouseman();
            }
        });
    }
}
