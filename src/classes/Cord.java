package classes;

import java.awt.*;

class Cord {
    private static final Color CORD_COLOR = Color.black;
    static final int CORD_PIVOT_X = 400;
    static final int CORD_PIVOT_Y = 20;
    private static final double MIN_DIVISIONS = 60.0;

    int length;
    double angle;

    private Pendulum pendulum;

    /**
     * Construct a cord for the pendulum.
     */
    Cord(Pendulum pendulum) {
        this.length = 10;
        this.angle = 0.0;
        this.pendulum = pendulum;
    }

    /**
     * Calculate a new cord length given a new weight position.
     */
    void newLength(int x, int y) {
        double len;
        double sqx;
        double sqy;

        sqx = (double) (CORD_PIVOT_X - x) * (double) (CORD_PIVOT_X - x);
        sqy = (double) (CORD_PIVOT_Y - y) * (double) (CORD_PIVOT_Y - y);

        // sqx and sqy will never be negative.
        len = Math.sqrt(sqx + sqy); // no exceptions in floating point math

        length = (int) len;
        newAngle(x);
    }

    /**
     * Calculate the angle between the cord and the vertical.
     */
    private void newAngle(int x) {
        double opp;

        // The angle will be the arcsin of the opposite side
        // (the x distance) over the hypotenuse (the length).
        opp = (double) (x - CORD_PIVOT_X); // note this can be negative.
        if (Math.abs(opp) < 1.0)
            angle = 0.0;
        else
            angle = Math.asin(opp / length);
    }

    /**
     * The pendulum moves faster the smaller the cord length.
     */
    double getDivisions() {
        if ((double) length < MIN_DIVISIONS)
            return MIN_DIVISIONS;
        else
            return (double) length;
    }

    /**
     * Draw the cord.
     */
    void draw(Graphics g) {
        g.setColor(CORD_COLOR);
        g.drawLine(CORD_PIVOT_X, CORD_PIVOT_Y, pendulum.weight.xpos, pendulum.weight.ypos);
    }

}