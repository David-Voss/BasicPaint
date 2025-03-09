package model.elements;

import java.awt.*;

public class LineElement extends GraphicElement {
    private final int x1, y1, x2, y2;

    public LineElement(int x1, int y1, int x2, int y2, Color color, int strokeWidth) {
        super(color, strokeWidth);
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    @Override
    public void draw(Graphics2D g2d) {
        g2d.setColor(color);
        g2d.setStroke(new BasicStroke(strokeWidth));
        g2d.drawLine(x1, y1, x2, y2);
    }
}
