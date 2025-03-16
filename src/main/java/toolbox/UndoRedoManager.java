package toolbox;

import model.PaintingModel;
import view.MainWindow;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayDeque;
import java.util.Deque;

public class UndoRedoManager {
    private final Deque<CanvasState> undoStack = new ArrayDeque<>();
    private final Deque<CanvasState> redoStack = new ArrayDeque<>();
    private final PaintingModel paintingModel;
    private final MainWindow mainWindow;
    private File currentFile;

    public UndoRedoManager(PaintingModel paintingModel, MainWindow mainWindow) {
        this.paintingModel = paintingModel;
        this.mainWindow = mainWindow;
    }

    public void saveCanvasState() {
        BufferedImage currentState = copyImage(paintingModel.getCanvas());
        if (currentState == null || (!undoStack.isEmpty() && imagesAreEqual(undoStack.peek().image, currentState))) {
            return; // Keine doppelte Speicherung nötig
        }
        undoStack.push(new CanvasState(currentState, paintingModel.getCanvas().getWidth(), paintingModel.getCanvas().getHeight(), getCurrentFileName()));
        redoStack.clear(); // Redo wird ungültig
        updateUndoRedoState();
    }

    public void clearHistory() {
        undoStack.clear();
        redoStack.clear();
        updateUndoRedoState();
    }


    public void undo() {
        if (!undoStack.isEmpty()) {
            redoStack.push(createCanvasState());
            applyCanvasState(undoStack.pop());
            updateUndoRedoState();
        }
    }

    public void redo() {
        if (!redoStack.isEmpty()) {
            undoStack.push(createCanvasState());
            applyCanvasState(redoStack.pop());
            updateUndoRedoState();
        }
    }

    private void applyCanvasState(CanvasState state) {
        paintingModel.setCanvas(state.image);
        currentFile = new File(state.fileName);
        mainWindow.setTitle("BasicPaint | " + state.fileName);
        mainWindow.getPaintingPanelView().repaint();
    }

    private CanvasState createCanvasState() {
        return new CanvasState(copyImage(paintingModel.getCanvas()), paintingModel.getCanvas().getWidth(), paintingModel.getCanvas().getHeight(), getCurrentFileName());
    }

    private boolean imagesAreEqual(BufferedImage img1, BufferedImage img2) {
        return img1.getData().getDataBuffer().equals(img2.getData().getDataBuffer());
    }

    private BufferedImage copyImage(BufferedImage image) {
        if (image == null) return null;
        BufferedImage copy = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        copy.getGraphics().drawImage(image, 0, 0, null);
        return copy;
    }

    private String getCurrentFileName() {
        return (currentFile != null) ? currentFile.getName() : "Unbenannt";
    }

    private void updateUndoRedoState() {
        boolean canUndo = !undoStack.isEmpty();
        boolean canRedo = !redoStack.isEmpty();

        mainWindow.getMenuBarView().getUndoItem().setEnabled(canUndo);
        mainWindow.getMenuBarView().getUndoButton().setEnabled(canUndo);

        mainWindow.getMenuBarView().getRedoItem().setEnabled(canRedo);
        mainWindow.getMenuBarView().getRedoButton().setEnabled(canRedo);
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
