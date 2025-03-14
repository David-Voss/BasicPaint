package model;

import controller.MainController;
import toolbox.TimeStamp;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.PriorityQueue;
import java.util.Queue;
import java.awt.Point;

public class PaintingModel {
    private BufferedImage canvas;
    private Graphics2D g2d;

    private Color currentColour;

    private Color backgroundColour;

    private int strokeWidth;

    public PaintingModel() {
        // Standardgröße des Canvas, z.B. 800x600 Pixel.
        this(1247, 1247);
    }

    public PaintingModel(int width, int height) {
        canvas = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        g2d = canvas.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        backgroundColour = Color.WHITE;
        clearCanvas();

        currentColour = Color.BLACK;
        strokeWidth = 3;
        g2d.setColor(currentColour);
        g2d.setStroke(new BasicStroke(strokeWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
    }

    public BufferedImage getCanvas() { return canvas; }
    public Graphics2D getG2D() { return g2d; }
    public Color getCurrentColour() { return currentColour; }
    public Color getBackgroundColour() { return backgroundColour; }
    public int getStrokeWidth() { return strokeWidth; }

    /**
     * Replaces the current canvas image with a new image (for example, when loading a file).
     * @param newImage the new BufferedImage to use as the canvas
     */
    public void setCanvas(BufferedImage newImage) {
        this.canvas = newImage;
        g2d = canvas.createGraphics();
        // Resize the panel to fit new image if necessary.
        //g2d.revalidate();
        //g2d.repaint();
    }

    public void setCanvasSize(int width, int height) {
        // 1️⃣ Neues BufferedImage mit neuer Größe erstellen
        BufferedImage newCanvas = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        // 2️⃣ Altes Bild auf das neue Canvas übertragen
        Graphics2D g2d = newCanvas.createGraphics();
        g2d.setColor(backgroundColour);
        g2d.fillRect(0,0, width, height);
        g2d.drawImage(canvas, 0, 0, null);
        g2d.dispose();

        // 3️⃣ Neues Canvas im Model setzen
        setCanvas(newCanvas);
    }

    public void setCurrentColour(Color colour) {
        //colourChange();
        this.currentColour = colour;
        g2d.setColor(colour);
    }
    public void setStrokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;
        if (g2d == null) {  // Falls `g2d` nicht existiert, neu erstellen
            g2d = canvas.createGraphics();
        }
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
        g2d.setColor(currentColour);
        g2d.setStroke(new BasicStroke(strokeWidth, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));
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

    public void floodFill(BufferedImage canvas, int x, int y, Color newColor, int baseTolerance){
        FloodFill floodFill = new FloodFill();
        floodFill.floodFill(canvas, x, y, newColor, baseTolerance);
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
        g2d.dispose();
        g2d = canvas.createGraphics();
        // ursprüngliche Farbe wiederherstellen
        g2d.setColor(oldColor);
    }

    public void showInitialPaintingModelValuesInConsole() {
        System.out.println(timeStamp() + ": Initiale Werte PaintingModel:");
        System.out.println(timeStamp() + ": Zeichenfläche Höhe = " + canvas.getHeight() + " px.");
        System.out.println(timeStamp() + ": Zeichenfläche Breite = " + canvas.getWidth() + " px.");
        System.out.println(timeStamp() + ": currentColour = " + getCurrentColour() + ".");
        System.out.println(timeStamp() + ": backgroundColour = " + getBackgroundColour() + ".");
        System.out.println(timeStamp() + ": strokeWidth = " + getStrokeWidth() + " px. \n");
    }

    private String timeStamp() {
        return TimeStamp.time();
    }

    public class FloodFill {

        public void floodFill(BufferedImage canvas, int x, int y, Color newColor, int baseTolerance) {
            int targetColor = canvas.getRGB(x, y);
            int replacementColor = newColor.getRGB();

            if (targetColor == replacementColor) return;

            float[][] mask = new float[canvas.getWidth()][canvas.getHeight()];

            Queue<Point> queue = new PriorityQueue<>((p1, p2) -> {
                int diff1 = colorDifference(canvas.getRGB(p1.x, p1.y), targetColor);
                int diff2 = colorDifference(canvas.getRGB(p2.x, p2.y), targetColor);
                return Integer.compare(diff1, diff2);
            });

            queue.add(new Point(x, y));
            mask[x][y] = 1.0f;

            while (!queue.isEmpty()) {
                Point p = queue.poll();

                if (p.x < 0 || p.x >= canvas.getWidth() || p.y < 0 || p.y >= canvas.getHeight()) continue;

                int pixelColor = canvas.getRGB(p.x, p.y);
                int adaptiveTolerance = adjustTolerance(pixelColor, targetColor, baseTolerance);
                if (!colorWithinTolerance(pixelColor, targetColor, adaptiveTolerance)) continue;

                // Harte Farbe setzen, wenn bereits stark eingefärbt wurde
                if (mask[p.x][p.y] >= 0.9) {
                    canvas.setRGB(p.x, p.y, replacementColor);
                } else {
                    // Nur in Randbereichen mischen
                    double blendFactor = mask[p.x][p.y];
                    int blendedColor = blendColors(pixelColor, replacementColor, blendFactor);

                    // Falls die Farbabweichung zu groß ist, setze direkt die neue Farbe
                    if (colorDifference(blendedColor, replacementColor) > 10) {
                        canvas.setRGB(p.x, p.y, replacementColor);
                    } else {
                        canvas.setRGB(p.x, p.y, blendedColor);
                    }
                }

                // Maske aktualisieren, aber nicht über 1.0 hinaus erhöhen
                mask[p.x][p.y] = Math.min(mask[p.x][p.y] + 0.2f, 1.0f);

                addNeighbor(queue, mask, p.x + 1, p.y, canvas);
                addNeighbor(queue, mask, p.x - 1, p.y, canvas);
                addNeighbor(queue, mask, p.x, p.y + 1, canvas);
                addNeighbor(queue, mask, p.x, p.y - 1, canvas);
            }
        }

        private void addNeighbor(Queue<Point> queue, float[][] mask, int x, int y, BufferedImage canvas) {
            if (x >= 0 && x < canvas.getWidth() && y >= 0 && y < canvas.getHeight() && mask[x][y] == 0.0f) {
                queue.add(new Point(x, y));
                mask[x][y] = 0.1f;
            }
        }

        private int adjustTolerance(int color1, int color2, int baseTolerance) {
            int difference = colorDifference(color1, color2);
            return Math.max(baseTolerance - (difference / 10), 5);
        }

        private int colorDifference(int color1, int color2) {
            Color c1 = new Color(color1, true);
            Color c2 = new Color(color2, true);
            return Math.abs(c1.getRed() - c2.getRed()) +
                    Math.abs(c1.getGreen() - c2.getGreen()) +
                    Math.abs(c1.getBlue() - c2.getBlue());
        }

        private boolean colorWithinTolerance(int color1, int color2, int tolerance) {
            Color c1 = new Color(color1, true);
            Color c2 = new Color(color2, true);
            return (Math.abs(c1.getRed() - c2.getRed()) <= tolerance &&
                    Math.abs(c1.getGreen() - c2.getGreen()) <= tolerance &&
                    Math.abs(c1.getBlue() - c2.getBlue()) <= tolerance);
        }

        private int blendColors(int originalColor, int newColor, double blendFactor) {
            Color orig = new Color(originalColor, true);
            Color blend = new Color(newColor, true);

            int r = (int) (orig.getRed() * (1 - blendFactor) + blend.getRed() * blendFactor);
            int g = (int) (orig.getGreen() * (1 - blendFactor) + blend.getGreen() * blendFactor);
            int b = (int) (orig.getBlue() * (1 - blendFactor) + blend.getBlue() * blendFactor);

            return new Color(r, g, b, orig.getAlpha()).getRGB();
        }
    }
}