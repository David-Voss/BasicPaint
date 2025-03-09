package view.components;

import model.PaintingModel;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

public class PaintingPanelView extends JPanel {
    private PaintingModel paintingModel;

    private Shape previewShape = null;
    private Point previewPoint = null;

    private boolean isPreviewEraser = false; // Speichert, ob Vorschau für Eraser ist

    public PaintingPanelView(int width, int height) {
        paintingModel = new PaintingModel(width, height);
        setPreferredSize(new Dimension(width, height));
    }

    public JPanel getPaintingPanelView() { return this; }
    public PaintingModel getPaintingModel() { return paintingModel; }

    // 🔹 Setzt das Vorschau-Rechteck (Wird von Controller aktualisiert)
    /*public void setPreviewRectangle(Rectangle rectangle) {
        this.previewShape = rectangle;
        repaint();
    }*/

    /**
     * Setzt die Vorschau-Form während des Zeichnens.
     * @param shape Die gezeichnete Vorschau-Form (Linie, Rechteck oder Ellipse)
     */
    public void setPreviewShape(Shape shape) {
        this.previewShape = shape;
        repaint();
    }

    /**
     * Löscht die Vorschau nach dem Zeichnen.
     */
    public void clearPreviewShape() {
        this.previewShape = null;
        repaint();
    }

    public void setPreviewPoint(Point p, boolean isEraser) {
        this.previewPoint = p;
        this.isPreviewEraser = isEraser; // Speichert, ob Eraser aktiv ist
        repaint();
    }

    public void clearPreviewPoint() {
        this.previewPoint = null;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(paintingModel.getCanvas(), 0, 0, null);

        // Falls eine Vorschau-Form existiert, zeichnen wir sie als gestrichelte Linie
        if (previewShape != null) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setColor(paintingModel.getCurrentColour());
            if (previewShape instanceof Rectangle2D || previewShape instanceof Line2D) {
                g2d.setStroke(new BasicStroke(paintingModel.getStrokeWidth(), BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));
            } else {
                g2d.setStroke(new BasicStroke(paintingModel.getStrokeWidth(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            }
            g2d.draw(previewShape);
            g2d.dispose();
        }
        if (previewPoint != null) {
            Graphics2D g2d = (Graphics2D) g.create();

            if (isPreviewEraser) {
                g2d.setColor(paintingModel.getBackgroundColour()); // Falls Eraser aktiv, ist die Vorschau weiß
            } else {
                g2d.setColor(paintingModel.getCurrentColour()); // Sonst normale Farbe
            }

            int size = paintingModel.getStrokeWidth();
            g2d.fillOval(previewPoint.x - size / 2, previewPoint.y - size / 2, size, size);
            g2d.dispose();
        }
    }
}