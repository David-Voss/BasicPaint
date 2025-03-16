package controller;

import controller.components.*;
import toolbox.DateTimeStamp;
import toolbox.LoggingHelper;
import view.MainWindow;

import javax.swing.*;
import java.awt.event.*;

public class MainController implements ActionListener {

    private final MainWindow mainWindow;
    private final MenuBarController menuBarController;
    private final ToolBarController toolBarController;
    private final PaintingPanelController paintingController;
    private final StatusBarController statusBarController;


    public MainController(MainWindow mainWindow) {

        this.mainWindow = mainWindow;
        this.menuBarController = new MenuBarController(mainWindow, this);
        this.toolBarController = new ToolBarController(mainWindow, this);
        this.paintingController = new PaintingPanelController(mainWindow, this);
        this.statusBarController = new StatusBarController(mainWindow, this);

        initialiseListeners();
        showInitialValuesInConsole();
    }

    public MenuBarController getMenuBarController() { return menuBarController; }
    public ToolBarController getToolBarController() { return toolBarController; }
    public PaintingPanelController getPaintingPanelController() { return paintingController; }
    public StatusBarController getStatusBarController() { return statusBarController; }

    /**
     * Registers action listeners for menu items and updates web search status.
     */
    private void initialiseListeners() {
        // Add a window listener to prompt for unsaved changes on close.
        mainWindow.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                LoggingHelper.log("windowClosing() aufgerufen.");
                boolean canExit = menuBarController.confirmDiscardChanges();
                if (!canExit) {
                    // Falls der Benutzer "Abbrechen" gedrückt hat, NICHT schließen
                    //System.out.print(" -> Schließen abgebrochen. \n");
                    return;
                }

                // Falls der Benutzer "Ja" oder "Nein" gewählt hat, die Anwendung beenden
                LoggingHelper.log("Anwendung wird geschlossen.");
                mainWindow.dispose();
                System.exit(0);
            }
        });
    }

    /**
     * Adds an action listener to a menu item with a specific action command.
     *
     * @param menuItem The menu item to which the listener is added.
     * @param command  The action command for event handling.
     */
    private void addMenuAction(JMenuItem menuItem, String command) {
        menuItem.addActionListener(this);
        menuItem.setActionCommand(command);
    }

    /**
     * Adds an action listener to a button with a specific action command.
     *
     * @param jButton The button to which the listener is added.
     * @param command The action command for event handling.
     */
    private void addMenuAction(JButton jButton, String command) {
        jButton.addActionListener(this);
        jButton.setActionCommand(command);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    private void showInitialValuesInConsole() {
        System.out.println(DateTimeStamp.date() + "\n" +
                DateTimeStamp.time() + ": BasicPaint gestartet. \n");

        LoggingHelper.log("Initiale Werte MainWindow:");
        LoggingHelper.log("MainWindow Höhe = " + mainWindow.getHeight() + " px.");
        LoggingHelper.log("MainWindow Breite = " + mainWindow.getWidth() + " px. \n");

        mainWindow.getPaintingPanelView().getPaintingModel().showInitialPaintingModelValuesInConsole();
    }
}

