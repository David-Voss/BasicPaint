package toolbox;

import model.PaintingModel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * Handles file operations such as opening, saving, and managing images.
 */
public class FileHandler {

    private File currentFile;
    private final PaintingModel paintingModel;
    private final Component parent;
    private Runnable onSaveFileCallback;

    /**
     * Constructs a new FileHandler for managing image files.
     *
     * @param paintingModel The model holding the canvas data.
     * @param parent        The parent component for dialog windows.
     */
    public FileHandler(PaintingModel paintingModel, Component parent) {
        this.paintingModel = paintingModel;
        this.parent = parent;
        this.currentFile = null;
    }

    /**
     * Sets a callback that is executed after saving a file.
     *
     * @param callback The callback function to execute after saving.
     */
    public void setOnSaveFileCallback(Runnable callback) {
        this.onSaveFileCallback = callback;
        LoggingHelper.log("Datei hat keine ungespeicherten Änderungen.");
    }

    /**
     * Starts a new file by clearing the canvas and resetting the file reference.
     */
    public void newFile() {
        paintingModel.clearCanvas();
        resetFile();
        LoggingHelper.log("Neues Bild erstellt. \n" +
                DateTimeStamp.time() + ": Keine ungespeicherten Änderungen. \n");
    }

    /**
     * Opens an image file into the drawing panel.
     *
     * @return The loaded image, or {@code null} if cancelled or failed.
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
            LoggingHelper.log("Bild geöffnet: " + currentFile.getName());
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
     *
     * @return {@code true} if the file was saved successfully, otherwise {@code false}.
     */
    public boolean saveFile() {
        if (currentFile == null) {
            LoggingHelper.log("Datei noch nicht vorhanden -> saveFileAs() wird aufgerufen.");
            return saveFileAs();
        }
        return writeFile(currentFile);
    }

    /**
     * Prompts the user to choose a file name and location, then saves the image.
     *
     * @return {@code true} if the file was saved successfully, otherwise {@code false}.
     */
    public boolean saveFileAs() {
        JFileChooser fileChooser = new JFileChooser();
        FileChooserConfigurator.configureFileChooser(fileChooser);

        if (fileChooser.showSaveDialog(parent) != JFileChooser.APPROVE_OPTION) {
            LoggingHelper.log("Speichern abgebrochen. \n");
            return false;
        }

        File selectedFile = ensureValidFileExtension(fileChooser.getSelectedFile(), fileChooser);
        currentFile = selectedFile;
        boolean success = writeFile(currentFile);
        if (success) {
            updateWindowTitle();
        }
        return success;
    }

    /**
     * Gets the currently associated file.     *
     * @return The current file, or {@code null} if none is set.
     */
    public File getCurrentFile() { return currentFile; }

    /**
     * Ensures the file has a valid image extension.
     *
     * @param file         The selected file.
     * @param fileChooser  The file chooser for determining the selected format.
     * @return A file with a proper extension.
     */
    private File ensureValidFileExtension(File file, JFileChooser fileChooser) {
        String fileName = file.getName().toLowerCase();
        String selectedExtension = fileChooser.getFileFilter().getDescription().contains("PNG") ? "png" : "jpg";

        if (!fileName.endsWith(".jpg") && !fileName.endsWith(".jpeg") && !fileName.endsWith(".png")) {
            return new File(file.getParentFile(), file.getName() + "." + selectedExtension);
        }
        return file;
    }

    /**
     * Writes the image data to a file.
     *
     * @param file The file to save to.
     * @return {@code true} if successful, otherwise {@code false}.
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
            if (success) {
                LoggingHelper.log("Speichern erfolgreich!");
                if (onSaveFileCallback != null) {
                    onSaveFileCallback.run();  // Sets 'hasUnsavedChanges = false' in MenuBarController
                }
            } else {
                throw new IOException(DateTimeStamp.time() + ": Fehler beim Speichern des Bildes. \n");
            }

            LoggingHelper.log("Datei " + file.getName() + " gespeichert: " + file.getAbsolutePath() + "\n");
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
     * Updates the window title based on the current file name.
     */
    private void updateWindowTitle() {
        if (parent instanceof JFrame && currentFile != null) {
            ((JFrame) parent).setTitle("BasicPaint | " + currentFile.getName());
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
     * Resets the file reference (e.g., after starting a new file).
     */
    private void resetFile() { this.currentFile = null; }
}
