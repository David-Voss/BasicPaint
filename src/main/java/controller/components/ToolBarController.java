package controller.components;

import controller.MainController;
import view.MainWindow;
import view.components.PaintingPanelView;
import view.components.ToolBarView;
import model.PaintingModel;

public class ToolBarController {
    private final MainWindow mainWindow;
    private final MainController mainController;
    private final ToolBarView toolBarView;
    private final PaintingPanelView paintingPanelView;
    private final PaintingModel paintingModel;

    public ToolBarController(MainWindow mainWindow, MainController mainController) {
        this.mainWindow = mainWindow;
        this.mainController = mainController;
        this.toolBarView = mainWindow.getToolBarView();
        this.paintingPanelView = mainWindow.getPaintingPanelView();
        this.paintingModel = paintingPanelView.getPaintingModel();
    }

    public ToolBarView getToolBarView() {
        return toolBarView;
    }
}
