package view.components;

import toolbox.CreateImageButton;

import javax.swing.*;
import java.awt.*;

public class MenuBarView extends JMenuBar{

    // File menu and its items
    JMenu fileMenu;
    JMenuItem openFileItem;
    JMenuItem newFileItem;
    JMenuItem saveFileItem;
    JMenuItem saveFileAsItem;
    JMenuItem printDocumentItem;

    // Edit menu and its items
    JMenu editMenu;
    JMenuItem undoItem;
    JMenuItem redoItem;

    // MenuBar-ToolBar
    JToolBar menuBarToolBar;
    JButton newFileButton;
    JButton openFileButton;
    JButton saveFileButton;
    JButton printDocumentButton;
    JButton undoButton;
    JButton redoButton;

    public MenuBarView() {
        super();
        this.fileMenu = new JMenu("Datei");
        this.newFileItem = new JMenuItem("Neu");
        this.openFileItem = new JMenuItem("Öffnen");
        this.saveFileItem = new JMenuItem("Speichern");
        this.saveFileAsItem = new JMenuItem("Speichern unter");
        this.printDocumentItem = new JMenuItem("Drucken");

        add(fileMenu);
        fileMenu.add(newFileItem);
        fileMenu.add(openFileItem);
        fileMenu.add(saveFileItem);
        fileMenu.add(saveFileAsItem);
        fileMenu.addSeparator();
        fileMenu.add(printDocumentItem);

        this.editMenu = new JMenu("Bearbeiten");
        this.undoItem = new JMenuItem("Rückgängig");
        this.redoItem = new JMenuItem("Wiederherstellen");

        add(editMenu);
        editMenu.add(undoItem);
        editMenu.add(redoItem);

        addSeparator();

        this.menuBarToolBar = new JToolBar();
        this.newFileButton = CreateImageButton.createButton("assets/icons/file-image-regular.png", "Neues Dokument erstellen");
        this.openFileButton = CreateImageButton.createButton("assets/icons/folder-open-regular.png", "Datei öffnen");
        this.saveFileButton = CreateImageButton.createButton("assets/icons/floppy-disk-regular.png", "Speichern");
        this.printDocumentButton = CreateImageButton.createButton("assets/icons/print-solid.png", "Drucken");
        this.undoButton = CreateImageButton.createButton("assets/icons/rotate-left-solid.png", "Rückgängig");
        this.redoButton = CreateImageButton.createButton("assets/icons/rotate-right-solid.png", "Wiederherstellen");


        add(menuBarToolBar);
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
    }

    // Getter methods to allow access to menu items and components

    // File menu getter
    public JMenu getFileMenu() { return fileMenu; }
    public JMenuItem getOpenFileItem() { return openFileItem; }
    public JMenuItem getNewFileItem() { return newFileItem; }
    public JMenuItem getSaveFileItem() { return saveFileItem; }
    public JMenuItem getSaveFileAsItem() { return saveFileAsItem; }
    public JMenuItem getPrintDocumentItem() { return printDocumentItem; }

    // Edit menu getter
    public JMenu getEditMenu() { return editMenu; }
    public JMenuItem getUndoItem() { return undoItem; }
    public JMenuItem getRedoItem() {  return redoItem; }

    // ToolBar getter
    public JButton getNewFileButton() { return newFileButton; }
    public JButton getOpenFileButton() { return openFileButton; }
    public JButton getSaveFileButton() { return saveFileButton; }
    public JButton getPrintDocumentButton() { return printDocumentButton; }
    public JButton getUndoButton() { return undoButton; }
    public JButton getRedoButton() { return redoButton; }

    private void addSeparator() {
        add(Box.createHorizontalStrut(10)); // Adds spacing
        JSeparator separator = new JSeparator(SwingConstants.VERTICAL);
        separator.setPreferredSize(new Dimension(2, 15)); // Set width & height
        add(separator);
        add(Box.createHorizontalStrut(10)); // Adds spacing
    }
}
