package controller.components;

import model.PaintingModel;
import view.components.PaintingPanelView;
import view.components.ToolBarView;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PaintingPanelController {
    private PaintingPanelView paintingView;
    private PaintingModel paintingModel;
    private final ToolBarView toolBarView;

    private int x1, y1, x2, y2;

    public PaintingPanelController(PaintingPanelView paintingView, ToolBarView toolBarView) {
        this.paintingView = paintingView;
        this.paintingModel = paintingView.getPaintingModel();
        this.toolBarView = toolBarView;

        initialiseListeners();
    }

    public PaintingModel getPaintingModel() { return paintingModel; }

    private void initialiseListeners() {
        paintingView.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                x1 = e.getX();
                y1 = e.getY();
            }
        });
        paintingView.addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                if (isPaintingToolSelected()) {
                    x2 = e.getX();
                    y2 = e.getY();

                    drawLine(x1, y1, x2, y2);
                    x1 = x2;
                    y1 = y2;
                    paintingView.repaint();
                }
            }
        });

        toolBarView.getBrushSizeDropdown().addActionListener(e -> updateStrokeWidth());
        toolBarView.getPencilButton().addActionListener(e -> activatePencil());
        toolBarView.getEraserButton().addActionListener(e -> activateEraser());
        toolBarView.getRectangleButton().addActionListener(e -> paintingModel.getG2d().drawRect(50,50,100,100));
        toolBarView.getColourChooser().getSelectionModel().addChangeListener(e -> changeColour());
    }

    private void changeColour() {
        disableEraserIfActive();
        toolBarView.getPencilButton().setSelected(true);
        Color newColour = toolBarView.getColour();
        if (newColour != null) {
            paintingModel.setCurrentColour(newColour);
        }
    }

    private void updateStrokeWidth() {
        String selectedValue = (String) toolBarView.getBrushSizeDropdown().getSelectedItem();

        if(selectedValue != null) {
            paintingModel.setStrokeWidth(Integer.parseInt(selectedValue.replace(" px", "")));
            //System.out.println("Neue Pinselbreite: " + paintingModel.getStrokeWidth() + " px");
        }
    }

    public void drawLine(int x1, int y1, int x2, int y2) {
        if (!isPaintingToolSelected()) return;

        paintingModel.getG2D().setStroke(new BasicStroke(paintingModel.getStrokeWidth(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        paintingModel.getG2D().drawLine(x1, y1, x2, y2);
    }

    private boolean isPaintingToolSelected() {
        if (!toolBarView.getPencilButton().isSelected() && !toolBarView.getEraserButton().isSelected()) {
            return false;
        } else {
            return true;
        }
    }

    private void activatePencil() {
        if (toolBarView.getEraserButton().isSelected()) {
            paintingModel.colourChangeBack();
            disableEraserIfActive();
        }
    }

    private void disablePencilIfActive() {
        if (toolBarView.getPencilButton().isSelected()) {
            toolBarView.getPencilButton().setSelected(false);
        }
    }

    private void activateEraser() {
        disablePencilIfActive();
        if (toolBarView.getEraserButton().isSelected()) {
            paintingModel.setCurrentColour(Color.WHITE);
        } else {
            toolBarView.getPencilButton().setSelected(true);
            paintingModel.colourChangeBack();
        }
    }

    private void disableEraserIfActive() {
        if (toolBarView.getEraserButton().isSelected()) {
            toolBarView.getEraserButton().setSelected(false);
            //System.out.println("Radierer deaktiviert");
        }
    }
}