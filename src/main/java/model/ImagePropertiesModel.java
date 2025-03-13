package model;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ImagePropertiesModel {
    private  int defaultWidth;
    private int defaultHeight;
    private int width;  // Immer in Pixel gespeichert
    private int height;
    private int dpi;
    private final File imageFile;

    public enum Unit {
        PIXEL, CM, INCH
    }

    private Unit currentUnit = Unit.PIXEL;

    public ImagePropertiesModel(int width, int height, int dpi, File imageFile) {
        this.defaultWidth = 1247;
        this.defaultHeight = 1247;
        this.width = width;
        this.height = height;
        this.dpi = dpi;
        this.imageFile = imageFile;
    }

    public int getDefaultWidth() {return defaultWidth; }
    public int getDefaultHeight() { return defaultHeight; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public int getDpi() { return dpi; }
    public Unit getCurrentUnit() { return currentUnit; }
    public void setCurrentUnit(Unit unit) { this.currentUnit = unit; }

    /**
     * Gibt die Breite in der aktuellen Einheit zurück.
     */
    public double getWidthInCurrentUnit() {
        return convertPixelsToUnit(width, currentUnit);
    }

    /**
     * Gibt die Höhe in der aktuellen Einheit zurück.
     */
    public double getHeightInCurrentUnit() {
        return convertPixelsToUnit(height, currentUnit);
    }

    public void setDpi(int dpi) {
        if (dpi > 0) {
            this.dpi = dpi;
        }
    }

    public void setSizeInPixels(int width, int height) {
        this.width = width;
        this.height = height;
        this.currentUnit = Unit.PIXEL; // Immer als Pixel speichern
    }

    /**
     * Setzt die Werte in der aktuellen Einheit und speichert sie als Pixel.
     */
    public void setSizeInCurrentUnit(double newWidth, double newHeight) {
        this.width = convertUnitToPixels(newWidth, currentUnit);
        this.height = convertUnitToPixels(newHeight, currentUnit);
    }

    public void convertToPixels() {
        this.width = convertUnitToPixels(getWidthInCurrentUnit(), currentUnit);
        this.height = convertUnitToPixels(getHeightInCurrentUnit(), currentUnit);
        this.currentUnit = Unit.PIXEL;
    }


    /**
     * Wandelt Pixel in die gewählte Einheit um.
     */
    private double convertPixelsToUnit(int value, Unit unit) {
        switch (unit) {
            case CM:
                return value / (dpi / 2.54); // Umrechnung Pixel → cm
            case INCH:
                return (double) value / dpi; // Umrechnung Pixel → Zoll
            default:
                return value;  // Pixel bleiben Pixel
        }
    }

    /**
     * Wandelt eine Einheit in Pixel um.
     */
    public int convertUnitToPixels(double value, Unit unit) {
        switch (unit) {
            case CM:
                return (int) Math.round(value * (dpi / 2.54)); // cm → Pixel
            case INCH:
                return (int) Math.round(value * dpi); // Zoll → Pixel
            default:
                return (int) value; // Pixel bleiben unverändert
        }
    }

    /**
     * Ändert die Einheit und aktualisiert die Werte entsprechend.
     */
    public void convertToUnit(Unit newUnit) {
        double widthInNewUnit = convertPixelsToUnit(width, newUnit);
        double heightInNewUnit = convertPixelsToUnit(height, newUnit);
        this.width = convertUnitToPixels(widthInNewUnit, newUnit);
        this.height = convertUnitToPixels(heightInNewUnit, newUnit);
        currentUnit = newUnit;
    }

    /**
     * Gibt das letzte Änderungsdatum der Datei zurück.
     */
    public String getLastModifiedDate() {
        if (imageFile == null || !imageFile.exists()) return "Nicht verfügbar";

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        return sdf.format(new Date(imageFile.lastModified()));
    }

    /**
     * Gibt die Dateigröße in MB zurück.
     */
    public String getFileSize() {
        if (imageFile == null || !imageFile.exists()) return "Nicht verfügbar";

        double fileSizeMB = imageFile.length() / (1024.0 * 1024.0);
        return String.format("%.2f MB", fileSizeMB);
    }
}
