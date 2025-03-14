package toolbox;

import controller.MainController;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

/**
 * Configures a {@link JFileChooser} to enhance usability.
 * Provides better keyboard navigation and ENTER key handling.
 */
public class FileChooserConfigurator {

    /**
     * Configures the given file chooser.
     * Ensures that the ENTER key can be used to select a file or confirm a button press.
     *
     * @param fileChooser The {@link JFileChooser} to configure.
     */
    public static void configureFileChooser(JFileChooser fileChooser) {
        fileChooser.setFocusable(true);
        fileChooser.requestFocusInWindow();

        setFileChooserImageFilter(fileChooser);

        // Activate "all Files" filter option
        fileChooser.setAcceptAllFileFilterUsed(true);

        // Map ENTER key to either select a file or trigger a focused button
        fileChooser.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
                .put(KeyStroke.getKeyStroke("ENTER"), "confirmSelection");

        fileChooser.getActionMap().put("confirmSelection", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Component focusOwner = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();

                if (focusOwner instanceof JButton) {
                    ((JButton) focusOwner).doClick(); // Simuliert einen Button-Klick
                } else if (focusOwner instanceof JList) {
                    JList<?> fileList = (JList<?>) focusOwner;
                    Object selectedObject = fileList.getSelectedValue();

                    if (selectedObject instanceof File) {
                        File selectedFile = (File) selectedObject;

                        if (selectedFile.isDirectory()) {
                            // ✅ Falls Ordner, navigiere in den Ordner statt ihn zu bestätigen
                            fileChooser.setCurrentDirectory(selectedFile);
                            fileChooser.rescanCurrentDirectory(); // Aktualisiert die Ansicht
                        } else {
                            // ✅ Falls Datei, bestätige die Auswahl
                            fileChooser.setSelectedFile(selectedFile);
                            fileChooser.approveSelection();
                        }
                    }
                }
            }
        });

    }

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