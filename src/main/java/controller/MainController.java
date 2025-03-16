package controller;

import controller.components.*;
import toolbox.DateTimeStamp;
import toolbox.LoggingHelper;
import view.MainWindow;

import java.awt.event.*;

/**
 * Main controller handling all major interactions between the UI components.
 */
public class MainController {

    private final MainWindow mainWindow;
    private final MenuBarController menuBarController;
    private final ToolBarController toolBarController;
    private final PaintingPanelController paintingController;
    private final StatusBarController statusBarController;


    /**
     * Constructs the main controller and initialises all sub-controllers.
     *
     * @param mainWindow The main application window.
     */
    public MainController(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        this.menuBarController = new MenuBarController(mainWindow, this);
        this.toolBarController = new ToolBarController(mainWindow, this);
        this.paintingController = new PaintingPanelController(mainWindow, this);
        this.statusBarController = new StatusBarController(mainWindow, this);

        initialiseListeners();
        logInitialApplicationState();
    }

    /**
     * Getter methods for accessing MainController components.
     */
    public MenuBarController getMenuBarController() { return menuBarController; }
    public ToolBarController getToolBarController() { return toolBarController; }
    public PaintingPanelController getPaintingPanelController() { return paintingController; }
    public StatusBarController getStatusBarController() { return statusBarController; }

    /**
     * Registers action listeners for the main window.
     * Prompts the user to confirm unsaved changes before exiting.
     */
    private void initialiseListeners() {
        mainWindow.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                handleWindowClosing();
            }
        });
    }

    /**
     * Handles window closing event, prompting for unsaved changes before exiting.
     */
    private void handleWindowClosing() {
        LoggingHelper.log("windowClosing() aufgerufen.");
        if (!menuBarController.confirmDiscardChanges()) {
            return;
        }
        LoggingHelper.log("Anwendung wird geschlossen.");
        mainWindow.dispose();
        System.exit(0);
    }

    /**
     * Logs the initial state of the application components.
     */
    private void logInitialApplicationState() {
        System.out.println(DateTimeStamp.date() + "\n" +
                DateTimeStamp.time() + ": BasicPaint gestartet. \n");

        LoggingHelper.log("Initiale Werte MainWindow:");
        LoggingHelper.log("MainWindow Höhe = " + mainWindow.getHeight() + " px.");
        LoggingHelper.log("MainWindow Breite = " + mainWindow.getWidth() + " px. \n");

        mainWindow.getPaintingPanelView().getPaintingModel().showInitialPaintingModelValuesInConsole();
    }
}

