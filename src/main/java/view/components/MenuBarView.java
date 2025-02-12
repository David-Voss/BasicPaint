package view.components;

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
        this.newFileButton = createButton("assets/icons/file-regular.png", "Neues Dokument erstellen");
        this.openFileButton = createButton("assets/icons/folder-open-regular.png", "Datei öffnen");
        this.saveFileButton = createButton("assets/icons/floppy-disk-regular.png", "Speichern");
        this.printDocumentButton = createButton("assets/icons/print-solid.png", "Drucken");
        this.undoButton = createButton("assets/icons/rotate-left-solid.png", "Rückgängig");
        this.redoButton = createButton("assets/icons/rotate-right-solid.png", "Wiederherstellen");


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
    public JMenuItem getOpenFileItem() { return openFileItem; }
    public JMenuItem getNewFileItem() { return newFileItem; }
    public JMenuItem getSaveFileItem() { return saveFileItem; }
    public JMenuItem getSaveFileAsItem() { return saveFileAsItem; }
    public JMenuItem getPrintDocumentItem() { return printDocumentItem; }

    // Edit menu getter
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

    /**
     * Creates a button with an icon and tooltip.
     *
     * @param iconPath The path to the icon file.
     * @param tooltip  The tooltip text for the button.
     * @return The created JButton.
     */
    private JButton createButton(String iconPath, String tooltip) {
        JButton button = new JButton(loadIcon(iconPath, 15, 15));
        button.setToolTipText(tooltip);
        return button;
    }

    /**
     * Loads and scales an icon to the specified dimensions.
     *
     * @param path  The file path of the icon.
     * @param width The desired width.
     * @param height The desired height.
     * @return A scaled {@link ImageIcon}.
     */
    private ImageIcon loadIcon(String path, int width, int height) {
        ImageIcon icon = new ImageIcon(path);
        Image scaledImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }
}
