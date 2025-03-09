package view.components;

import model.PaintingModel;

import javax.swing.*;
import java.awt.*;

public class PaintingPanelView extends JPanel {
    private PaintingModel paintingModel;

    private Shape previewShape = null;

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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(paintingModel.getCanvas(), 0, 0, null);

        // Falls eine Vorschau-Form existiert, zeichnen wir sie als gestrichelte Linie
        if (previewShape != null) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setColor(paintingModel.getCurrentColour()); // Vorschau-Farbe
            float[] dashPattern = {5, 5}; // Gestrichelte Linie
            g2d.setStroke(new BasicStroke(paintingModel.getStrokeWidth(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 10, dashPattern, 0));
            g2d.draw(previewShape);
            g2d.dispose();
        }
    }
}