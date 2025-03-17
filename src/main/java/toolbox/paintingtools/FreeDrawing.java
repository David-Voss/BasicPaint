package toolbox.paintingtools;

import model.PaintingModel;

import java.awt.*;


public class FreeDrawing {
    private final PaintingModel paintingModel;
    private final Graphics2D g2d;

    public FreeDrawing(PaintingModel paintingModel) {
        this.paintingModel = paintingModel;
        this.g2d = paintingModel.getG2D();
    }

    public void fillCircle(int centerX, int centerY, int radius, PaintingTool selectedTool) {
        if (selectedTool == PaintingTool.ERASER) {
            g2d.setColor(paintingModel.getBackgroundColour());
        } else {
            g2d.setColor(paintingModel.getCurrentColour());
        }
        for (int y = -radius; y <= radius; y++) {
            for (int x = -radius; x <= radius; x++) {
                if (x * x + y * y <= radius * radius) {
                    g2d.fillRect(centerX + x, centerY + y, 1, 1);
                }
            }
        }
    }

    public void drawCircle(int centerX, int centerY, int radius) {
        int x = 0;
        int y = radius;
        int d = 3 - 2 * radius;

        while (y >= x) {
            g2d.fillRect(centerX + x, centerY + y, 1, 1);
            g2d.fillRect(centerX - x, centerY + y, 1, 1);
            g2d.fillRect(centerX + x, centerY - y, 1, 1);
            g2d.fillRect(centerX - x, centerY - y, 1, 1);
            g2d.fillRect(centerX + y, centerY + x, 1, 1);
            g2d.fillRect(centerX - y, centerY + x, 1, 1);
            g2d.fillRect(centerX + y, centerY - x, 1, 1);
            g2d.fillRect(centerX - y, centerY - x, 1, 1);

            x++;
            if (d > 0) {
                y--;
                d = d + 4 * (x - y) + 10;
            } else {
                d = d + 4 * x + 6;
            }
        }
    }

    public void freeDrawing(int x1, int y1, int x2, int y2, boolean isEraser) {
        g2d.setColor(isEraser ? paintingModel.getBackgroundColour() : paintingModel.getCurrentColour());
        g2d.setStroke(new BasicStroke(paintingModel.getStrokeWidth(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.drawLine(x1, y1, x2, y2);
    }
}
