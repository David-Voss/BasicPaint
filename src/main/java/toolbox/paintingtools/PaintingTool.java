package toolbox.paintingtools;

/**
 * Enum representing various painting tools available in the application.
 */
public enum PaintingTool {

    /** Tool for filling an area with a selected colour. */
    FILL,

    /** Tool for erasing content by painting with the background colour. */
    ERASER,

    /** Tool for drawing freehand strokes. */
    PENCIL,

    /** Tool for zooming in and out of the canvas. */
    MAGNIFIER,

    /** Tool for drawing straight lines. */
    LINE,

    /** Tool for drawing rectangular shapes. */
    RECTANGLE,

    /** Tool for drawing oval (ellipse) shapes. */
    ELLIPSE;

    /**
     * Returns a user-friendly name for the painting tool.
     *
     * @return A formatted string representing the tool's name.
     */
    public String getDisplayName() {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }
}

