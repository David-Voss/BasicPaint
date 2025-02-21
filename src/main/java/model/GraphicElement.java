package model;

import java.awt.*;

public abstract class GraphicElement {
    protected Color color;
    protected float strokeWidth;

    public GraphicElement(Color color, float strokeWidth) {
        this.color = color;
        this.strokeWidth = strokeWidth;
    }

    public abstract void draw(Graphics2D g2d);

    public Color getColor() {
        return color;
    }

    public float getStrokeWidth() {
        return strokeWidth;
    }
}