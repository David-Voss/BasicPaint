package controller.components;

import controller.MainController;
import model.PaintingModel;
import toolbox.FileChooserConfigurator;
import toolbox.FileHandler;
import toolbox.DateTimeStamp;
import view.MainWindow;
import view.components.MenuBarView;
import view.components.PaintingPanelView;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import java.util.Stack;

public class MenuBarController implements ActionListener {
    private MainWindow mainWindow;
    private MainController mainController;
    private MenuBarView menuBar;
    private PaintingModel paintingModel;

    private FileHandler fileHandler;
    private boolean hasUnsavedChanges;

    private File currentFile = null;
    private JFileChooser fileChooser;

    private Stack<CanvasState> undoStack = new Stack<>();
    private Stack<CanvasState> redoStack = new Stack<>();

    public MenuBarController(MainWindow mainWindow, MainController mainController, FileHandler fileHandler) {
        this.mainWindow = mainWindow;
        this.mainController = mainController;
        this.menuBar = mainWindow.getMenuBarView();
        this.paintingModel = mainWindow.getPaintingPanelView().getPaintingModel();

        this.fileHandler = fileHandler;
        this.hasUnsavedChanges = false;

        this.fileChooser = new JFileChooser();
        FileChooserConfigurator.configureFileChooser(this.fileChooser);

        initMenuBarFunctions();
        registerCanvasInteractionListener();
        updateUndoRedoState();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();

        switch (actionCommand) {
            // 'File' menu actions
            case "new":
                System.out.println(timeStamp() + ": newFile() aufgerufen.");
                newFile();
                break;
            case "open":
                System.out.println(timeStamp() + ": openFile() aufgerufen.");
                openFile();
                break;
            case "save":
                System.out.println(timeStamp() + ": saveFile() aufgerufen.");
                saveFile();
                break;
            case "save_as":
                System.out.println(timeStamp() + ": saveFileAs() aufgerufen.");
                saveFileAs();
                break;
            case "print":
                System.out.println(timeStamp() + ": printPicture() aufgerufen.");
                printPicture();
                break;
            case "image_properties":
                System.out.println(timeStamp() + ": showImagePropertiesDialog() aufgerufen.");
                showImagePropertiesDialog();
                break;
            // 'Edit' menu actions
            case "undo":
                System.out.println("timeStamp() + \": undo() aufgerufen. \n");
                undo();
                break;
            case "redo":
                System.out.println("timeStamp() + \": redo() aufgerufen. \n");
                redo();
                break;
            default:
                break;
        }
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

    private void initMenuBarFunctions() {
        initShortcuts();
        registerFileMenuActions();
        registerEditMenuActions();
        registerMenuBarToolBarActions();
        registerCanvasInteractionListener();
    }

    private void initShortcuts() {
        // Shortcuts 'File' menu
        menuBar.getFileMenu().setMnemonic(KeyEvent.VK_D);
        menuBar.getNewFileItem().setAccelerator(KeyStroke.getKeyStroke('N', InputEvent.CTRL_DOWN_MASK));
        menuBar.getOpenFileItem().setAccelerator(KeyStroke.getKeyStroke('O', InputEvent.CTRL_DOWN_MASK));
        menuBar.getSaveFileItem().setAccelerator(KeyStroke.getKeyStroke('S', InputEvent.CTRL_DOWN_MASK));
        menuBar.getSaveFileAsItem().setAccelerator(KeyStroke.getKeyStroke('S', InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
        menuBar.getPrintDocumentItem().setAccelerator(KeyStroke.getKeyStroke('P', InputEvent.CTRL_DOWN_MASK));
        menuBar.getImageProperties().setAccelerator(KeyStroke.getKeyStroke('E', InputEvent.CTRL_DOWN_MASK));

        // Shortcuts 'Edit' menu
        menuBar.getEditMenu().setMnemonic(KeyEvent.VK_B);
        menuBar.getUndoItem().setAccelerator(KeyStroke.getKeyStroke('Z', InputEvent.CTRL_DOWN_MASK));
        menuBar.getRedoItem().setAccelerator(KeyStroke.getKeyStroke('Z', InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
    }

    private void registerFileMenuActions() {
        addMenuAction(menuBar.getNewFileItem(), "new");
        addMenuAction(menuBar.getOpenFileItem(), "open");
        addMenuAction(menuBar.getSaveFileItem(), "save");
        addMenuAction(menuBar.getSaveFileAsItem(), "save_as");
        addMenuAction(menuBar.getPrintDocumentItem(), "print");
        addMenuAction(menuBar.getImageProperties(), "image_properties");
    }

    private void registerEditMenuActions() {
        addMenuAction(menuBar.getUndoItem(), "undo");
        addMenuAction(menuBar.getRedoItem(), "redo");
    }

    private void registerMenuBarToolBarActions() {
        addMenuAction(menuBar.getNewFileButton(), "new");
        addMenuAction(menuBar.getOpenFileButton(), "open");
        addMenuAction(menuBar.getSaveFileButton(), "save");
        addMenuAction(menuBar.getPrintDocumentButton(), "print");
        addMenuAction(menuBar.getUndoButton(), "undo");
        addMenuAction(menuBar.getRedoButton(), "redo");
    }

    private void registerCanvasInteractionListener() {
        mainWindow.getPaintingPanelView().addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                saveCanvasState();

                if (!hasUnsavedChanges) {
                    System.out.println(timeStamp() + ": Zeichenfläche Bearbeitet. \n" +
                            timeStamp() + ": Bild hat ungespeicherte Änderungen. \n");
                    hasUnsavedChanges = true;
                }
            }
        });
    }

    private void newFile() {
        saveCanvasState();

        if (confirmDiscardChanges()) {
            paintingModel.clearCanvas();
            fileHandler.resetFile();
            hasUnsavedChanges = false;
            currentFile = null;
            mainWindow.setTitle("BasicPaint");

            // Undo- und Redo-Stack leeren
            undoStack.clear();
            redoStack.clear();
            updateUndoRedoState();

            // **Sofortiges Neuzeichnen erzwingen**
            SwingUtilities.invokeLater(() -> mainWindow.getPaintingPanelView().repaint());

            System.out.println(timeStamp() + ": Neues Bild erstellt. \n" +
                    timeStamp() + ": Bild hat keine ungespeicherten Änderungen. \n" +
                    timeStamp() + ": Neuer Bildtitel: " + mainWindow.getTitle() + "\n");
        }
    }


    /**
     * Opens an image file (JPG) into the drawing panel. Prompts to save current work if unsaved.
     */
    private void openFile() {
        saveCanvasState();

        if (confirmDiscardChanges()) {
            BufferedImage image = fileHandler.openImage(mainWindow);
            if (image != null) {
                PaintingPanelView paintingPanel = mainWindow.getPaintingPanelView();
                PaintingModel paintingModel = paintingPanel.getPaintingModel();

                // Setze das geladene Bild auf das Canvas
                paintingModel.setCanvas(image);

                // Ändere die Größe des PaintingPanels auf die Bildgröße
                mainController.getPaintingPanelController().resizePanelWhenOpenedFileIsWiderOrHigher(image.getWidth(), image.getHeight());

                paintingPanel.revalidate();
                paintingPanel.repaint();
                hasUnsavedChanges = false;

                // Undo- und Redo-Stack leeren
                undoStack.clear();
                redoStack.clear();
                updateUndoRedoState();

                // Update frame title to show opened file name.
                File openedFile = fileHandler.getFile();
                if (openedFile != null) {
                    this.currentFile = openedFile;
                    mainWindow.setTitle("BasicPaint | " + openedFile.getName());

                    System.out.println(timeStamp() + ": Datei geöffnet. \n" +
                            timeStamp() + ": currentFile = " + currentFile.getName() + ". \n" +
                            timeStamp() + ": Bild hat keine ungespeicherten Änderungen. \n");
                }
            } else if (currentFile == null){
                System.out.println(timeStamp() + ": Keine Datei gewählt. \n" +
                        timeStamp() + ": currentFile = null. \n");
            } else {
                System.out.println(timeStamp() + ": Keine Datei gewählt. \n" +
                        timeStamp() + ": currentFile = " + currentFile.getName() + ". \n");
            }
            int newWidth = mainWindow.getPaintingPanelView().getPaintingModel().getCanvas().getWidth();
            int newHeight = mainWindow.getPaintingPanelView().getPaintingModel().getCanvas().getHeight();
            mainController.getStatusBarController().updateImageSize(newWidth, newHeight);
        }
    }

    private boolean saveFile() {
        if (currentFile == null) {
            System.out.println(timeStamp() + ": currentFile = null -> Bild noch nicht gespeichert -> saveFileAs() wird aufgerufen.");
            saveFileAs();

            // Falls der Benutzer keine Datei ausgewählt hat, breche ab
            if (currentFile == null) {
                System.out.println(timeStamp() + ": Speichern abgebrochen. \n");
                return false;
            }
        } else {
            writeFile(currentFile);
        }
        return true;
    }

    private void saveFileAs() {
        JFileChooser fileChooser = new JFileChooser();
        FileChooserConfigurator.configureFileChooser(fileChooser);

        if (fileChooser.showSaveDialog(mainWindow) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String fileName = selectedFile.getName().toLowerCase();

            // 1️⃣ Standardmäßige Dateiendung bestimmen (basierend auf aktivem Filter)
            String selectedExtension = "jpg"; // Standard: JPG
            FileNameExtensionFilter selectedFilter = (FileNameExtensionFilter) fileChooser.getFileFilter();

            if (selectedFilter.getDescription().contains("PNG")) {
                selectedExtension = "png";
            }

            // 2️⃣ Falls keine Endung angegeben wurde, automatisch anhängen
            if (!fileName.endsWith(".jpg") && !fileName.endsWith(".jpeg") && !fileName.endsWith(".png")) {
                selectedFile = new File(selectedFile.getParentFile(), selectedFile.getName() + "." + selectedExtension);
            }

            this.currentFile = selectedFile;
            writeFile(currentFile);
        } else {
            System.out.println(timeStamp() + ": Datei wurde nicht ausgewählt.");
        }
    }

    private void writeFile(File file) {
        try {
            BufferedImage image = paintingModel.getCanvas();
            if (image == null) {
                System.out.println("Fehler: Das Bildobjekt ist null. Überprüfe paintingModel.getCanvas().");
                return;
            }

            if (file.exists() && !file.canWrite()) {
                System.out.println("Fehler: Die Datei ist schreibgeschützt oder von einem anderen Prozess gesperrt.");
                return;
            }

            // 1️⃣ Dateiformat bestimmen (JPG oder PNG)
            String format = "jpg"; // Standard ist JPG
            String fileName = file.getName().toLowerCase();
            if (fileName.endsWith(".png")) {
                format = "png";
            }

            // 2️⃣ Datei speichern mit dem richtigen Format
            boolean success = ImageIO.write(image, format, file);

            if (!success) {
                boolean secondTry = ImageIO.write(convertImage(image), format, file);

                if (!success && !secondTry) throw new IOException("ImageIO.write() konnte das Bild nicht speichern.");
            }

            mainWindow.setTitle("BasicPaint | " + file.getName());
            System.out.println(timeStamp() + ": Speichere Datei nach: " + file.getAbsolutePath());
            hasUnsavedChanges = false;
            System.out.println(timeStamp() + ": Änderungen gespeichert -> hasUnsavedChanges = false \n");

        } catch (IOException e) {
            System.out.print(timeStamp() + ": ");
            e.printStackTrace();
            JOptionPane.showMessageDialog(mainWindow,
                    "Fehler beim Speichern der Datei.\n" + e.getMessage(),
                    "Fehler",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private BufferedImage convertImage(BufferedImage image) {
        BufferedImage formattedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = formattedImage.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        System.out.println(timeStamp() + ": convertImage() wurde aufgerufen.");
        return formattedImage;
    }

    private void printPicture() {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPrintable((graphics, pageFormat, pageIndex) -> {
            if (pageIndex > 0) {
                return Printable.NO_SUCH_PAGE;
            }
            double scaleX = pageFormat.getImageableWidth() / paintingModel.getCanvas().getWidth();
            double scaleY = pageFormat.getImageableHeight() / paintingModel.getCanvas().getHeight();
            double scale = Math.min(scaleX, scaleY);
            Graphics2D g2d = (Graphics2D) graphics;
            g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
            g2d.scale(scale, scale);
            g2d.drawImage(paintingModel.getCanvas(), 0, 0, null);
            return Printable.PAGE_EXISTS;
        });
        if (job.printDialog()) {
            try {
                job.print();
            } catch (PrinterException e) {
                JOptionPane.showMessageDialog(mainWindow, "Fehler beim Drucken.", "Fehler", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void showImagePropertiesDialog() {
        PaintingModel paintingModel = mainWindow.getPaintingPanelView().getPaintingModel();
        int currentWidth = paintingModel.getCanvas().getWidth();
        int currentHeight = paintingModel.getCanvas().getHeight();

        ImagePropertiesController controller = new ImagePropertiesController(mainWindow, currentWidth, currentHeight, currentFile);
        controller.showDialog();

        if (controller.isConfirmed()) {
            saveCanvasState();

            int newWidth = controller.getImageWidth();
            int newHeight = controller.getImageHeight();

            mainController.getPaintingPanelController().setCanvasSize(newWidth, newHeight);
            System.out.println(timeStamp() + ": Neue Größe gesetzt: " + newWidth + "x" + newHeight + "\n");
        }
    }

    private void undo() {
        if (!undoStack.isEmpty()) {
            redoStack.push(new CanvasState(copyImage(paintingModel.getCanvas()),
                    paintingModel.getCanvas().getWidth(),
                    paintingModel.getCanvas().getHeight(),
                    (currentFile != null) ? currentFile.getName() : "Unbenannt"));

            CanvasState previousState = undoStack.pop();
            paintingModel.setCanvas(previousState.image);
            currentFile = new File(previousState.fileName);
            mainWindow.setTitle("BasicPaint | " + previousState.fileName);

            mainWindow.getPaintingPanelView().repaint();
            updateUndoRedoState();
        }
    }

    private void redo() {
        if (!redoStack.isEmpty()) {
            undoStack.push(new CanvasState(copyImage(paintingModel.getCanvas()),
                    paintingModel.getCanvas().getWidth(),
                    paintingModel.getCanvas().getHeight(),
                    (currentFile != null) ? currentFile.getName() : "Unbenannt"));

            CanvasState nextState = redoStack.pop();
            paintingModel.setCanvas(nextState.image);
            currentFile = new File(nextState.fileName);
            mainWindow.setTitle("BasicPaint | " + nextState.fileName);

            mainWindow.getPaintingPanelView().repaint();
            updateUndoRedoState();
        }
    }

    private void updateUndoRedoState() {
        boolean canUndo = !undoStack.isEmpty();
        boolean canRedo = !redoStack.isEmpty();

        menuBar.getUndoItem().setEnabled(canUndo);
        menuBar.getUndoButton().setEnabled(canUndo);

        menuBar.getRedoItem().setEnabled(canRedo);
        menuBar.getRedoButton().setEnabled(canRedo);
    }


    private void saveCanvasState() {
        BufferedImage currentState = copyImage(paintingModel.getCanvas());
        int currentWidth = paintingModel.getCanvas().getWidth();
        int currentHeight = paintingModel.getCanvas().getHeight();
        String currentFileName = (currentFile != null) ? currentFile.getName() : "Unbenannt";

        // Falls sich nichts geändert hat, speichern wir nicht doppelt
        if (!undoStack.isEmpty() && imagesAreEqual(undoStack.peek().image, currentState)
                && undoStack.peek().width == currentWidth
                && undoStack.peek().height == currentHeight) {
            return;
        }

        undoStack.push(new CanvasState(currentState, currentWidth, currentHeight, currentFileName));
        redoStack.clear(); // Redo wird ungültig, sobald eine neue Aktion passiert
        updateUndoRedoState();
    }


    private boolean imagesAreEqual(BufferedImage img1, BufferedImage img2) {
        if (img1.getWidth() != img2.getWidth() || img1.getHeight() != img2.getHeight()) {
            return false;
        }
        for (int y = 0; y < img1.getHeight(); y++) {
            for (int x = 0; x < img1.getWidth(); x++) {
                if (img1.getRGB(x, y) != img2.getRGB(x, y)) {
                    return false;
                }
            }
        }
        return true;
    }

    private BufferedImage copyImage(BufferedImage image) {
        if (image == null) return null;
        BufferedImage copy = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        Graphics2D g = copy.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return copy;
    }

    public boolean confirmDiscardChanges() {
        if (!hasUnsavedChanges) {
            return true; // Keine ungespeicherten Änderungen, kein Bestätigungsdialog notwendig.
        }

        // Erstelle eine Option-Pane mit den Buttons "Ja", "Nein" und "Abbrechen"
        String[] options = {"Ja", "Nein", "Abbrechen"};
        JOptionPane optionPane = new JOptionPane(
                "Die aktuelle Zeichnung wurde nicht gespeichert. Bild speichern?",
                JOptionPane.WARNING_MESSAGE,
                JOptionPane.YES_NO_CANCEL_OPTION,
                null,
                options,
                options[0]); // Standard ist "Ja"

        // Erstelle den Dialog
        JDialog dialog = optionPane.createDialog(mainWindow, "Ungespeicherte Änderungen");
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        // **Schließen mit X = "Abbrechen"**
        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                optionPane.setValue("Abbrechen");
                dialog.setVisible(false);
                dialog.dispose();
            }
        });

        // **ALLE Buttons holen**
        JButton yesButton = null, noButton = null, cancelButton = null;
        for (Component c : optionPane.getComponents()) {
            if (c instanceof JPanel) {
                for (Component btn : ((JPanel) c).getComponents()) {
                    if (btn instanceof JButton) {
                        JButton button = (JButton) btn;
                        if (button.getText().equals("Ja")) yesButton = button;
                        if (button.getText().equals("Nein")) noButton = button;
                        if (button.getText().equals("Abbrechen")) cancelButton = button;
                    }
                }
            }
        }

        // **ENTER- und ESC-KEY LISTENER HINZUFÜGEN**
        KeyAdapter keyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    Component focusOwner = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
                    if (focusOwner instanceof JButton) {
                        ((JButton) focusOwner).doClick(); // Nur Button mit Fokus drücken!
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    optionPane.setValue("Abbrechen");
                    dialog.setVisible(false);
                    dialog.dispose();
                }
            }
        };

        // **KeyListener für den gesamten Dialog setzen**
        dialog.addKeyListener(keyListener);
        dialog.setFocusable(true);
        dialog.requestFocusInWindow();

        // **KeyListener für alle Buttons setzen**
        if (yesButton != null) yesButton.addKeyListener(keyListener);
        if (noButton != null) noButton.addKeyListener(keyListener);
        if (cancelButton != null) cancelButton.addKeyListener(keyListener);

        // Dialog anzeigen
        dialog.setVisible(true);

        // Ergebnis ermitteln
        Object selectedValue = optionPane.getValue();

        if (selectedValue == null || selectedValue.equals("Abbrechen")) {
            System.out.println(timeStamp() + ": Abbrechen gewählt.");
            return false;
        }
        if (selectedValue.equals("Ja")) {
            System.out.println(timeStamp() + ": Ja gewählt -> saveFile() wird aufgerufen.");
            return saveFile(); // Falls speichern fehlschlägt, Abbruch
        }
        if (selectedValue.equals("Nein")) {
            System.out.println(timeStamp() + ": Nein gewählt -> Änderungen wurden nicht gespeichert.");
        }

        return true;
    }

    private String timeStamp() {
        return DateTimeStamp.time();
    }

    private static class CanvasState {
        BufferedImage image;
        int width;
        int height;
        String fileName;

        public CanvasState(BufferedImage image, int width, int height, String fileName) {
            this.image = image;
            this.width = width;
            this.height = height;
            this.fileName = fileName;
        }
    }
}
