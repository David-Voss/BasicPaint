package controller.components;

import controller.MainController;
import toolbox.LoggingHelper;
import toolbox.PaintingTool;
import model.PaintingModel;
import view.MainWindow;
import view.components.PaintingPanelView;
import view.components.ToolBarView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

/**
 * Controls the interaction between the painting panel and the user's input.
 * Handles drawing, shape previews, and event-based input processing.
 */
public class PaintingPanelController {
    private final MainWindow mainWindow;
    private final MainController mainController;
    private final PaintingPanelView paintingView;
    private final PaintingModel paintingModel;
    private final ToolBarView toolBarView;

    private Point startPoint;
    private Point endPoint;
    private boolean isDragging = false;
    private boolean isDrawingShape = false; // Neue Variable für Form-Zeichnen

    public PaintingPanelController(MainWindow mainWindow, MainController mainController) {
        this.mainWindow = mainWindow;
        this.mainController = mainController;
        this.paintingView = mainWindow.getPaintingPanelView();
        this.paintingModel = paintingView.getPaintingModel();
        this.toolBarView = mainWindow.getToolBarView();

        paintingView.setFocusable(true);
        paintingView.requestFocus();

        initListeners();
    }

    private void initListeners() {
        registerMouseListener();
        registerKeyListener();
    }

