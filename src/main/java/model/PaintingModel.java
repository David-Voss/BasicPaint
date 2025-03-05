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
    private Color previousColour = Color.BLACK;
    private Color tempColour = Color.BLACK;

    private int strokeWidth = 3;

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
    public Graphics2D getG2D() { return g2d; }
    public Color getCurrentColour() { return currentColour; }
    public Color getPreviousColour() { return previousColour; }
    public Color getTempColour() { return tempColour; }
    public int getStrokeWidth() { return strokeWidth; }

    public void setCurrentColour(Color colour) {
        colourChange();
        this.currentColour = colour;
        g2d.setColor(colour);
    }
    public void setStrokeWidth(int strokeWidth) { this.strokeWidth = strokeWidth; }

    public void colourChange() {
        this.tempColour = this.previousColour;
        this.previousColour = this.currentColour;
    }

    public void colourChangeBack() {
        this.currentColour = this.previousColour;
        this.previousColour = this.tempColour;
        g2d.setColor(this.currentColour);
    }

    public Graphics2D getG2d() { return g2d; }

    public void addElement(GraphicElement element) {
        elements.add(element);
        element.draw(g2d);
    }
}