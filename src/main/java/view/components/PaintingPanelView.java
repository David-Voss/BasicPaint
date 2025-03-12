package view.components;

import model.PaintingModel;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

public class PaintingPanelView extends JPanel {
    private PaintingModel paintingModel;

    Dimension paintingPanelDimension;

    private Shape previewShape = null;
    private Point previewPoint = null;

    private boolean isPreviewEraser = false; // Speichert, ob Vorschau für Eraser ist

    public PaintingPanelView() {
        this.paintingPanelDimension = new Dimension(1247, 1247);
        paintingModel = new PaintingModel((int) paintingPanelDimension.getWidth(), (int) paintingPanelDimension.getHeight());
        setPreferredSize(paintingPanelDimension);
    }

    public JPanel getPaintingPanelView() { return this; }
    public PaintingModel getPaintingModel() { return paintingModel; }

    public void setPaintingPanelSize(int width, int height) {
        setPreferredSize(new Dimension(width, height));
        revalidate(); // Aktualisiert das Layout-Management
        repaint(); // Zeichnet das Panel neu
    }

    public void resizePanelWhenOpenedFileIsWiderOrHigher(int width, int height) {
        boolean isWider = width > this.getWidth();
        boolean isHigher = height > this.getWidth();

        if (isWider || isHigher) {
            setPreferredSize(new Dimension(width, height));
            revalidate(); // Aktualisiert das Layout-Management
            repaint(); // Zeichnet das Panel neu
        }
    }

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