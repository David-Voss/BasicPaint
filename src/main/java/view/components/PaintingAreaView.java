package view.components;

import controller.components.PaintingAreaController;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class PaintingAreaView extends JPanel {

    public PaintingAreaView(int width, int height) {
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.WHITE);
        setDoubleBuffered(true);
    }
}
