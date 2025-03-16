package toolbox;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

/**
 * Configures a {@link JFileChooser} to enhance usability.
 * <p>
 * - Enables keyboard navigation with the ENTER key
 * - Automatically adds image file filters
 * - Supports seamless navigation into directories
 * </p>
 */
public class FileChooserConfigurator {

    /**
     * Configures the given file chooser.
     * <p>
     * - Adds standard image file filters
     * - Enables keyboard shortcuts for improved usability
     * - Ensures ENTER selects a file or navigates into directories
     * </p>
     *
     * @param fileChooser The {@link JFileChooser} to configure.
     */
    public static void configureFileChooser(JFileChooser fileChooser) {
        if (fileChooser == null) {
            throw new IllegalArgumentException(LoggingHelper.formatMessage("FileChooser kann nicht 'null' sein. \n"));
        }

        fileChooser.setFocusable(true);
        fileChooser.requestFocusInWindow();
        fileChooser.setAcceptAllFileFilterUsed(true);

        setFileChooserImageFilter(fileChooser);
        configureEnterKeyHandling(fileChooser);
    }

    /**
     * Configures the ENTER key behaviour for selecting files and navigating directories.
     *
     * @param fileChooser The file chooser to enhance.
     */
    private static void configureEnterKeyHandling(JFileChooser fileChooser) {
        fileChooser.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
                .put(KeyStroke.getKeyStroke("ENTER"), "confirmSelection");

        fileChooser.getActionMap().put("confirmSelection", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Component focusOwner = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();

                if (focusOwner instanceof JButton) {
                    ((JButton) focusOwner).doClick(); // Simulate button click
                } else if (focusOwner instanceof JList) {
                    JList<?> fileList = (JList<?>) focusOwner;
                    Object selectedObject = fileList.getSelectedValue();

                    if (selectedObject instanceof File) {
                        File selectedFile = (File) selectedObject;

                        if (selectedFile.isDirectory()) {
                            // Navigate into the selected directory instead of confirming it
                            fileChooser.setCurrentDirectory(selectedFile);
                            fileChooser.rescanCurrentDirectory();
                        } else {
                            // Confirm file selection
                            fileChooser.setSelectedFile(selectedFile);
                            fileChooser.approveSelection();
                        }
                    }
                }
            }
        });
    }

    /**
     * Adds predefined file filters for image selection.
     *
     * @param fileChooser The file chooser to configure.
     */
    private static void setFileChooserImageFilter(JFileChooser fileChooser) {
        // Generate file filters
        FileNameExtensionFilter jpgFilter = new FileNameExtensionFilter("JPEG Bilder (*.jpg, *.jpeg)", "jpg", "jpeg");
        FileNameExtensionFilter pngFilter = new FileNameExtensionFilter("PNG Bilder (*.png)", "png");
        FileNameExtensionFilter allImagesFilter = new FileNameExtensionFilter("Alle unterstützten Bilder (*.jpg, *.jpeg, *.png)", "jpg", "jpeg", "png");

        // Add file filters to fileChooser
        fileChooser.addChoosableFileFilter(jpgFilter);
        fileChooser.addChoosableFileFilter(pngFilter);
        fileChooser.addChoosableFileFilter(allImagesFilter);
        fileChooser.setFileFilter(allImagesFilter);
    }
}