package controller.components;

import toolbox.PaintingTool;
import model.PaintingModel;
import view.components.PaintingPanelView;
import view.components.ToolBarView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

public class PaintingPanelController {
    private PaintingPanelView paintingView;
    private PaintingModel paintingModel;
    private ToolBarView toolBarView;


    private Point startPoint;
    private Point endPoint;
    private boolean isDragging = false;
    private boolean isDrawingShape = false; // Neue Variable für Form-Zeichnen

    public PaintingPanelController(PaintingPanelView paintingView, ToolBarView toolBarView) {
        this.paintingView = paintingView;
        this.paintingModel = paintingView.getPaintingModel();
        this.toolBarView = toolBarView;

        paintingView.setFocusable(true);
        paintingView.requestFocus();

        initialiseListeners();
    }

    private void initialiseListeners() {
        paintingView.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                startPoint = e.getPoint();
                isDragging = true;
                isDrawingShape = true;

                if (isPaintingToolSelected(PaintingTool.PENCIL) || isPaintingToolSelected(PaintingTool.ERASER)) {
                    drawPoint(e.getX(), e.getY());
                    paintingView.repaint();
                }
                else if (toolBarView.getSelectedTool() == PaintingTool.FILL) {
                    paintingModel.floodFill(e.getX(), e.getY(), paintingModel.getCurrentColour(), 30);
                    paintingView.repaint();
                }
                else if (SwingUtilities.isRightMouseButton(e) && !isPencilOrEraserSelected()) {
                    System.out.println("Zeichnen abgebrochen (rechte Maustaste).");
                    cancelDrawing();
                    return; // Sofort beenden, nichts weiter tun
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (isDragging) {
                    endPoint = e.getPoint();
                    PaintingTool selectedTool = toolBarView.getSelectedTool();

                    switch (selectedTool) {
                        case RECTANGLE:
                            paintingModel.drawRectangle(startPoint.x, startPoint.y, endPoint.x, endPoint.y);
                            break;
                        case ELLIPSE:
                            paintingModel.drawEllipse(startPoint.x, startPoint.y, endPoint.x, endPoint.y);
                            break;
                        case LINE:
                            paintingModel.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y);
                            break;
                        default:
                            break;
                    }

                    paintingView.clearPreviewShape();
                    paintingView.repaint();
                }
                isDragging = false;
            }
        });

        paintingView.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                // Falls der Pencil oder Eraser aktiv ist, zeige eine Vorschau des Punktes
                if (isPencilOrEraserSelected()) {
                    boolean isEraser = toolBarView.getEraserButton().isSelected();
                    paintingView.setPreviewPoint(e.getPoint(), isEraser);
                } else {
                    paintingView.clearPreviewPoint(); // Andernfalls keine Vorschau
                }
            }

            public void mouseDragged(MouseEvent e) {
                if (isPencilOrEraserSelected()) {
                    boolean isEraser = toolBarView.getEraserButton().isSelected();
                    paintingView.setPreviewPoint(e.getPoint(), isEraser);
                    drawPoint(e.getX(), e.getY());
                    paintingView.repaint();
                }
                else if (isDragging) {
                    if (isPencilOrEraserSelected() || isPaintingToolSelected()) {
                        Point endPoint = e.getPoint();
                        PaintingTool selectedTool = toolBarView.getSelectedTool();

                        Shape previewShape = null;

                        switch (selectedTool) {
                            case PENCIL, ERASER:
                                endPoint = e.getPoint();
                                drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y);
                                startPoint = endPoint;
                                break;
                            case RECTANGLE:
                                previewShape = new Rectangle2D.Float(
                                        Math.min(startPoint.x, endPoint.x),
                                        Math.min(startPoint.y, endPoint.y),
                                        Math.abs(startPoint.x - endPoint.x),
                                        Math.abs(startPoint.y - endPoint.y)
                                );
                                break;
                            case ELLIPSE:
                                previewShape = new Ellipse2D.Float(
                                        Math.min(startPoint.x, endPoint.x),
                                        Math.min(startPoint.y, endPoint.y),
                                        Math.abs(startPoint.x - endPoint.x),
                                        Math.abs(startPoint.y - endPoint.y)
                                );
                                break;
                            case LINE:
                                previewShape = new Line2D.Float(startPoint.x, startPoint.y, endPoint.x, endPoint.y);
                                break;
                            case null, default:
                                break;
                        }

                        // Vorschau in der View setzen
                        paintingView.setPreviewShape(previewShape);
                    }
                }
            }
        });

        paintingView.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                paintingView.clearPreviewPoint(); // Entfernt den Vorschaupunkt
            }
        });

        paintingView.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    if (isDragging || isDrawingShape) {  // Prüft beides: Linien & Formen
                        System.out.println("Zeichnen abgebrochen (ESC gedrückt).");
                        cancelDrawing();
                        e.consume(); // Verhindert, dass ESC andere Events auslöst
                    }
                }
            }
        });

        toolBarView.getBrushSizeDropdown().addActionListener(e -> {
            updateStrokeWidth();
            ensureFocus();
        });

        toolBarView.getPencilButton().addActionListener(e -> ensureFocus());
        toolBarView.getFillButton().addActionListener(e -> ensureFocus());
        toolBarView.getEraserButton().addActionListener(e -> ensureFocus());

        toolBarView.getLineButton().addActionListener(e -> ensureFocus());
        toolBarView.getEllipseButton().addActionListener(e -> ensureFocus());
        toolBarView.getRectangleButton().addActionListener(e -> ensureFocus());

        toolBarView.getColourChooser().getSelectionModel().addChangeListener(e -> {
            changeColour();
            ensureFocus();
        });
    }

    private void ensureFocus() {
        SwingUtilities.invokeLater(() -> paintingView.requestFocusInWindow());
    }

    private void cancelDrawing() {
        System.out.println("Zeichenvorgang abgebrochen.");
        isDragging = false;
        isDrawingShape = false; // Form-Zeichnen abbrechen
        startPoint = null;
        endPoint = null;
        paintingView.clearPreviewShape(); // Vorschau löschen
        paintingView.repaint(); // Ansicht aktualisieren
    }


    private void changeColour() {
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

    private void eraserOrOtherToolColor() {
        Graphics2D g2d = paintingModel.getG2D();

        if (toolBarView.getEraserButton().isSelected()) {
            g2d.setColor(paintingModel.getBackgroundColour());
        } else {
            g2d.setColor(paintingModel.getCurrentColour());
        }
    }

    public void drawPoint(int x, int y) {
        Graphics2D g2d = paintingModel.getG2D();
        eraserOrOtherToolColor();
        g2d.fillOval(x - paintingModel.getStrokeWidth() / 2,
                y - paintingModel.getStrokeWidth() / 2,
                paintingModel.getStrokeWidth(),
                paintingModel.getStrokeWidth());
    }

    public void drawLine(int x1, int y1, int x2, int y2) {
        if (!isPencilOrEraserSelected()) return;
        Graphics2D g2d = paintingModel.getG2D();
        eraserOrOtherToolColor();
        g2d.setStroke(new BasicStroke(paintingModel.getStrokeWidth(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.drawLine(x1, y1, x2, y2);
    }

    private boolean isPencilOrEraserSelected() {
        return toolBarView.getPencilButton().isSelected() || toolBarView.getEraserButton().isSelected();
    }

    private boolean isPaintingToolSelected() {
        return toolBarView.getLineButton().isSelected() || toolBarView.getEllipseButton().isSelected() || toolBarView.getRectangleButton().isSelected();
    }

    private boolean isPaintingToolSelected(PaintingTool tool) {
        return toolBarView.getSelectedTool() == tool;
    }
}