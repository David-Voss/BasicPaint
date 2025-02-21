package controller.components;

import model.PaintingModel;
import view.components.PaintingPanelView;
import view.components.ToolBarView;

import javax.swing.*;
import java.awt.*;

public class PaintingPanelController {
    private PaintingPanelView paintingView;
    private PaintingModel paintingModel;
    private final ToolBarView toolBarView;

    public PaintingPanelController(PaintingPanelView paintingView, ToolBarView toolBarView) {
        this.paintingView = paintingView;
        this.paintingModel = paintingView.getPaintingModel();
        this.toolBarView = toolBarView;


        toolBarView.getColourChooser().getSelectionModel().addChangeListener(e -> changeColour());
        toolBarView.getRectangleButton().addActionListener(e -> activateEraser());
    }

    private void changeColour() {
        //Color newColour = JColorChooser.showDialog(toolBarView, "Choose Colour", paintingModel.getCanvas().getGraphics().getColor());
        Color newColour = toolBarView.getColour();
        if (newColour != null) {
            paintingModel.setColor(newColour);
        }
    }

    private void activateEraser() {
        paintingModel.setColor(Color.WHITE);
    }

    public PaintingModel getPaintingModel() {
        return paintingModel;
    }
}
