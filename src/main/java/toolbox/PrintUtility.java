package toolbox;

import view.MainWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.print.*;

/**
 * Utility class for printing images from the application.
 */
public class PrintUtility extends JPanel {

    /**
     * Prints the given image from the application.
     *
     * @param mainWindow The main application window, used for error dialogs.
     * @param image      The image to be printed.
     */
    public void printPicture(MainWindow mainWindow, BufferedImage image) {
        PrinterJob job = PrinterJob.getPrinterJob();
        if (job == null) {
            JOptionPane.showMessageDialog(mainWindow, "Drucken wird auf diesem System nicht unterstützt.", "Fehler", JOptionPane.ERROR_MESSAGE);
            LoggingHelper.log("Druckauftrag konnte nicht erstellt werden. \n");
            return;
        }

        job.setPrintable(createPrintable(image));

        if (job.printDialog()) {
            try {
                job.print();
                LoggingHelper.log("Druckvorgang gestartet. \n");
            } catch (PrinterException e) {
                JOptionPane.showMessageDialog(mainWindow, "Fehler beim Drucken.", "Fehler", JOptionPane.ERROR_MESSAGE);
                LoggingHelper.log("Druck fehlgeschlagen: \n" +
                        DateTimeStamp.time() + ": " + e.getMessage() + "\n");
            }
        } else {
            LoggingHelper.log("Druckvorgang vom Benutzer abgebrochen. \n");
        }
    }

    /**
     * Creates a {@link Printable} instance for printing the given image.
     *
     * @param image The image to be printed.
     * @return A {@link Printable} that scales the image to fit the printable area.
     */
    private Printable createPrintable(BufferedImage image) {
        return (graphics, pageFormat, pageIndex) -> {
            if (pageIndex > 0) {
                return Printable.NO_SUCH_PAGE;
            }

            double scaleX = pageFormat.getImageableWidth() / image.getWidth();
            double scaleY = pageFormat.getImageableHeight() / image.getHeight();
            double scale = Math.min(scaleX, scaleY);

            Graphics2D g2d = (Graphics2D) graphics;
            g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
            g2d.scale(scale, scale);
            g2d.drawImage(image, 0, 0, null);

            return Printable.PAGE_EXISTS;
        };
    }
}
