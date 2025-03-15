package controller;

import controller.components.*;
import toolbox.FileHandler;
import toolbox.DateTimeStamp;
import view.MainWindow;

import javax.swing.*;
import java.awt.event.*;

public class MainController implements ActionListener {

    private FileHandler fileHandler;

    private final MainWindow mainWindow;
    private final MenuBarController menuBarController;
    private final ToolBarController toolBarController;
    private final PaintingPanelController paintingController;
    private final StatusBarController statusBarController;


    public MainController(MainWindow mainWindow) {
        this.fileHandler = new FileHandler();

        this.mainWindow = mainWindow;
        this.menuBarController = new MenuBarController(mainWindow, this, fileHandler);
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
                System.out.println(timeStamp() + ": windowClosing() aufgerufen.");
                boolean canExit = menuBarController.confirmDiscardChanges();
                if (!canExit) {
                    // Falls der Benutzer "Abbrechen" gedrückt hat, NICHT schließen
                    //System.out.print(" -> Schließen abgebrochen. \n");
                    return;
                }

                // Falls der Benutzer "Ja" oder "Nein" gewählt hat, die Anwendung beenden
                System.out.println(timeStamp() + ": Anwendung wird geschlossen.");
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
                timeStamp() + ": BasicPaint gestartet. \n");

        System.out.println(timeStamp() + ": Initiale Werte MainWindow:");
        System.out.println(timeStamp() + ": MainWindow Höhe = " + mainWindow.getHeight() + " px.");
        System.out.println(timeStamp() + ": MainWindow Breite = " + mainWindow.getWidth() + " px. \n");

        mainWindow.getPaintingPanelView().getPaintingModel().showInitialPaintingModelValuesInConsole();
    }

    public static String timeStamp() {
        return DateTimeStamp.time();
    }
}

