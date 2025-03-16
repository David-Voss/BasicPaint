package controller.components;

import controller.MainController;
import model.PaintingModel;
import toolbox.*;
import view.MainWindow;
import view.components.MenuBarView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class MenuBarController implements ActionListener {
    private MainWindow mainWindow;
    private MainController mainController;
    private MenuBarView menuBar;
    private PaintingModel paintingModel;

    private FileHandler fileHandler;
    private boolean hasUnsavedChanges;

    private DiscardChangesHandler discardChangesHandler;

    private PrintService printService;

    private File currentFile = null;
    private JFileChooser fileChooser;

    private UndoRedoManager undoRedoManager;

    /*private Stack<CanvasState> undoStack = new Stack<>();
    private Stack<CanvasState> redoStack = new Stack<>();*/



    private final Map<String, Runnable> actionMap = new HashMap<>();

    public MenuBarController(MainWindow mainWindow, MainController mainController) {
        this.mainWindow = mainWindow;
        this.mainController = mainController;
        this.menuBar = mainWindow.getMenuBarView();
        this.paintingModel = mainWindow.getPaintingPanelView().getPaintingModel();

        this.fileHandler = new FileHandler(paintingModel, mainWindow);
        this.hasUnsavedChanges = false;
        this.fileHandler.setOnFileSavedCallback(() -> hasUnsavedChanges = false);

        this.fileChooser = new JFileChooser();
        FileChooserConfigurator.configureFileChooser(this.fileChooser);

        this.discardChangesHandler = new DiscardChangesHandler(mainWindow);

        this.printService = new PrintService();

        initActionMap();
        initMenuBarFunctions();
        this.undoRedoManager = new UndoRedoManager(paintingModel, mainWindow);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Runnable action = actionMap.get(e.getActionCommand());
        if (action != null) {
            LoggingHelper.log(e.getActionCommand() + "() aufgerufen.");
            action.run();
        }
    }

    public boolean confirmDiscardChanges() {
        return discardChangesHandler.confirmDiscardChanges(hasUnsavedChanges, this::saveFile);
    }

    private void initActionMap() {
        actionMap.put("new", this::newFile);
        actionMap.put("open", this::openFile);
        actionMap.put("save", this::saveFile);
        actionMap.put("save_as", this::saveFileAs);
        actionMap.put("print", this::printPicture);
        actionMap.put("image_properties", this::showImagePropertiesDialog);
        actionMap.put("undo", this::undo);
        actionMap.put("redo", this::redo);
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
                    LoggingHelper.log("Zeichenfläche Bearbeitet. \n" +
                            DateTimeStamp.time() + ": Bild hat ungespeicherte Änderungen. \n");
                    hasUnsavedChanges = true;
                }
            }
        });
    }

    public void newFile() {
        saveCanvasState();

        if (confirmDiscardChanges()) {
            fileHandler.newFile();
            hasUnsavedChanges = false;
            currentFile = null;
            mainWindow.setTitle("BasicPaint | Unbenannt");

            undoRedoManager.clearHistory();

            SwingUtilities.invokeLater(() -> mainWindow.getPaintingPanelView().repaint());
        }
    }

    public void openFile() {
        saveCanvasState();

        if (confirmDiscardChanges()) {
            BufferedImage image = fileHandler.openFile();
            if (image != null) {
                mainWindow.getPaintingPanelView().getPaintingModel().setCanvas(image);
                mainController.getPaintingPanelController()
                        .resizePanelWhenOpenedFileIsWiderOrHigher(image.getWidth(), image.getHeight());

                hasUnsavedChanges = false;
                undoRedoManager.clearHistory();

                File openedFile = fileHandler.getCurrentFile();
                if (openedFile != null) {
                    this.currentFile = openedFile;
                    mainWindow.setTitle("BasicPaint | " + openedFile.getName());
                }

                SwingUtilities.invokeLater(() -> mainWindow.getPaintingPanelView().repaint());
            }
        }
    }

    public boolean saveFile() {
        return fileHandler.saveFile();
    }

    public void saveFileAs() {
        fileHandler.saveFileAs();
    }

    public void printPicture() {
        printService.printPicture(mainWindow, paintingModel.getCanvas());
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
            LoggingHelper.log("Neue Größe gesetzt: " + newWidth + "x" + newHeight + "\n");
        }
    }

    private void undo() {
        undoRedoManager.undo();
        System.out.print("\n");
    }

    private void redo() {
        undoRedoManager.redo();
        System.out.print("\n");
    }

    private void saveCanvasState() {
        undoRedoManager.saveCanvasState();
    }
}
