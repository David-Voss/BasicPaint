package controller;

import controller.components.*;
import toolbox.FileHandler;
import view.MainWindow;

import javax.swing.*;
import java.awt.event.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

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
                System.out.println(time() + ": windowClosing() aufgerufen.");
                boolean canExit = menuBarController.confirmDiscardChanges();
                if (!canExit) {
                    // Falls der Benutzer "Abbrechen" gedrückt hat, NICHT schließen
                    //System.out.print(" -> Schließen abgebrochen. \n");
                    return;
                }

                // Falls der Benutzer "Ja" oder "Nein" gewählt hat, die Anwendung beenden
                System.out.println(time() + ": Anwendung wird geschlossen.");
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

    public static String dateTime() {
        Locale systemLocale = Locale.getDefault();
        ZoneId systemTimeZone = ZoneId.systemDefault();

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(systemLocale);
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(systemLocale);

        ZonedDateTime dateTimeNow = ZonedDateTime.now(systemTimeZone);
        String dateTimeString = dateTimeNow.format(dateFormatter) + " " + dateTimeNow.format(timeFormatter);
        return dateTimeString;
    }

    public static String date() {
        Locale systemLocale = Locale.getDefault();
        ZoneId systemTimeZone = ZoneId.systemDefault();

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).withLocale(systemLocale);

        ZonedDateTime dateTimeNow = ZonedDateTime.now(systemTimeZone);
        String dateString = dateTimeNow.format(dateFormatter);
        return dateString;
    }

    public static String time() {
        Locale systemLocale = Locale.getDefault();
        ZoneId systemTimeZone = ZoneId.systemDefault();

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.LONG).withLocale(systemLocale);

        ZonedDateTime dateTimeNow = ZonedDateTime.now(systemTimeZone);
        String timeString = dateTimeNow.format(timeFormatter);
        return timeString;
    }

    private void showInitialValuesInConsole() {
        System.out.println(date() + "\n" +
                time() + ": BasicPaint gestartet. \n");

        System.out.println(time() + ": Initiale Werte MainWindow:");
        System.out.println(time() + ": MainWindow Höhe = " + mainWindow.getHeight() + " px.");
        System.out.println(time() + ": MainWindow Breite = " + mainWindow.getWidth() + " px. \n");

        mainWindow.getPaintingPanelView().getPaintingModel().showInitialPaintingModelValuesInConsole();
    }
}

