package model;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Model for managing image properties such as dimensions, DPI, and file metadata.
 */
public class ImagePropertiesModel {
    private final int defaultWidth;
    private final int defaultHeight;
    private int width;  // Immer in Pixel gespeichert
    private int height;
    private int dpi;
    private final File imageFile;
    private Unit currentUnit = Unit.PIXEL;

    /**
     * Defines measurement units for image dimensions.
     */
    public enum Unit {
        PIXEL, CM, INCH
    }

    /**
     * Constructs an ImagePropertiesModel with given width, height, DPI, and file reference.
     *
     * @param width       The initial width in pixels.
     * @param height      The initial height in pixels.
     * @param dpi         The dots per inch resolution.
     * @param imageFile   The associated image file.
     */
    public ImagePropertiesModel(int width, int height, int dpi, File imageFile) {
        this.defaultWidth = 1247;
        this.defaultHeight = 1247;
        this.width = width;
        this.height = height;
        this.dpi = dpi;
        this.imageFile = imageFile;
    }

    /**
     * Returns the width in the currently set unit.
     *
     * @return The width converted to the selected unit.
     */
    public double getWidthInCurrentUnit() {
        return convertPixelsToUnit(width, currentUnit);
    }

    /**
     * Returns the height in the currently set unit.
     *
     * @return The height converted to the selected unit.
     */
    public double getHeightInCurrentUnit() {
        return convertPixelsToUnit(height, currentUnit);
    }

    /**
     * Sets the DPI value, ensuring it remains positive.
     *
     * @param dpi The new DPI value.
     */
    public void setDpi(int dpi) {
        if (dpi > 0) {
            this.dpi = dpi;
        }
    }

    /**
     * Sets the image size in pixels.
     *
     * @param width  The new width in pixels.
     * @param height The new height in pixels.
     */
    public void setSizeInPixels(int width, int height) {
        this.width = width;
        this.height = height;
        this.currentUnit = Unit.PIXEL;
    }

    /**
     * Sets the image size in the currently selected unit.
     *
     * @param newWidth  The new width.
     * @param newHeight The new height.
     */
    public void setSizeInCurrentUnit(double newWidth, double newHeight) {
        this.width = convertUnitToPixels(newWidth, currentUnit);
        this.height = convertUnitToPixels(newHeight, currentUnit);
    }

    /**
     * Converts stored dimensions to pixels.
     */
    public void convertToPixels() {
        this.width = convertUnitToPixels(getWidthInCurrentUnit(), currentUnit);
        this.height = convertUnitToPixels(getHeightInCurrentUnit(), currentUnit);
        this.currentUnit = Unit.PIXEL;
    }

    /**
     * Converts a unit-based value to pixels.
     *
     * @param value The value in the selected unit.
     * @param unit  The unit type.
     * @return The converted pixel value.
     */
    public int convertUnitToPixels(double value, Unit unit) {
        switch (unit) {
            case CM:
                return (int) Math.round(value * (dpi / 2.54)); // Centimetres to pixels
            case INCH:
                return (int) Math.round(value * dpi); // Inches to pixels
            default:
                return (int) value; // Pixels remain unchanged
        }
    }

    /**
     * Changes the unit type and updates stored values accordingly.
     *
     * @param newUnit The new unit type.
     */
    public void convertUnit(Unit newUnit) {
        double widthInNewUnit = convertPixelsToUnit(width, newUnit);
        double heightInNewUnit = convertPixelsToUnit(height, newUnit);
        this.width = convertUnitToPixels(widthInNewUnit, newUnit);
        this.height = convertUnitToPixels(heightInNewUnit, newUnit);
        currentUnit = newUnit;
    }

    /**
     * Returns the last modified date of the associated file.
     *
     * @return A formatted string representing the last modified date.
     */
    public String getLastModifiedDate() {
        if (imageFile == null || !imageFile.exists())
            return "Nicht verfügbar";

        return new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date(imageFile.lastModified()));
    }

    /**
     * Returns the file size in megabytes.
     *
     * @return A formatted string representing the file size.
     */
    public String getFileSize() {
        if (imageFile == null || !imageFile.exists())
            return "Nicht verfügbar";

        return String.format("%.2f MB", imageFile.length() / (1024.0 * 1024.0));
    }

    /**
     * Converts pixel values to the selected unit.
     *
     * @param value The value in pixels.
     * @param unit  The unit to convert to.
     * @return The converted value.
     */
    private double convertPixelsToUnit(int value, Unit unit) {
        switch (unit) {
            case CM:
                return value / (dpi / 2.54); // Pixels to centimetres
            case INCH:
                return (double) value / dpi; // Pixels to inches
            default:
                return value;  // Pixels remain unchanged
        }
    }

    /**
     * Getter methods for accessing ImagePropertiesModel components.
     */
    public int getDefaultWidth() {return defaultWidth; }
    public int getDefaultHeight() { return defaultHeight; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public int getDpi() { return dpi; }
    public Unit getCurrentUnit() { return currentUnit; }
    public void setCurrentUnit(Unit unit) { this.currentUnit = unit; }
}
