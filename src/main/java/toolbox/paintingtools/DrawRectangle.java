package toolbox.paintingtools;

import model.PaintingModel;

import java.awt.*;

/**
 * Draws rectangular shapes on the canvas using the current painting settings.
 */
public class DrawRectangle {
    private final PaintingModel paintingModel;
    private final Graphics2D g2d;

    /**
     * Creates a new instance for drawing rectangles.
     *
     * @param paintingModel The model managing the canvas and painting properties.
     */
    public DrawRectangle(PaintingModel paintingModel) {
        this.paintingModel = paintingModel;
        this.g2d = paintingModel.getG2D();
    }

    /**
     * Draws a rectangle from (x1, y1) to (x2, y2) using the current stroke and colour.
     *
     * @param x1 The X-coordinate of the first corner.
     * @param y1 The Y-coordinate of the first corner.
     * @param x2 The X-coordinate of the opposite corner.
     * @param y2 The Y-coordinate of the opposite corner.
     */
    public void drawRectangle(int x1, int y1, int x2, int y2) {
        g2d.setColor(paintingModel.getCurrentColour());
        int x = Math.min(x1, x2);
        int y = Math.min(y1, y2);
        int width = Math.abs(x2 - x1);
        int height = Math.abs(y2 - y1);
        g2d.setStroke(new BasicStroke(
                paintingModel.getStrokeWidth(),
                BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER
        ));
        g2d.drawRect(x, y, width, height);
    }
}
