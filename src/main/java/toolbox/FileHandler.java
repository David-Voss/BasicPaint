package toolbox;

import java.awt.Component;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Helper class for file operations such as opening and saving images.
 * It uses {@link JFileChooser} with a {@link FileNameExtensionFilter} to allow only JPEG files.
 * This class keeps track of the current file path for save operations.
 */
public class FileHandler {
    /** The current file associated with the drawing (opened or last saved). */
    private File currentFile;
    //private FileChooserConfigurator chooserConfigurator;

    /**
     * Constructs a new FileHandler with no file initially associated.
     */
    public FileHandler() {
        this.currentFile = null;
    }

    /**
     * Opens a file chooser dialog to load a JPEG image file.
     * Only files with extensions ".jpg" or ".jpeg" are displayed.
     * @param parent the parent component for the dialog
     * @return a BufferedImage of the selected file, or null if cancelled or an error occurred
     */
    public BufferedImage openImage(Component parent) {
        JFileChooser chooser = new JFileChooser();
        FileChooserConfigurator.configureFileChooser(chooser);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("JPEG Images (*.jpg, *.jpeg)", "jpg", "jpeg");
        chooser.setFileFilter(filter);
        chooser.setAcceptAllFileFilterUsed(false);
        int result = chooser.showOpenDialog(parent);
        if (result != JFileChooser.APPROVE_OPTION) {
            return null; // user cancelled
        }
        File file = chooser.getSelectedFile();
        try {
            BufferedImage img = ImageIO.read(file);
            if (img == null) {
                // If the file is not a known image type or corrupted, ImageIO may return null.
                JOptionPane.showMessageDialog(parent,
                        "Die ausgewählte Datei konnte nicht geöffnet werden. Möglicherweise wird das Format nicht unterstützt oder die Datei ist beschädigt.",
                        "Dateiöffnungsfehler",
                        JOptionPane.ERROR_MESSAGE);
                return null;
            }
            currentFile = file;
            return img;
        } catch (IOException e) {
            // Show error message if reading fails.
            JOptionPane.showMessageDialog(parent,
                    "Fehler beim Laden der Datei:\n" + e.getMessage(),
                    "Dateiöffnungsfehler",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    /**
     * Gets the currently associated file.
     * @return the current file, or null if none is associated
     */
    public File getFile() {
        return currentFile;
    }

    /**
     * Resets the current file association (e.g., when starting a new drawing).
     */
    public void resetFile() {
        this.currentFile = null;
    }
}
