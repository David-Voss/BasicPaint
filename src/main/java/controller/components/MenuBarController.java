package controller.components;

import controller.MainController;
import model.PaintingModel;
import toolbox.*;
import view.MainWindow;
import view.components.MenuBarView;

import javax.swing.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller for managing interactions with the menu bar.
 * Handles file operations, undo/redo functionalities, and user actions from the menu.
 */
public class MenuBarController implements ActionListener {
    private MainWindow mainWindow;
    private MainController mainController;
    private MenuBarView menuBar;
    private PaintingModel paintingModel;

    private FileHandler fileHandler;
    private boolean hasUnsavedChanges;
    private DiscardChangesHandler discardChangesHandler;
    private PrintService printService;
    private UndoRedoManager undoRedoManager;

    private File currentFile = null;
    private JFileChooser fileChooser;

    private final Map<String, Runnable> actionMap = new HashMap<>();

    /**
     * Constructs the MenuBarController and initialises all menu bar functions.
     * @param mainWindow The main application window.
     * @param mainController The main application controller.
     */
    public MenuBarController(MainWindow mainWindow, MainController mainController) {
        this.mainWindow = mainWindow;
        this.mainController = mainController;
        this.menuBar = mainWindow.getMenuBarView();
        this.paintingModel = mainWindow.getPaintingPanelView().getPaintingModel();

        initActionMap();
        initMenuBarFunctions();
    }

    /**
     * Handles action events triggered by menu items and buttons.
     * Executes the corresponding function mapped to the action command.
     *
     * @param e The action event containing the command.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Runnable action = actionMap.get(e.getActionCommand());
        if (action != null) {
            LoggingHelper.log(e.getActionCommand() + "() aufgerufen.");
            action.run();
        }
    }

    /**
     * Confirms whether unsaved changes should be discarded.
     * @return true if the user confirms discarding changes, false otherwise.
     */
    public boolean confirmDiscardChanges() {
        return discardChangesHandler.confirmDiscardChanges(hasUnsavedChanges, this::saveFile);
    }

    /**
     * Initialises a mapping between action commands and their corresponding methods.
     */
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

    /**
     * Registers and initialises all menu bar functions including shortcuts and event listeners.
     */
    private void initMenuBarFunctions() {
        initShortcuts();
        registerFileMenuActions();
        registerEditMenuActions();
        registerMenuBarToolBarActions();
        registerCanvasInteractionListener();

        setUpFileHandler();
        setUpFileChooser();

        this.discardChangesHandler = new DiscardChangesHandler(mainWindow);
        this.printService = new PrintService();
        this.undoRedoManager = new UndoRedoManager(paintingModel, mainWindow);
    }

    /**
     * Sets up keyboard shortcuts for the menu items.
     */
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

    /**
     * Registers action listeners for the File menu items.
     * Associates menu items with corresponding commands.
     */
    private void registerFileMenuActions() {
        addMenuAction(menuBar.getNewFileItem(), "new");
        addMenuAction(menuBar.getOpenFileItem(), "open");
        addMenuAction(menuBar.getSaveFileItem(), "save");
        addMenuAction(menuBar.getSaveFileAsItem(), "save_as");
        addMenuAction(menuBar.getPrintDocumentItem(), "print");
        addMenuAction(menuBar.getImageProperties(), "image_properties");
    }

    /**
     * Registers action listeners for the Edit menu items.
     * Associates menu items with undo and redo commands.
     */
    private void registerEditMenuActions() {
        addMenuAction(menuBar.getUndoItem(), "undo");
        addMenuAction(menuBar.getRedoItem(), "redo");
    }

    /**
     * Registers action listeners for toolbar buttons in the menu bar.
     * Associates buttons with their respective file and edit operations.
     */
    private void registerMenuBarToolBarActions() {
        addMenuAction(menuBar.getNewFileButton(), "new");
        addMenuAction(menuBar.getOpenFileButton(), "open");
        addMenuAction(menuBar.getSaveFileButton(), "save");
        addMenuAction(menuBar.getPrintDocumentButton(), "print");
        addMenuAction(menuBar.getUndoButton(), "undo");
        addMenuAction(menuBar.getRedoButton(), "redo");
    }

    /**
     * Registers a listener to detect user interactions with the painting canvas.
     * Saves the canvas state before any modification and logs unsaved changes.
     */
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

    /**
     * Initialises the file handler, responsible for file-related operations.
     * Sets up a callback to track unsaved changes when a file is saved.
     */
    private void setUpFileHandler() {
        this.fileHandler = new FileHandler(paintingModel, mainWindow);
        this.hasUnsavedChanges = false;
        this.fileHandler.setOnSaveFileCallback(() -> hasUnsavedChanges = false);
    }

    /**
     * Configures the file chooser dialog for opening and saving files.
     * Ensures appropriate file filters and settings are applied.
     */
    private void setUpFileChooser() {
        this.fileChooser = new JFileChooser();
        FileChooserConfigurator.configureFileChooser(this.fileChooser);
    }

    /**
     * Handles opening a new file, ensuring unsaved changes are managed appropriately.
     */
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

    /**
     * Handles opening an existing file and loads it onto the canvas.
     */
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

    /**
     * Saves the current file.
     * @return true if the file was successfully saved, false otherwise.
     */
    public boolean saveFile() {
        return fileHandler.saveFile();
    }

    /**
     * Saves the file with a new user specified name/location.
     */
    public void saveFileAs() {
        fileHandler.saveFileAs();
    }

    /**
     * Prints the current canvas content.
     */
    public void printPicture() {
        printService.printPicture(mainWindow, paintingModel.getCanvas());
    }

    /**
     * Displays the image properties dialog.
     * Allows the user to view and modify the image dimensions.
     */
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

    /**
     * Performs an undo operation.
     * Reverts the canvas to the previous state.
     */
    private void undo() {
        undoRedoManager.undo();
        System.out.print("\n");
    }

    /**
     * Performs a redo operation.
     * Restores the previously undone state.
     */
    private void redo() {
        undoRedoManager.redo();
        System.out.print("\n");
    }

    /**
     * Saves the current state of the canvas.
     */
    private void saveCanvasState() {
        undoRedoManager.saveCanvasState();
    }
}
