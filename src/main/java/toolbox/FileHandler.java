package toolbox;

import model.PaintingModel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 * Handles file operations like opening, saving, and managing images.
 */
public class FileHandler {

    private File currentFile;
    private PaintingModel paintingModel;
    private Component parent;

    /**
     * Constructs a new FileHandler with the given PaintingModel.
     * @param paintingModel The model holding the canvas data.
     * @param parent The parent component for dialog windows.
     */
    public FileHandler(PaintingModel paintingModel, Component parent) {
        this.paintingModel = paintingModel;
        this.parent = parent;
        this.currentFile = null;
    }

    /**
     * Starts a new file (clears the canvas and resets the file reference).
     */
    public void newFile() {
        paintingModel.clearCanvas();
        resetFile();
        LoggingHelper.log("Neues Bild erstellt. \n" +
                DateTimeStamp.time() + ": Keine ungespeicherten Änderungen. \n");
    }

    /**
     * Opens an image file into the drawing panel.
     * @return The loaded image, or null if canceled.
     */
    public BufferedImage openFile() {
        JFileChooser fileChooser = new JFileChooser();
        FileChooserConfigurator.configureFileChooser(fileChooser);

        int result = fileChooser.showOpenDialog(parent);
        if (result != JFileChooser.APPROVE_OPTION) {
            return null;
        }

        File file = fileChooser.getSelectedFile();
        try {
            BufferedImage image = ImageIO.read(file);
            if (image == null) {
                JOptionPane.showMessageDialog(parent,
                        "Format wird nicht unterstützt oder ist beschädigt.",
                        "Fehler: Die Datei konnte nicht geöffnet werden!",
                        JOptionPane.ERROR_MESSAGE);
                return null;
            }
            currentFile = file;
            return image;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(parent,
                    "Fehler beim Laden der Datei:\n" + e.getMessage(),
                    "Fehler",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    /**
     * Saves the current file. If no file is selected, it prompts the user.
     * @return True if successful, false if canceled.
     */
    public boolean saveFile() {
        if (currentFile == null) {
            return saveFileAs();
        }
        return writeFile(currentFile);
    }

    /**
     * Prompts the user to choose a file name and location, then saves the image.
     * @return True if successful, false otherwise.
     */
    public boolean saveFileAs() {
        JFileChooser fileChooser = new JFileChooser();
        FileChooserConfigurator.configureFileChooser(fileChooser);

        if (fileChooser.showSaveDialog(parent) != JFileChooser.APPROVE_OPTION) {
            LoggingHelper.log("Speichern abgebrochen. \n");
            return false;
        }

        File selectedFile = fileChooser.getSelectedFile();
        String fileName = selectedFile.getName().toLowerCase();
        String selectedExtension = fileChooser.getFileFilter().getDescription().contains("PNG") ? "png" : "jpg";

        if (!fileName.endsWith(".jpg") && !fileName.endsWith(".jpeg") && !fileName.endsWith(".png")) {
            selectedFile = new File(selectedFile.getParentFile(), selectedFile.getName() + "." + selectedExtension);
        }

        currentFile = selectedFile;
        return writeFile(currentFile);
    }

    /**
     * Writes the image data to a file.
     * @param file The file to save to.
     * @return True if successful, false otherwise.
     */
    private boolean writeFile(File file) {
        try {
            BufferedImage image = paintingModel.getCanvas();
            if (image == null) {
                LoggingHelper.log("Fehler: Das Bildobjekt ist null. \n");
                return false;
            }

            if (file.exists() && !file.canWrite()) {
                LoggingHelper.log("Fehler: Datei ist schreibgeschützt oder gesperrt. \n");
                return false;
            }

            String format = file.getName().toLowerCase().endsWith(".png") ? "png" : "jpg";
            boolean success = ImageIO.write(image, format, file) || ImageIO.write(convertImage(image), format, file);

            if (!success) {
                throw new IOException(DateTimeStamp.time() + ": Fehler beim Speichern des Bildes. \n");
            }

            LoggingHelper.log("Datei gespeichert: " + file.getAbsolutePath() + "\n");
            return true;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(parent,
                    "Fehler beim Speichern der Datei.\n" + e.getMessage(),
                    "Fehler",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    /**
     * Converts an image to a supported format if necessary.
     * @param image The image to convert.
     * @return A converted BufferedImage.
     */
    private BufferedImage convertImage(BufferedImage image) {
        BufferedImage formattedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = formattedImage.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return formattedImage;
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

        int result = chooser.showOpenDialog(parent);
        if (result != JFileChooser.APPROVE_OPTION) {
            return null; // User hat abgebrochen
        }

        File file = chooser.getSelectedFile();

        // ❗ Sicherstellen, dass file nicht null ist
        if (file == null) {
            LoggingHelper.log("Keine Datei ausgewählt. \n");
            return null;
        }

        // ❗ Verhindern, dass ein Ordner geöffnet wird
        if (file.isDirectory()) {
            chooser.setCurrentDirectory(file);  // Falls Ordner, einfach öffnen
            return null;  // Nicht versuchen, das als Bild zu lesen
        }

        try {
            BufferedImage img = ImageIO.read(file);
            if (img == null) {
                JOptionPane.showMessageDialog(parent,
                        "Die ausgewählte Datei konnte nicht geöffnet werden. \n" +
                                "Möglicherweise wird das Format nicht unterstützt oder die Datei ist beschädigt.",
                        "Dateiöffnungsfehler",
                        JOptionPane.ERROR_MESSAGE);
                return null;
            }
            currentFile = file;
            return img;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(parent,
                    "Fehler beim Laden der Datei:\n" + e.getMessage(),
                    "Dateiöffnungsfehler",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    /**
     * Gets the currently associated file.
     * @return The current file, or null if none is set.
     */
    public File getCurrentFile() {
        return currentFile;
    }

    /**
     * Resets the file reference (e.g., after starting a new file).
     */
    public void resetFile() {
        this.currentFile = null;
    }
}
