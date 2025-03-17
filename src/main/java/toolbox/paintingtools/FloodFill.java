package toolbox.paintingtools;

import toolbox.LoggingHelper;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Implements the flood fill (paint bucket) algorithm for filling an area with a new colour.
 * Ensures that the fill operation remains within the image boundaries and respects a tolerance level.
 */
public class FloodFill {
    private final BufferedImage canvas;
    private final boolean[][] visited; // To track processed pixels

    /**
     * Constructs a FloodFill instance with the specified canvas.
     *
     * @param canvas The image on which the flood fill operation will be performed.
     */
    public FloodFill(BufferedImage canvas) {
        this.canvas = canvas;
        this.visited = new boolean[canvas.getWidth()][canvas.getHeight()];
    }

    /**
     * Performs the flood fill operation using an optimised iterative approach.
     *
     * @param x         The x-coordinate of the starting point.
     * @param y         The y-coordinate of the starting point.
     * @param newColor  The colour to apply to the filled area.
     * @param tolerance The allowed colour difference for adjacent pixels.
     */
    public void fill(int x, int y, Color newColor, int tolerance) {
        if (!isInsideBounds(x, y)) {
            LoggingHelper.log("FloodFill: Startkoordinaten außerhalb des gültigen Bereichs! (" + x + ", " + y + ")");
            return;
        }

        int targetColor = canvas.getRGB(x, y);
        if (targetColor == newColor.getRGB()) return;

        Deque<Point> stack = new ArrayDeque<>();
        stack.push(new Point(x, y));

        while (!stack.isEmpty()) {
            Point p = stack.pop();

            // Ensure pixel is within bounds and has not been visited
            if (!isInsideBounds(p.x, p.y) || visited[p.x][p.y]) continue;

            // Check if the colour is within the tolerance range
            if (!colorWithinTolerance(canvas.getRGB(p.x, p.y), targetColor, tolerance)) continue;

            // Fill the pixel with the new colour
            canvas.setRGB(p.x, p.y, newColor.getRGB());
            visited[p.x][p.y] = true;

            // Add neighbouring pixels to the stack
            stack.push(new Point(p.x + 1, p.y));
            stack.push(new Point(p.x - 1, p.y));
            stack.push(new Point(p.x, p.y + 1));
            stack.push(new Point(p.x, p.y - 1));
        }
    }

    /**
     * Checks whether the given coordinates are within the image boundaries.
     *
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @return true if the coordinates are within bounds, false otherwise.
     */
    private boolean isInsideBounds(int x, int y) {
        return x >= 0 && y >= 0 && x < canvas.getWidth() && y < canvas.getHeight();
    }

    /**
     * Determines whether two colours are within the specified tolerance.
     *
     * @param color1    The first colour value.
     * @param color2    The second colour value.
     * @param tolerance The acceptable colour difference.
     * @return true if the colours are within the tolerance, false otherwise.
     */
    private boolean colorWithinTolerance(int color1, int color2, int tolerance) {
        Color c1 = new Color(color1, true);
        Color c2 = new Color(color2, true);
        return (Math.abs(c1.getRed() - c2.getRed()) <= tolerance &&
                Math.abs(c1.getGreen() - c2.getGreen()) <= tolerance &&
                Math.abs(c1.getBlue() - c2.getBlue()) <= tolerance);
    }
}
