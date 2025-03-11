package main;

import controller.MainController;
import model.PaintingModel;
import view.MainWindow;

import javax.swing.*;

public class BasicPaint {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(BasicPaint::run);
    }

    private static void run() {
        MainWindow gui = new MainWindow();
        new MainController(gui);
    }
}
