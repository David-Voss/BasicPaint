package model;

import toolbox.LoggingHelper;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Manages the painting canvas, including drawing operations and flood fill.
 */
public class PaintingModel {
    private BufferedImage canvas;
    private Graphics2D g2d;
    private Color currentColour;
    private Color backgroundColour;
    private int strokeWidth;

    /**
     * Constructs a new painting model with the specified dimensions.
     *
     * @param width  The width of the canvas.
     * @param height The height of the canvas.
     */
    public PaintingModel(int width, int height) {
        this.canvas = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        this.g2d = canvas.createGraphics();
        this.g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF); // TODO: Implement function to switch antialiasing on/off after a floolFill function has been implemented that takes antialiasing sufficiently into account.

        this.backgroundColour = Color.WHITE;
        this.currentColour = Color.BLACK;
        this.strokeWidth = 3;

        g2d.setColor(currentColour);
        g2d.setStroke(new BasicStroke(strokeWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        clearCanvas();
    }

    /**
     * Getter methods for accessing PaintinModel components.
     */
    public BufferedImage getCanvas() { return canvas; }
    public Graphics2D getG2D() { return g2d; }
    public Color getCurrentColour() { return currentColour; }
    public Color getBackgroundColour() { return backgroundColour; }
    public int getStrokeWidth() { return strokeWidth; }

    /**
     * Replaces the current canvas with a new image (for example, when loading a file).
     *
     * @param newImage The new BufferedImage to use as the canvas.
     */
    public void setCanvas(BufferedImage newImage) {
        this.canvas = newImage;
        g2d = canvas.createGraphics();
    }

    /**
     * Resizes the canvas while preserving the current content.
     *
     * @param width  The new width.
     * @param height The new height.
     */
    public void setCanvasSize(int width, int height) {
        BufferedImage newCanvas = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D tempG2D = newCanvas.createGraphics();
        tempG2D.setColor(backgroundColour);
        tempG2D.fillRect(0, 0, width, height);
        tempG2D.drawImage(canvas, 0, 0, null);
        tempG2D.dispose();

        LoggingHelper.log("Größe der Zeichenfläche geändert. \n" +
                LoggingHelper.formatMessage("Neue Breite: " + width + " px \n" +
                        LoggingHelper.formatMessage("Neue Höhe: " + height + " px \n")));

        setCanvas(newCanvas);
    }

    /**
     * Clears the entire canvas by filling it with the background colour.
     */
    public void clearCanvas() {
        g2d.setBackground(backgroundColour);
        g2d.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        g2d.setColor(currentColour); // Restore drawing colour
    }

    /**
     * Updates the current drawing colour.
     *
     * @param colour The new colour to be used for drawing.
     */
    public void setCurrentColour(Color colour) {
        this.currentColour = colour;
        g2d.setColor(colour);
        LoggingHelper.log("Farbe gewechselt. \n" +
                LoggingHelper.formatMessage("Neue Farbe: " + currentColour + "\n"));
    }

    /**
     * Updates the stroke width.
     *
     * @param strokeWidth The new stroke width in pixels.
     */
    public void setStrokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;
        if (g2d == null) {  // Falls `g2d` nicht existiert, neu erstellen
            g2d = canvas.createGraphics();
        }
        g2d.setStroke(new BasicStroke(strokeWidth));
    }

    /**
     * Logs the initial state of the painting model to the console.
     */
    public void showInitialPaintingModelValuesInConsole() {
        LoggingHelper.log("Initiale Werte PaintingModel:");
        LoggingHelper.log("Zeichenfläche Höhe = " + canvas.getHeight() + " px.");
        LoggingHelper.log("Zeichenfläche Breite = " + canvas.getWidth() + " px.");
        LoggingHelper.log("currentColour = " + getCurrentColour() + ".");
        LoggingHelper.log("backgroundColour = " + getBackgroundColour() + ".");
        LoggingHelper.log("strokeWidth = " + getStrokeWidth() + " px. \n");
    }
}