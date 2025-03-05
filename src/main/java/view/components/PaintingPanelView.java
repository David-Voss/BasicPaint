package view.components;

import model.PaintingModel;

import javax.swing.*;
import java.awt.*;

public class PaintingPanelView extends JPanel {
    private PaintingModel paintingModel;

    public PaintingPanelView(int width, int height) {
        paintingModel = new PaintingModel(width, height);
        setPreferredSize(new Dimension(width, height));
    }

    public JPanel getPaintingPanelView() { return this; }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(paintingModel.getCanvas(), 0, 0, null);
    }

    public PaintingModel getPaintingModel() {
        return paintingModel;
    }
}