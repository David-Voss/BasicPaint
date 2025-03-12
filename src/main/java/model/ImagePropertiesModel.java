package model;

public class ImagePropertiesModel {
    private int width;  // Immer in Pixel gespeichert
    private int height;
    private final int dpi;

    public enum Unit {
        PIXEL, CM, INCH
    }

    private Unit currentUnit = Unit.PIXEL;

    public ImagePropertiesModel(int width, int height, int dpi) {
        this.width = width;
        this.height = height;
        this.dpi = dpi;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getDpi() {
        return dpi;
    }

    public Unit getCurrentUnit() {
        return currentUnit;
    }

    public void setCurrentUnit(Unit unit) {
        this.currentUnit = unit;
    }

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
    private int convertUnitToPixels(double value, Unit unit) {
        switch (unit) {
            case CM:
                return (int) (value * (dpi / 2.54)); // Umrechnung cm → Pixel
            case INCH:
                return (int) (value * dpi); // Umrechnung Zoll → Pixel
            default:
                return (int) value; // Pixel bleiben Pixel
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
}
