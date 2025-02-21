package model.elements;

import model.GraphicElement;

import java.awt.*;

public class CircleElement extends GraphicElement {
    private final int x, y, diameter;

    public CircleElement(int x, int y, int diameter, Color color, float strokeWidth) {
        super(color, strokeWidth);
        this.x = x;
        this.y = y;
        this.diameter = diameter;
    }

    @Override
    public void draw(Graphics2D g2d) {
        g2d.setColor(color);
        g2d.setStroke(new BasicStroke(strokeWidth));
        g2d.drawOval(x, y, diameter, diameter);
    }
}
