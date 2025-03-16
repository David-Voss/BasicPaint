package main;

import controller.MainController;
import view.MainWindow;

import javax.swing.*;

/**
 * Entry point for the Paint Application.
 * Starts the GUI in the Event Dispatch Thread (EDT).
 */
public class BasicPaint {

    /**
     * Launches the application.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(BasicPaint::runApplication);
    }

    /**
     * Initialises the application.
     */
    private static void runApplication() {
        MainWindow gui = new MainWindow();
        new MainController(gui);
    }
}
