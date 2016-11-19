package classes;

import java.awt.*;

class Weight {
    private static final Color WEIGHT_COLOR = Color.ORANGE;
    private static final int WEIGHT_DIAMETER = 50;
    private static final int RIGHT = 1;
    private static final int LEFT = -1;

    int xpos;
    int ypos;

    private int direction;
    private int height;
    private Pendulum pendulum;
    private boolean reverseOk = true;
    private double speed;
    private double fastestSpeed;

    /**
     * Construct the weight at end of the pendulum.
     */
    Weight(Pendulum pendulum) {
        this.direction = 0;
        this.speed = 0.0;
        this.fastestSpeed = 0.0;
        this.xpos = Cord.CORD_PIVOT_X;
        this.ypos = Cord.CORD_PIVOT_Y + 150;
        this.height = ypos;
        this.pendulum = pendulum;
    }

    /**
     * The primary method that moves the pendulum a little at a time.
     */
    void swing() {
        double delta;
        double divisions;
        double theta;

        // Change the angle to move a little.
        divisions = pendulum.cord.getDivisions();
        delta = speed * (Math.PI / divisions) * (double) direction;

        theta = pendulum.cord.angle + delta;
        // I have a new theta and the same length. Where are we now?

        xpos = (int) (Math.sin(theta) * (double) pendulum.cord.length) +
                Cord.CORD_PIVOT_X;
        ypos = (int) (Math.cos(theta) * (double) pendulum.cord.length) +
                Cord.CORD_PIVOT_Y;

        // Is it okay to possibly reverse direction?
        if (sign(theta) != sign(pendulum.cord.angle))
            reverseOk = true;

        // Set the cord's new angle with the vertical.
        pendulum.cord.angle = theta;

        // Change the swing's direction if necessary.
        if (ypos < height && reverseOk) {
            direction = -direction;
            reverseOk = false;
        }

        // Adjust the weight's speed to match the height in its swing.
        speed = newSpeed();
    }

    private double newSpeed() {
        double ratio;
        ratio = ((double) (ypos - height)) / ((double) ((pendulum.cord.length +
                Cord.CORD_PIVOT_Y) - height));

        if (ratio < 0.0)
            ratio = 0.0;
        return ((ratio + .1) * fastestSpeed);
    }

    /**
     * Set the weight to where the user drags it.
     */
    void newPosition(int x, int y) {
        xpos = x;
        ypos = y;

        // Prepare to swing the in the appropriate direction.
        speed = 0.1;

        if (x < Cord.CORD_PIVOT_X)
            // The user has dragged the weight to the left;
            // prepare to swing right.
            direction = RIGHT;
        else
            // Prepare to swing left.
            direction = LEFT;

        // Tell the cord to update.
        pendulum.cord.newLength(xpos, ypos);

        // Set the initial height.
        height = ypos;

        // Don't try to reverse the direction of the pendulum's swing
        // until it has swung back through the center.
        reverseOk = false;

        // Acquire a new speed.
        // SQRT(2 * Gravity in m/s/s) length in meters (1 pixel = 1 dm) *
        //         (1 - cos(initial angle)) )
        fastestSpeed = Math.sqrt(2.0 * 10.0 * (double) pendulum.cord.length / 10.0 *
                (1.0 - Math.cos(pendulum.cord.angle)));

    }

    /**
     * Draw the weight.
     */
    void draw(Graphics g) {
        int radius = WEIGHT_DIAMETER / 2;
        g.setColor(WEIGHT_COLOR);
        g.fillOval(xpos - radius, ypos - radius, WEIGHT_DIAMETER,
                WEIGHT_DIAMETER);
    }

    /**
     * Private method to assist with the calculations for swinging.
     */
    private int sign(double value) {
        if (value > 0)
            return 1;
        else
            return -1;
    }

}