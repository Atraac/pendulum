package classes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Pendulum extends JPanel implements Runnable {

    private static final int WINDOW_HEIGHT = 600;
    private static final int WINDOW_WIDTH = 800;

    Cord cord;
    Weight weight;

    private boolean freeze = true;

    private Pendulum() throws HeadlessException {
        setDoubleBuffered(true);

        this.cord = new Cord(this);
        this.weight = new Weight(this);

        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                freeze = true;
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                freeze = false;
                repaint();
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                weight.newPosition(e.getX(), e.getY());
                repaint();
            }
        });
    }

    /**
     * Keep on swinging and redrawing the pendulum.
     */
    @SuppressWarnings("InfiniteLoopStatement")
    public void run() {
        while(true) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (!freeze) {
                weight.swing();
            }

            repaint();
        }
    }

    /**
     * Draw the pieces of the pendulum.
     */
    public void paint(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());

        cord.draw(g);
        weight.draw(g);

        // Draw a little red spot where the pivot is.
        g.setColor(Color.RED);
        g.fillOval(Cord.CORD_PIVOT_X - 5, Cord.CORD_PIVOT_Y - 5, 10, 10);
    }

    /**
     * Resize the window
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT);
    }

    /**
     * Provide a main routine so that we can run stand-alone.
     */
    public static void main(String args[]) {
        Pendulum pendulum = new Pendulum();

        JFrame frame = new JFrame("classes.Pendulum");
        frame.add(pendulum);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        new Thread(pendulum).start();
    }
}
