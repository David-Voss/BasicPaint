package toolbox;

import view.MainWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.print.*;

public class PrintService extends JPanel {

    public void printPicture(MainWindow mainWindow, BufferedImage image) {
        PrinterJob job = PrinterJob.getPrinterJob();

        job.setPrintable(new Printable() {
            @Override
            public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) {
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
            }
        });

        if (job.printDialog()) {
            try {
                job.print();
                LoggingHelper.log("Bild wird gedruckt. \n");
            } catch (PrinterException e) {
                JOptionPane.showMessageDialog(mainWindow, "Fehler beim Drucken.", "Fehler", JOptionPane.ERROR_MESSAGE);
                LoggingHelper.log("Drucken Fehlgeschlagen. \n");
            }

        }
    }
}
