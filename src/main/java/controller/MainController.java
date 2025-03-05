package controller;

import controller.components.MenuBarController;
import controller.components.PaintingPanelController;
import controller.components.StatusBarController;
import controller.components.ToolBarController;
import view.MainWindow;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class MainController implements ActionListener {
    private final MainWindow mainWindow;
    private final MenuBarController menuBarController;
    private final ToolBarController toolBarController;
    private final PaintingPanelController paintingController;
    private final StatusBarController statusBarController;


    public MainController(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        this.menuBarController = new MenuBarController(mainWindow.getMenuBarView());
        this.toolBarController = new ToolBarController(mainWindow.getToolBarView());
        this.paintingController = new PaintingPanelController(mainWindow.getPaintingPanelView(), mainWindow.getToolBarView());
        this.statusBarController = new StatusBarController(mainWindow.getStatusBarView());

        initialiseShortcuts();
        initialiseListeners();
    }

    /**
     * Sets up keyboard shortcuts for the application's menus.
     */
    public void initialiseShortcuts() {
        // Shortcuts 'File' menu
        mainWindow.getMenuBarView().getFileMenu().setMnemonic(KeyEvent.VK_D);
        mainWindow.getMenuBarView().getNewFileItem().setAccelerator(KeyStroke.getKeyStroke('N', InputEvent.CTRL_DOWN_MASK));
        mainWindow.getMenuBarView().getOpenFileItem().setAccelerator(KeyStroke.getKeyStroke('O', InputEvent.CTRL_DOWN_MASK));
        mainWindow.getMenuBarView().getSaveFileItem().setAccelerator(KeyStroke.getKeyStroke('S', InputEvent.CTRL_DOWN_MASK));
        mainWindow.getMenuBarView().getSaveFileAsItem().setAccelerator(KeyStroke.getKeyStroke('S', InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
        mainWindow.getMenuBarView().getPrintDocumentItem().setAccelerator(KeyStroke.getKeyStroke('P', InputEvent.CTRL_DOWN_MASK));

        // Shortcuts 'Edit' menu
        mainWindow.getMenuBarView().getEditMenu().setMnemonic(KeyEvent.VK_B);
        mainWindow.getMenuBarView().getUndoItem().setAccelerator(KeyStroke.getKeyStroke('Z', InputEvent.CTRL_DOWN_MASK));
        mainWindow.getMenuBarView().getRedoItem().setAccelerator(KeyStroke.getKeyStroke('Z', InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
    }

    /**
     * Registers action listeners for menu items and updates web search status.
     */
    private void initialiseListeners() {
        // Register listeners for 'File' menu actions
        addMenuAction(mainWindow.getMenuBarView().getNewFileItem(), "new");
        addMenuAction(mainWindow.getMenuBarView().getOpenFileItem(), "open");
        addMenuAction(mainWindow.getMenuBarView().getSaveFileItem(), "save");
        addMenuAction(mainWindow.getMenuBarView().getSaveFileAsItem(), "save_as");
        addMenuAction(mainWindow.getMenuBarView().getPrintDocumentItem(), "print");

        // Register listeners for 'Edit' menu actions
        addMenuAction(mainWindow.getMenuBarView().getUndoItem(), "undo");
        addMenuAction(mainWindow.getMenuBarView().getRedoItem(), "redo");

        // Register listeners for 'MenuBar-ToolBar-Buttons'
        addMenuAction(mainWindow.getMenuBarView().getNewFileButton(), "new");
        addMenuAction(mainWindow.getMenuBarView().getOpenFileButton(), "open");
        addMenuAction(mainWindow.getMenuBarView().getSaveFileButton(), "save");
        addMenuAction(mainWindow.getMenuBarView().getPrintDocumentButton(), "print");
        addMenuAction(mainWindow.getMenuBarView().getUndoButton(), "undo");
        addMenuAction(mainWindow.getMenuBarView().getRedoButton(), "redo");
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
        String actionCommand = e.getActionCommand();

        switch (actionCommand) {
            // 'File' menu actions
            case "new":
                System.out.println("NEU");
                //fileMenuManager.createNewFile();
                break;
            case "open":
                System.out.println("ÖFFNEN");
                //fileMenuManager.openFile();
                break;
            case "save":
                System.out.println("SPEICHERN");
                //fileMenuManager.saveFile();
                break;
            case "save_as":
                System.out.println("SPEICHERN UNTER");
                //fileMenuManager.saveFileAs();
                break;
            case "print":
                System.out.println("DRUCKEN");
                //fileMenuManager.printDocument();
                break;
            // 'Edit' menu actions
            case "undo":
                System.out.println("RÜCKGÄNGIG");
                //editMenuManager.undo();
                break;
            case "redo":
                System.out.println("WIEDERHERSTELLEN");
                //editMenuManager.redo();
                break;
            default:
                break;
        }
    }
}
