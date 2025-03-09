package model;

import toolbox.PaintingTool;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.Queue;
import java.awt.Point;

public class PaintingModel {
    private BufferedImage canvas;
    private Graphics2D g2d;

    private Color currentColour;
    private Color previousColour;
    private Color tempColour;

    private Color backgroundColour;

    private int strokeWidth;

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
        g2d.setStroke(new BasicStroke(strokeWidth, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));
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

    public void floodFill(int x, int y, Color newColor, int baseTolerance) {
        int targetColor = canvas.getRGB(x, y);
        int replacementColor = newColor.getRGB();

        if (targetColor == replacementColor) return;

        Queue<Point> queue = new LinkedList<>();
        queue.add(new Point(x, y));

        while (!queue.isEmpty()) {
            Point p = queue.poll();

            if (p.x < 0 || p.x >= canvas.getWidth() || p.y < 0 || p.y >= canvas.getHeight()) continue;

            int pixelColor = canvas.getRGB(p.x, p.y);

            // Dynamische Toleranz-Anpassung für weichere Kanten
            int adaptiveTolerance = adjustTolerance(pixelColor, targetColor, baseTolerance);
            if (!colorWithinTolerance(pixelColor, targetColor, adaptiveTolerance)) continue;

            // Setzt neue Farbe
            canvas.setRGB(p.x, p.y, replacementColor);

            // 4er-Nachbarschaft (besser für genauere Konturen)
            queue.add(new Point(p.x + 1, p.y));
            queue.add(new Point(p.x - 1, p.y));
            queue.add(new Point(p.x, p.y + 1));
            queue.add(new Point(p.x, p.y - 1));

            // Falls diagonale Nachbarschaft nötig ist:
            queue.add(new Point(p.x + 1, p.y + 1));
            queue.add(new Point(p.x - 1, p.y - 1));
            queue.add(new Point(p.x + 1, p.y - 1));
            queue.add(new Point(p.x - 1, p.y + 1));
        }
    }

    // Dynamische Anpassung der Toleranz für feinere Kantenerkennung
    private int adjustTolerance(int color1, int color2, int baseTolerance) {
        int difference = colorDifference(color1, color2);
        return Math.max(baseTolerance - (difference / 10), 5);  // Mindesttoleranz 5
    }

    // Berechnet den Farbunterschied für bessere Kantenerkennung
    private int colorDifference(int color1, int color2) {
        Color c1 = new Color(color1, true);
        Color c2 = new Color(color2, true);

        return Math.abs(c1.getRed() - c2.getRed()) +
                Math.abs(c1.getGreen() - c2.getGreen()) +
                Math.abs(c1.getBlue() - c2.getBlue());
    }


    // Prüft, ob die Farbe innerhalb der Toleranz liegt
    private boolean colorWithinTolerance(int color1, int color2, int tolerance) {
        Color c1 = new Color(color1, true);
        Color c2 = new Color(color2, true);

        int rDiff = Math.abs(c1.getRed() - c2.getRed());
        int gDiff = Math.abs(c1.getGreen() - c2.getGreen());
        int bDiff = Math.abs(c1.getBlue() - c2.getBlue());

        return (rDiff <= tolerance && gDiff <= tolerance && bDiff <= tolerance);
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