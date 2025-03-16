package toolbox;

import model.PaintingModel;
import view.MainWindow;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Manages the undo and redo functionality for the painting application.
 */
public class UndoRedoManager {
    private final Deque<CanvasState> undoStack = new ArrayDeque<>();
    private final Deque<CanvasState> redoStack = new ArrayDeque<>();
    private final PaintingModel paintingModel;
    private final MainWindow mainWindow;
    private File currentFile;

    /**
     * Constructs the UndoRedoManager.
     *
     * @param paintingModel The painting model managing the canvas.
     * @param mainWindow    The main window of the application.
     */
    public UndoRedoManager(PaintingModel paintingModel, MainWindow mainWindow) {
        this.paintingModel = paintingModel;
        this.mainWindow = mainWindow;

        updateUndoRedoState();
    }

    /**
     * Saves the current canvas state for undo functionality.
     */
    public void saveCanvasState() {
        BufferedImage currentState = copyImage(paintingModel.getCanvas());
        if (currentState == null || (!undoStack.isEmpty() && imagesAreEqual(undoStack.peek().image, currentState))) {
            return; // No need to save duplicate states
        }
        undoStack.push(new CanvasState(currentState, getCurrentFileName()));
        redoStack.clear(); // Redo becomes invalid
        updateUndoRedoState();
    }

    /**
     * Clears the entire undo/redo history.
     */
    public void clearHistory() {
        undoStack.clear();
        redoStack.clear();
        updateUndoRedoState();
    }

    /**
     * Restores the previous canvas state (undo operation).
     */
    public void undo() {
        if (!undoStack.isEmpty()) {
            redoStack.push(createCanvasState());
            applyCanvasState(undoStack.pop());
            updateUndoRedoState();
        }
    }

    /**
     * Restores the next canvas state (redo operation).
     */
    public void redo() {
        if (!redoStack.isEmpty()) {
            undoStack.push(createCanvasState());
            applyCanvasState(redoStack.pop());
            updateUndoRedoState();
        }
    }

    /**
     * Applies a saved canvas state to the painting model.
     *
     * @param state The canvas state to apply.
     */
    private void applyCanvasState(CanvasState state) {
        paintingModel.setCanvas(state.image);
        currentFile = new File(state.fileName);
        mainWindow.setTitle("BasicPaint | " + state.fileName);
        mainWindow.getPaintingPanelView().repaint();
    }

    /**
     * Creates a snapshot of the current canvas state.
     *
     * @return A CanvasState object containing the current canvas information.
     */
    private CanvasState createCanvasState() {
        return new CanvasState(copyImage(paintingModel.getCanvas()), getCurrentFileName());
    }

    /**
     * Checks if two images are identical.
     *
     * @param img1 The first image.
     * @param img2 The second image.
     * @return {@code true} if both images are identical, otherwise {@code false}.
     */
    private boolean imagesAreEqual(BufferedImage img1, BufferedImage img2) {
        if (img1 == null || img2 == null || img1.getWidth() != img2.getWidth() || img1.getHeight() != img2.getHeight()) {
            return false;
        }
        return img1.getData().equals(img2.getData()); // Efficient pixel comparison
    }

    /**
     * Creates a deep copy of an image.
     *
     * @param image The image to copy.
     * @return A copied BufferedImage or {@code null} if the input is invalid.
     */
    private BufferedImage copyImage(BufferedImage image) {
        if (image == null) return null;
        BufferedImage copy = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = copy.createGraphics();
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();
        return copy;
    }

    /**
     * Retrieves the current file name of the canvas.
     *
     * @return The file name or "Unbenannt" if no file is set.
     */
    private String getCurrentFileName() {
        return (currentFile != null) ? currentFile.getName() : "Unbenannt";
    }

    /**
     * Updates the undo/redo state in the UI.
     */
    private void updateUndoRedoState() {
        boolean canUndo = !undoStack.isEmpty();
        boolean canRedo = !redoStack.isEmpty();

        mainWindow.getMenuBarView().getUndoItem().setEnabled(canUndo);
        mainWindow.getMenuBarView().getUndoButton().setEnabled(canUndo);

        mainWindow.getMenuBarView().getRedoItem().setEnabled(canRedo);
        mainWindow.getMenuBarView().getRedoButton().setEnabled(canRedo);
    }

    /**
     * Represents a snapshot of the canvas state for undo/redo operations.
     */
    private static final class CanvasState {
        private final BufferedImage image;
        private final String fileName;

        /**
         * Constructs a CanvasState object.
         *
         * @param image    The saved image state.
         * @param fileName The name of the file associated with this state.
         */
        public CanvasState(BufferedImage image, String fileName) {
            this.image = image;
            this.fileName = fileName;
        }
    }
}