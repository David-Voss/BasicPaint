package view.components;

import toolbox.CreateIcon;

import javax.swing.*;
import java.awt.*;

/**
 * Represents the menu bar of the application.
 * Provides access to file operations, editing functionalities,
 * and a toolbar for quick actions.
 */
public class MenuBarView extends JMenuBar{

    // File menu and its items
    private JMenu fileMenu;
    private JMenuItem openFileItem;
    private JMenuItem newFileItem;
    private JMenuItem saveFileItem;
    private JMenuItem saveFileAsItem;
    private JMenuItem printDocumentItem;
    private JMenuItem imageProperties;

    // Edit menu and its items
    private JMenu editMenu;
    private JMenuItem undoItem;
    private JMenuItem redoItem;

    // MenuBar-ToolBar
    private JToolBar menuBarToolBar;
    private JButton newFileButton;
    private JButton openFileButton;
    private JButton saveFileButton;
    private JButton printDocumentButton;
    private JButton undoButton;
    private JButton redoButton;

    /**
     * Constructs the MenuBarView and initializes all its components.
     */
    public MenuBarView() {
        super();
        initFileMenu();
        initEditMenu();
        addSeparator();
        initMenuBarToolBar();
    }

    /**
     * Getter methods for accessing MenuBar components.
     */
    // File menu getter
    public JMenu getFileMenu() { return fileMenu; }
    public JMenuItem getOpenFileItem() { return openFileItem; }
    public JMenuItem getNewFileItem() { return newFileItem; }
    public JMenuItem getSaveFileItem() { return saveFileItem; }
    public JMenuItem getSaveFileAsItem() { return saveFileAsItem; }
    public JMenuItem getPrintDocumentItem() { return printDocumentItem; }
    public JMenuItem getImageProperties() { return imageProperties; }

    // Edit menu getter
    public JMenu getEditMenu() { return editMenu; }
    public JMenuItem getUndoItem() { return undoItem; }
    public JMenuItem getRedoItem() {  return redoItem; }

    // MenuBar-ToolBar getter
    public JButton getNewFileButton() { return newFileButton; }
    public JButton getOpenFileButton() { return openFileButton; }
    public JButton getSaveFileButton() { return saveFileButton; }
    public JButton getPrintDocumentButton() { return printDocumentButton; }
    public JButton getUndoButton() { return undoButton; }
    public JButton getRedoButton() { return redoButton; }

    /**
     * Initializes the File menu and its menu items.
     */
    private void initFileMenu() {
        this.fileMenu = new JMenu("Datei");
        this.newFileItem = new JMenuItem("Neu");
        this.openFileItem = new JMenuItem("Öffnen");
        this.saveFileItem = new JMenuItem("Speichern");
        this.saveFileAsItem = new JMenuItem("Speichern unter");
        this.printDocumentItem = new JMenuItem("Drucken");
        this.imageProperties = new JMenuItem("Bildeigenschaften");

        fileMenu.add(newFileItem);
        fileMenu.add(openFileItem);
        fileMenu.add(saveFileItem);
        fileMenu.add(saveFileAsItem);
        fileMenu.addSeparator();
        fileMenu.add(printDocumentItem);
        fileMenu.addSeparator();
        fileMenu.add(imageProperties);

        add(fileMenu);
    }

    /**
     * Initializes the Edit menu and its menu items.
     */
    private void initEditMenu() {
        this.editMenu = new JMenu("Bearbeiten");
        this.undoItem = new JMenuItem("Rückgängig");
        this.redoItem = new JMenuItem("Wiederherstellen");

        editMenu.add(undoItem);
        editMenu.add(redoItem);

        add(editMenu);
    }

    /**
     * Initializes the toolbar inside the menu bar with quick action buttons.
     */
    private void initMenuBarToolBar() {
        this.menuBarToolBar = new JToolBar();
        this.newFileButton = CreateIcon.createButton("assets/icons/file-image-regular.png", "Neues Bild erstellen [STRG + N]");
        this.openFileButton = CreateIcon.createButton("assets/icons/folder-open-regular.png", "Datei öffnen [STRG + O]");
        this.saveFileButton = CreateIcon.createButton("assets/icons/floppy-disk-regular.png", "Speichern [STRG + S]");
        this.printDocumentButton = CreateIcon.createButton("assets/icons/print-solid.png", "Drucken [STRG + P]");
        this.undoButton = CreateIcon.createButton("assets/icons/rotate-left-solid.png", "Rückgängig [STRG + Z]");
        this.redoButton = CreateIcon.createButton("assets/icons/rotate-right-solid.png", "Wiederherstellen [STRG + SHIFT + Z]");

        menuBarToolBar.add(newFileButton);
        menuBarToolBar.add(openFileButton);
        menuBarToolBar.add(saveFileButton);
        menuBarToolBar.addSeparator();
        menuBarToolBar.add(printDocumentButton);
        menuBarToolBar.addSeparator();
        menuBarToolBar.add(undoButton);
        menuBarToolBar.add(redoButton);

        menuBarToolBar.setFloatable(false);
        menuBarToolBar.setBorder(BorderFactory.createEmptyBorder(3, 0, 2, 0));

        add(menuBarToolBar);
    }

    /**
     * Adds a vertical separator to visually distinguish different sections in the toolbar.
     */
    private void addSeparator() {
        add(Box.createHorizontalStrut(10)); // Adds spacing
        JSeparator separator = new JSeparator(SwingConstants.VERTICAL);
        separator.setPreferredSize(new Dimension(2, 15)); // Set width & height
        add(separator);
        add(Box.createHorizontalStrut(10)); // Adds spacing
    }
}