    private void registerMouseListener() {
        paintingView.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                handleMousePressedAction(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                handleMouseReleasedAction(e);
            }
        });

        paintingView.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                handleMouseMovedAction(e);
            }

            public void mouseDragged(MouseEvent e) {
                handleMouseDraggedAction(e);
            }
        });

        paintingView.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                paintingView.clearPreviewPoint(); // Entfernt den Vorschaupunkt
            }
        });
    }

    private void handleMousePressedAction(MouseEvent e) {
        if (toolBarView.getMagnifierButton().isSelected()) {
            handleMagnifierAction(e);
            return;
        } else {
            startDrawingAction(e);
        }
    }

    private void handleMouseReleasedAction(MouseEvent e) {
        if (isDragging) {
            endPoint = e.getPoint();
            PaintingTool selectedTool = toolBarView.getSelectedTool();

            switch (selectedTool) {
                case RECTANGLE:
                    paintingModel.drawRectangle(startPoint.x, startPoint.y, endPoint.x, endPoint.y);
                    LoggingHelper.log("Rechteck gezeichnet.");
                    break;
                case ELLIPSE:
                    paintingModel.drawEllipse(startPoint.x, startPoint.y, endPoint.x, endPoint.y);
                    LoggingHelper.log("Ellipse gezeichnet.");
                    break;
                case LINE:
                    paintingModel.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y);
                    LoggingHelper.log("Linie gezeichnet.");
                    break;
                case PENCIL:
                    LoggingHelper.log("Freies zeichnen.");
                    break;
                case ERASER:
                    LoggingHelper.log("Radieren.");
                    break;
                default:
                    break;
            }
            paintingView.clearPreviewShape();
            paintingView.repaint();
        }
        if (!isPaintingToolSelected(PaintingTool.MAGNIFIER)){
            isDragging = false;
            LoggingHelper.log("Bearbeitung beendet. \n");
        }
    }

    private void handleMouseMovedAction(MouseEvent e){
        // Falls der Pencil oder Eraser aktiv ist, zeige eine Vorschau des Punktes
        if (isPencilOrEraserSelected()) {
            boolean isEraser = toolBarView.getEraserButton().isSelected();
            paintingView.setPreviewPoint(e.getPoint(), isEraser);
        } else {
            paintingView.clearPreviewPoint(); // Andernfalls keine Vorschau
        }
    }

    private void handleMouseDraggedAction(MouseEvent e) {
        if (isPencilOrEraserSelected()) {
            boolean isEraser = toolBarView.getEraserButton().isSelected();
            paintingView.setPreviewPoint(e.getPoint(), isEraser);

            if (startPoint == null) {  // Falls `startPoint` null ist, setze es neu
                startPoint = e.getPoint();
            }

            freeDrawing(startPoint.x, startPoint.y, e.getX(), e.getY());
            startPoint = e.getPoint();
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
                        freeDrawing(startPoint.x, startPoint.y, endPoint.x, endPoint.y);
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

    private void registerKeyListener() {
        // Alternative: Bessere Methode mit InputMap für globale Tastenkombination
        paintingView.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "cancelDrawing");

        paintingView.getActionMap().put("cancelDrawing", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isDragging || isDrawingShape) {
                    LoggingHelper.log("Zeichnen abgebrochen (ESC).");
                    cancelDrawing();
                }
            }
        });
    }

    private void handleMagnifierAction(MouseEvent e) {
        e.consume();

        if (SwingUtilities.isLeftMouseButton(e)) {
            // TODO: Implement the zoom control here as soon as the function has been implemented.
        } else if (SwingUtilities.isRightMouseButton(e)) {
            // TODO: Implement the zoom control here as soon as the function has been implemented.
        }
    }

    private void startDrawingAction(MouseEvent e) {
        if (!isDragging) {
            LoggingHelper.log("Zeichenfläche wird bearbeitet. \n" +
                    LoggingHelper.formatMessage("Tool: " + toolBarView.getSelectedTool().getDisplayName()));
        }

        startPoint = e.getPoint();
        isDragging = true;
        isDrawingShape = true;

        if (SwingUtilities.isRightMouseButton(e)) {
            LoggingHelper.log("Rechte Maustaste gedrückt.");
        }

        if (isPaintingToolSelected(PaintingTool.PENCIL) || isPaintingToolSelected(PaintingTool.ERASER)) {
            if (paintingModel.getStrokeWidth() <= 2) {
                paintingModel.getG2D().fillRect(e.getX(), e.getY(), paintingModel.getStrokeWidth(), paintingModel.getStrokeWidth());
                LoggingHelper.log("Punkt gesetzt.");
            } else {
                drawCircle(e.getX(), e.getY(), ((int) Math.floor(paintingModel.getStrokeWidth() / 2.0)), paintingModel.getG2D());
                fillCircle(e.getX(), e.getY(), ((int) Math.floor(paintingModel.getStrokeWidth() / 2.0)), paintingModel.getG2D());
                LoggingHelper.log("Punkt gesetzt.");
            }
            paintingView.repaint();
        }
        else if (toolBarView.getSelectedTool() == PaintingTool.FILL) {
            paintingModel.floodFill(e.getX(),e.getY(),paintingModel.getCurrentColour(), 50);
            paintingView.repaint();
        }
        else if (SwingUtilities.isRightMouseButton(e)) {
            LoggingHelper.log("Zeichnen abgebrochen (rechte Maustaste). \n");
            cancelDrawing();
            return; // Sofort beenden, nichts weiter tun
        }
    }

    private void cancelDrawing() {
        isDragging = false;
        isDrawingShape = false; // Form-Zeichnen abbrechen
        startPoint = null;
        endPoint = null;
        paintingView.clearPreviewShape(); // Vorschau löschen
        paintingView.repaint(); // Ansicht aktualisieren
    }

    public void setCanvasSize(int width, int height) {
        paintingModel.setCanvasSize(width, height);
        setPaintingPanelSize(width, height);

        mainController.getStatusBarController().updateImageSize(width, height);
    }

    public void resizePanelWhenOpenedFileIsWiderOrHigher(int width, int height) {
        boolean isWider = width > paintingView.getWidth();
        boolean isHigher = height > paintingView.getWidth();

        if (isWider || isHigher) {
            setPaintingPanelSize(width, height);
        }
    }

    public void setPaintingPanelSize(int width, int height) {
        paintingView.setPreferredSize(new Dimension(width, height));
        paintingView.revalidate(); // Aktualisiert das Layout-Management
        paintingView.repaint(); // Zeichnet das Panel neu
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
        int strokeWidth = paintingModel.getStrokeWidth();
        if (strokeWidth == 1) {
            g2d.fillRect(x, y, 1, 1);
        } else {
            g2d.fillOval(x - strokeWidth / 2,
                    y - strokeWidth / 2,
                    strokeWidth,
                    strokeWidth);
        }
    }

    public void fillCircle(int centerX, int centerY, int radius, Graphics2D g2d) {
        if (toolBarView.getSelectedTool() == PaintingTool.ERASER) {
            g2d.setColor(paintingModel.getBackgroundColour());
        } else {
            g2d.setColor(paintingModel.getCurrentColour());
        }
        for (int y = -radius; y <= radius; y++) {
            for (int x = -radius; x <= radius; x++) {
                if (x * x + y * y <= radius * radius) {
                    g2d.fillRect(centerX + x, centerY + y, 1, 1);
                }
            }
        }
    }

    public void drawCircle(int centerX, int centerY, int radius, Graphics2D g2d) {
        int x = 0;
        int y = radius;
        int d = 3 - 2 * radius;

        while (y >= x) {
            // Setze die 8 symmetrischen Punkte für den Kreis
            g2d.fillRect(centerX + x, centerY + y, 1, 1);
            g2d.fillRect(centerX - x, centerY + y, 1, 1);
            g2d.fillRect(centerX + x, centerY - y, 1, 1);
            g2d.fillRect(centerX - x, centerY - y, 1, 1);
            g2d.fillRect(centerX + y, centerY + x, 1, 1);
            g2d.fillRect(centerX - y, centerY + x, 1, 1);
            g2d.fillRect(centerX + y, centerY - x, 1, 1);
            g2d.fillRect(centerX - y, centerY - x, 1, 1);

            x++;
            if (d > 0) {
                y--;
                d = d + 4 * (x - y) + 10;
            } else {
                d = d + 4 * x + 6;
            }
        }
    }

    public void freeDrawing(int x1, int y1, int x2, int y2) {
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
        return toolBarView.getLineButton().isSelected() ||
                toolBarView.getEllipseButton().isSelected() ||
                toolBarView.getRectangleButton().isSelected();
    }

    private boolean isPaintingToolSelected(PaintingTool tool) {
        return toolBarView.getSelectedTool() == tool;
    }
}