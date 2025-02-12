package controller.toolbox;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

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

        // Map ENTER key to either select a file or trigger a focused button
        fileChooser.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
                .put(KeyStroke.getKeyStroke("ENTER"), "confirmSelection");

        fileChooser.getActionMap().put("confirmSelection", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Component focusOwner = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();

                if (focusOwner instanceof JButton) {
                    ((JButton) focusOwner).doClick(); // Simulates button click
                } else if (focusOwner instanceof JList) {
                    fileChooser.approveSelection(); // Confirms file selection
                }
            }
        });
    }
}