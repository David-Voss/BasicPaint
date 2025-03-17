package toolbox.paintingtools;

import model.PaintingModel;

import java.awt.*;

/**
 * Draws basic shapes on the canvas using the current painting settings.
 */
public class DrawEllipse {
    private final PaintingModel paintingModel;
    private final Graphics2D g2d;

    /**
     * Creates a new instance for drawing ellipses.
     *
     * @param paintingModel The model managing the canvas and painting properties.
     */
    public DrawEllipse(PaintingModel paintingModel) {
        this.paintingModel = paintingModel;
        this.g2d = paintingModel.getG2D();
    }

    /**
     * Draws an ellipse from (x1, y1) to (x2, y2) using the current stroke and colour.
     *
     * @param x1 The X-coordinate of the first corner of the bounding box.
     * @param y1 The Y-coordinate of the first corner of the bounding box.
     * @param x2 The X-coordinate of the opposite corner of the bounding box.
     * @param y2 The Y-coordinate of the opposite corner of the bounding box.
     */
    public void drawEllipse(int x1, int y1, int x2, int y2) {
        g2d.setColor(paintingModel.getCurrentColour());
        int x = Math.min(x1, x2);
        int y = Math.min(y1, y2);
        int width = Math.abs(x2 - x1);
        int height = Math.abs(y2 - y1);
        g2d.setStroke(new BasicStroke(
                paintingModel.getStrokeWidth(),
                BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_ROUND
        ));
        g2d.drawOval(x, y, width, height);
    }
}
