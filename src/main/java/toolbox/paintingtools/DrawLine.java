package toolbox.paintingtools;

import model.PaintingModel;

import java.awt.*;

/**
 * Handles the drawing of lines on the canvas using the current painting settings.
 */
public class DrawLine {
    private final PaintingModel paintingModel;
    private final Graphics2D g2d;

    /**
     * Creates a new instance for drawing lines.
     *
     * @param paintingModel The model managing the canvas and painting properties.
     */
    public DrawLine(PaintingModel paintingModel) {
        this.paintingModel = paintingModel;
        this.g2d = paintingModel.getG2D();
    }

    /**
     * Draws a straight line from (x1, y1) to (x2, y2) using the current stroke and colour.
     *
     * @param x1 The starting X-coordinate.
     * @param y1 The starting Y-coordinate.
     * @param x2 The ending X-coordinate.
     * @param y2 The ending Y-coordinate.
     */
    public void drawLine(int x1, int y1, int x2, int y2) {
        g2d.setColor(paintingModel.getCurrentColour());
        g2d.setStroke(new BasicStroke(
                paintingModel.getStrokeWidth(),
                BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER
        ));
        g2d.drawLine(x1, y1, x2, y2);
    }
}
