package view.components;

import model.PaintingModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PaintingPanelView extends JPanel {
    private PaintingModel paintingModel;
    private int x1, y1, x2, y2;

    public PaintingPanelView(int width, int height) {
        paintingModel = new PaintingModel(width, height);
        setPreferredSize(new Dimension(width, height));
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                x1 = e.getX();
                y1 = e.getY();
            }
        });
        addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                x2 = e.getX();
                y2 = e.getY();
                paintingModel.drawLine(x1, y1, x2, y2);
                x1 = x2;
                y1 = y2;
                repaint();
            }
        });
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(paintingModel.getCanvas(), 0, 0, null);
    }

    public PaintingModel getPaintingModel() {
        return paintingModel;
    }
}
