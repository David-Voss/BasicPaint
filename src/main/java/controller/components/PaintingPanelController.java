package controller.components;

import model.PaintingModel;
import model.elements.RectangleElement;
import view.components.PaintingPanelView;
import view.components.ToolBarView;

import javax.swing.*;
import java.awt.*;

import static sun.swing.SwingUtilities2.drawRect;

public class PaintingPanelController {
    private PaintingPanelView paintingView;
    private PaintingModel paintingModel;
    private final ToolBarView toolBarView;



    public PaintingPanelController(PaintingPanelView paintingView, ToolBarView toolBarView) {
        this.paintingView = paintingView;
        this.paintingModel = paintingView.getPaintingModel();
        this.toolBarView = toolBarView;

        initialiseListeners();
    }

    private void initialiseListeners() {
        toolBarView.getColourChooser().getSelectionModel().addChangeListener(e -> changeColour());
        toolBarView.getEraserButton().addActionListener(e -> activateEraser());
        toolBarView.getRectangleButton().addActionListener(e -> paintingModel.getG2d().drawRect(50,50,100,100));
    }

    private void changeColour() {
        disableEraserIfActive();
        Color newColour = toolBarView.getColour();
        if (newColour != null) {
            paintingModel.setCurrentColour(newColour);
        }
    }

    private void disableEraserIfActive() {
        if (toolBarView.getEraserButton().isSelected()) {
            toolBarView.getEraserButton().setSelected(false);
        }
    }

    private void activateEraser() {
        if (toolBarView.getEraserButton().isSelected()) {
            paintingModel.setCurrentColour(Color.WHITE);
        } else {
            paintingModel.colourChangeBack();
        }
    }

    public PaintingModel getPaintingModel() {
        return paintingModel;
    }
}
