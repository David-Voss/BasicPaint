package view.components;

import model.PaintingModel;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

/**
 * A custom JPanel for displaying and handling painting operations.
 */
public class PaintingPanelView extends JPanel {
    private final PaintingModel paintingModel;
    private Dimension paintingPanelDimension;

    private Shape previewShape = null;
    private Point previewPoint = null;
    private boolean isPreviewEraser = false; // Indicates if the preview is for the eraser

    /**
     * Constructs a new painting panel with a default size.
     */
    public PaintingPanelView() {
        this.paintingPanelDimension = new Dimension(1247, 1247);
        this.paintingModel = new PaintingModel(
                (int) paintingPanelDimension.getWidth(),
                (int) paintingPanelDimension.getHeight()
        );
        setPreferredSize(paintingPanelDimension);
        setOpaque(true);
    }

    /**
     * Retrieves the painting model associated with this panel.
     *
     * @return The {@link PaintingModel} instance.
     */
    public PaintingModel getPaintingModel() { return paintingModel; }

    /**
     * Sets a preview shape to be displayed temporarily while drawing.
     *
     * @param shape The shape to preview (line, rectangle, or ellipse).
     */
    public void setPreviewShape(Shape shape) {
        this.previewShape = shape;
        repaint();
    }

    /**
     * Clears the preview shape after drawing is completed.
     */
    public void clearPreviewShape() {
        this.previewShape = null;
        repaint();
    }

    /**
     * Sets the preview point for eraser or brush cursor indication.
     *
     * @param point      The point to be previewed.
     * @param isEraser   True if eraser is active, false for normal brush.
     */
    public void setPreviewPoint(Point point, boolean isEraser) {
        this.previewPoint = point;
        this.isPreviewEraser = isEraser;
        repaint();
    }

    /**
     * Clears the preview point, removing the visual indicator.
     */
    public void clearPreviewPoint() {
        this.previewPoint = null;
        repaint();
    }

    /**
     * Paints the component, rendering the image and any previews.
     *
     * @param g The {@link Graphics} context.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(paintingModel.getCanvas(), 0, 0, null);

        Graphics2D g2d = (Graphics2D) g.create();
        drawPreviewShape(g2d);
        drawPreviewPoint(g2d);
        g2d.dispose();
    }

    /**
     * Draws the preview shape if one exists.
     *
     * @param g2d The {@link Graphics2D} context.
     */
    private void drawPreviewShape(Graphics2D g2d) {
        if (previewShape == null) return;

        g2d.setColor(paintingModel.getCurrentColour());

        if (previewShape instanceof Rectangle2D || previewShape instanceof Line2D) {
            g2d.setStroke(new BasicStroke(
                    paintingModel.getStrokeWidth(),
                    BasicStroke.CAP_BUTT,
                    BasicStroke.JOIN_MITER
            ));
        } else {
            g2d.setStroke(new BasicStroke(
                    paintingModel.getStrokeWidth(),
                    BasicStroke.CAP_ROUND,
                    BasicStroke.JOIN_ROUND
            ));
        }

        g2d.draw(previewShape);
    }

    /**
     * Draws the preview point (eraser or brush cursor).
     *
     * @param g2d The {@link Graphics2D} context.
     */
    private void drawPreviewPoint(Graphics2D g2d) {
        if (previewPoint == null) return;

        g2d.setColor(isPreviewEraser ? paintingModel.getBackgroundColour() : paintingModel.getCurrentColour());

        int size = paintingModel.getStrokeWidth();
        float correctedX = previewPoint.x - (size / 2.0f);
        float correctedY = previewPoint.y - (size / 2.0f);

        g2d.fillOval(Math.round(correctedX), Math.round(correctedY), size, size);
    }
}