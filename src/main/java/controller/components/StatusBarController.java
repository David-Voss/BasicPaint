package controller.components;

import controller.MainController;
import view.MainWindow;
import view.components.PaintingPanelView;
import view.components.StatusBarView;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

/**
 * Controls the status bar, updating the displayed mouse position and image size.
 */
public class StatusBarController {
    private final MainWindow mainWindow;
    private final MainController mainController;
    private final StatusBarView statusBar;
    private final PaintingPanelView paintingPanel;

    /**
     * Constructs a StatusBarController to manage status bar updates.
     *
     * @param mainWindow     The main application window.
     * @param mainController The main application controller.
     */
    public StatusBarController(MainWindow mainWindow, MainController mainController) {
        this.mainWindow = mainWindow;
        this.mainController = mainController;
        this.statusBar = mainWindow.getStatusBarView();
        this.paintingPanel = mainWindow.getPaintingPanelView();

        int canvasWidth = paintingPanel.getPaintingModel().getCanvas().getWidth();
        int canvasHeight = paintingPanel.getPaintingModel().getCanvas().getHeight();
        updateImageSize(canvasWidth, canvasHeight);

        initialiseListeners();
    }

    /**
     * Updates the displayed image size in the status bar.
     *
     * @param width  The width of the canvas in pixels.
     * @param height The height of the canvas in pixels.
     */
    public void updateImageSize(int width, int height) {
        statusBar.getImageSizeLabel().setText(width + " × " + height + " px");
    }

    /**
     * Initializes listeners for mouse motion events.
     */
    private void initialiseListeners() {
        paintingPanel.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                updateMousePosition(e.getX(), e.getY());
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                updateMousePosition(e.getX(), e.getY());
            }
        });;
    }

    /**
     * Updates the mouse position in the status bar.
     *
     * @param x The X-coordinate of the mouse pointer.
     * @param y The Y-coordinate of the mouse pointer.
     */
    private void updateMousePosition(int x, int y) {
        statusBar.getMousePositionLabel().setText("X: " + x + ", Y: " + y);
    }
}