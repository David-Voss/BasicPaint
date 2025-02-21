package controller.components;

import model.PaintingModel;
import model.elements.LineElement;
import view.components.PaintingPanelView;
import view.components.ToolBarView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PaintingPanelController {
    private PaintingPanelView paintingView;
    private PaintingModel paintingModel;
    private final ToolBarView toolBarView;

    public PaintingPanelController(PaintingPanelView paintingView, ToolBarView toolBarView) {
        this.paintingView = paintingView;
        this.paintingModel = paintingView.getPaintingModel();
        this.toolBarView = toolBarView;


        toolBarView.getColorPickerButton().addActionListener(e -> changeColour());
        toolBarView.getRectangleButton().addActionListener(e -> activateEraser());
    }

    private void changeColour() {
        Color newColour = JColorChooser.showDialog(paintingView, "Choose Colour", paintingModel.getCanvas().getGraphics().getColor());
        if (newColour != null) {
            paintingModel.setColor(newColour);
        }
    }

    private void activateEraser() {
        paintingModel.setColor(Color.WHITE);
    }

    /*public void setCurrentColor(Color color) {
        this.currentColor = color;
    }

    public void setCurrentStrokeWidth(float strokeWidth) {
        this.currentStrokeWidth = strokeWidth;
    }*/

    public PaintingModel getPaintingModel() {
        return paintingModel;
    }
}
