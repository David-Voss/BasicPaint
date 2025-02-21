package model;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class PaintingModel {
    private final BufferedImage canvas;
    private final Graphics2D g2d;
    private final List<GraphicElement> elements;
    private Color currentColour = Color.BLACK;
    private int strokeWidth = 2;

    public PaintingModel(int width, int height) {
        canvas = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        g2d = canvas.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, height);
        g2d.setColor(currentColour);
        elements = new ArrayList<>();
    }

    public BufferedImage getCanvas() { return canvas; }

    public void setColor(Color color) {
        this.currentColour = color;
        g2d.setColor(color);
    }
    public void setStrokeWidth(int width) { this.strokeWidth = width; }

    public void addElement(GraphicElement element) {
        elements.add(element);
        element.draw(g2d);
    }

    public void drawLine(int x1, int y1, int x2, int y2) {
        g2d.setStroke(new BasicStroke(strokeWidth));
        g2d.drawLine(x1, y1, x2, y2);
    }
}
