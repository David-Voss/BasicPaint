package model;

import toolbox.PaintingTool;

import java.awt.*;
import java.awt.image.BufferedImage;

public class PaintingModel {
    private BufferedImage canvas;
    private Graphics2D g2d;

    private Color currentColour;
    private Color previousColour;
    private Color tempColour;

    private Color backgroundColour;

    private int strokeWidth;

    private PaintingTool currentTool;

    public PaintingModel() {
        // Standardgröße des Canvas, z.B. 800x600 Pixel.
        this(1700, 1700);
    }

    public PaintingModel(int width, int height) {
        canvas = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        g2d = canvas.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        backgroundColour = Color.WHITE;
        clearCanvas();

        currentColour = Color.BLACK;
        previousColour = Color.BLACK;
        tempColour = Color.BLACK;
        strokeWidth = 3;
        //g2d.fillRect(0, 0, width, height);
        g2d.setColor(currentColour);
        g2d.setStroke(new BasicStroke(strokeWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
    }

    public BufferedImage getCanvas() { return canvas; }
    public Graphics2D getG2D() { return g2d; }
    public Color getCurrentColour() { return currentColour; }
    public Color getPreviousColour() { return previousColour; }
    public Color getTempColour() { return tempColour; }
    public Color getBackgroundColour() { return backgroundColour; }
    public int getStrokeWidth() { return strokeWidth; }

    public void setCurrentColour(Color colour) {
        //colourChange();
        this.currentColour = colour;
        g2d.setColor(colour);
    }
    public void setStrokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;
        g2d.setStroke(new BasicStroke(strokeWidth));
    }

    public void setTool(PaintingTool tool) {
        this.currentTool = tool;
        // If switching tools while drawing, end any current shape preview.
        //this.isDragging = false;
    }

    public void colourChange() {
        this.tempColour = this.previousColour;
        this.previousColour = this.currentColour;
    }

    public void colourChangeBack() {
        this.currentColour = this.previousColour;
        this.previousColour = this.tempColour;
        g2d.setColor(this.currentColour);
    }

    /**
     * Zeichnet eine Linie (bzw. ein Freihand-Liniensegment) von (x1,y1) bis (x2,y2) auf das Canvas.
     * Verwendet die aktuelle Farbe und Strichstärke.
     * Kann für freies Zeichnen (viele kurze Segmente) oder eine einzelne Linie genutzt werden.
     * @param x1 Startpunkt X-Koordinate
     * @param y1 Startpunkt Y-Koordinate
     * @param x2 Endpunkt X-Koordinate
     * @param y2 Endpunkt Y-Koordinate
     */
    public void drawLine(int x1, int y1, int x2, int y2) {
        //Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(currentColour);
        g2d.drawLine(x1, y1, x2, y2);
    }

    /**
     * Zeichnet ein Rechteck auf das Canvas, definiert durch zwei gegenüberliegende Eckpunkte (x1,y1) und (x2,y2).
     * Zeichnet den Rechteckumriss mit aktueller Farbe und Strichstärke.
     * Negative Breite/Höhe werden intern korrigiert (Koordinaten normalisiert).
     * @param x1 X-Koordinate des ersten Eckpunkts (z.B. Maus-Press)
     * @param y1 Y-Koordinate des ersten Eckpunkts (z.B. Maus-Press)
     * @param x2 X-Koordinate des gegenüberliegenden Eckpunkts (z.B. Maus-Release)
     * @param y2 Y-Koordinate des gegenüberliegenden Eckpunkts (z.B. Maus-Release)
     */
    public void drawRectangle(int x1, int y1, int x2, int y2) {
        g2d.setColor(currentColour);
        // obere linke Ecke und Breite/Höhe berechnen
        int x = Math.min(x1, x2);
        int y = Math.min(y1, y2);
        int width = Math.abs(x2 - x1);
        int height = Math.abs(y2 - y1);
        g2d.drawRect(x, y, width, height);
    }

    /**
     * Zeichnet eine Ellipse (Oval) auf das Canvas.
     * Der umlaufende Rahmen der Ellipse wird durch (x1,y1) und (x2,y2) definiert.
     * Zeichnet den Ellipsen-Umriss mit aktueller Farbe und Strichstärke.
     * @param x1 X-Koordinate des ersten Eckpunkts des Begrenzungsrechtecks
     * @param y1 Y-Koordinate des ersten Eckpunkts des Begrenzungsrechtecks
     * @param x2 X-Koordinate des gegenüberliegenden Eckpunkts des Begrenzungsrechtecks
     * @param y2 Y-Koordinate des gegenüberliegenden Eckpunkts des Begrenzungsrechtecks
     */
    public void drawEllipse(int x1, int y1, int x2, int y2) {
        g2d.setColor(currentColour);
        // obere linke Ecke und Breite/Höhe des Begrenzungsrechtecks berechnen
        int x = Math.min(x1, x2);
        int y = Math.min(y1, y2);
        int width = Math.abs(x2 - x1);
        int height = Math.abs(y2 - y1);
        g2d.drawOval(x, y, width, height);
    }

    /**
     * Löscht das gesamte Canvas, indem es mit der Hintergrundfarbe gefüllt wird.
     * (z.B. beim Anlegen einer neuen Datei oder "Alles löschen").
     * Die aktuelle Zeichenfarbe und Strichstärke bleiben erhalten.
     */
    public void clearCanvas() {
        // aktuelle Zeichenfarbe sichern und Hintergrund füllen
        Color oldColor = g2d.getColor();
        g2d.setColor(backgroundColour);
        g2d.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        // ursprüngliche Farbe wiederherstellen
        g2d.setColor(oldColor);
    }
}