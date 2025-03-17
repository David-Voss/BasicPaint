package toolbox.paintingtools;

import model.PaintingModel;

import java.awt.*;

/**
 * Handles freehand drawing, including circles and lines, using the current painting settings.
 */
public class FreeDrawing {
    private final PaintingModel paintingModel;
    private final Graphics2D g2d;

    /**
     * Creates a new instance for freehand drawing.
     *
     * @param paintingModel The model managing the canvas and painting properties.
     */
    public FreeDrawing(PaintingModel paintingModel) {
        this.paintingModel = paintingModel;
        this.g2d = paintingModel.getG2D();
    }

    /**
     * Draws a single point at the given coordinates using the selected tool's settings.
     * The size of the point is determined by the current stroke width.
     *
     * @param x The X-coordinate of the point.
     * @param y The Y-coordinate of the point.
     * @param selectedTool The currently selected painting tool.
     */
    public void drawPoint(int x, int y, PaintingTool selectedTool) {
        int size = paintingModel.getStrokeWidth();

        g2d.setColor(selectedTool == PaintingTool.ERASER ?
                paintingModel.getBackgroundColour() : paintingModel.getCurrentColour());

        float correctedX = x - (size / 2.0f);
        float correctedY = y - (size / 2.0f);

        g2d.fillOval(Math.round(correctedX), Math.round(correctedY), size, size);
    }

    /**
     * Draws a freehand line between two points using the selected tool's settings.
     *
     * @param x1 The starting X-coordinate.
     * @param y1 The starting Y-coordinate.
     * @param x2 The ending X-coordinate.
     * @param y2 The ending Y-coordinate.
     * @param isEraser Whether the tool is an eraser (true) or a drawing tool (false).
     */
    public void freeDrawing(int x1, int y1, int x2, int y2, boolean isEraser) {
        g2d.setColor(isEraser ? paintingModel.getBackgroundColour() : paintingModel.getCurrentColour());
        g2d.setStroke(new BasicStroke(paintingModel.getStrokeWidth(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.drawLine(x1, y1, x2, y2);
    }
}
