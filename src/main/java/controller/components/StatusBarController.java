package controller.components;

import controller.MainController;
import view.MainWindow;
import view.components.PaintingPanelView;
import view.components.StatusBarView;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class StatusBarController {
    MainWindow mainWindow;
    MainController mainController;
    StatusBarView statusBar;
    PaintingPanelView paintingPanel;

    public StatusBarController(MainWindow mainWindow, MainController mainController) {
        this.mainWindow = mainWindow;
        this.mainController = mainController;
        this.statusBar = mainWindow.getStatusBarView();
        this.paintingPanel = mainWindow.getPaintingPanelView();

        updateImageSize(mainWindow.getPaintingPanelView().getPaintingModel().getCanvas().getWidth(), mainWindow.getPaintingPanelView().getPaintingModel().getCanvas().getHeight());

        initialiseListeners();
    }

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
     * Aktualisiert die Mausposition im StatusBarView.
     */
    public void updateMousePosition(int x, int y) {
        statusBar.getMousePositionLabel().setText("X: " + x + ", Y: " + y);
    }

    /**
     * Aktualisiert die Bildgröße in der Statusleiste.
     */
    public void updateImageSize(int width, int height) {
        statusBar.getImageSizeLabel().setText(width + " × " + height + " px");
    }
}
