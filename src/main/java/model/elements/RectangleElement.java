package model.elements;

import java.awt.*;

public class RectangleElement extends GraphicElement {
    private final int x, y, width, height;

    public RectangleElement(int x, int y, int width, int height, Color color, int strokeWidth) {
        super(color, strokeWidth);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public void draw(Graphics2D g2d) {
        g2d.setColor(color);
        g2d.setStroke(new BasicStroke(strokeWidth));
        g2d.drawRect(x, y, width, height);
    }
}
