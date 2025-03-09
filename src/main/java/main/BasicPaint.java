package main;

import controller.MainController;
import view.MainWindow;

public class BasicPaint {
    public static void main(String[] args) {
        run();
    }

    private static void run() {
        //DrawingModel model = new DrawingModel();
        MainWindow gui = new MainWindow();
        new MainController(gui);
    }
}
