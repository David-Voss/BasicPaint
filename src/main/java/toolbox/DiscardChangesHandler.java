package toolbox;

import view.MainWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.function.BooleanSupplier;

/**
 * Handles user confirmation when discarding unsaved changes.
 */
public class DiscardChangesHandler {

    private final MainWindow mainWindow;

    /**
     * Constructs the DiscardChangesHandler.
     *
     * @param mainWindow The main application window.
     */
    public DiscardChangesHandler(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }

    /**
     * Asks the user whether to discard unsaved changes.
     *
     * @param hasUnsavedChanges {@code true} if there are unsaved changes.
     * @param saveCallback      Callback to attempt saving the changes.
     * @return {@code true} if changes can be discarded, otherwise {@code false}.
     */
    public boolean confirmDiscardChanges(boolean hasUnsavedChanges, BooleanSupplier saveCallback) {
        if (!hasUnsavedChanges) {
            return true;
        }

        return showDiscardChangesDialog(saveCallback);
    }

    /**
     * Displays a confirmation dialog for unsaved changes.
     *
     * @param saveCallback Callback for saving before discarding changes.
     * @return {@code true} if the user confirms discarding changes.
     */
    private boolean showDiscardChangesDialog(BooleanSupplier saveCallback) {
        String[] options = {"Ja", "Nein", "Abbrechen"};
        JOptionPane optionPane = new JOptionPane(
                "Die aktuelle Zeichnung wurde nicht gespeichert. Bild speichern?",
                JOptionPane.WARNING_MESSAGE,
                JOptionPane.YES_NO_CANCEL_OPTION,
                null,
                options,
                options[0]);

        JDialog dialog = optionPane.createDialog(mainWindow, "Ungespeicherte Änderungen");
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        addWindowCloseListener(dialog, optionPane);
        addKeyboardListeners(dialog, optionPane);

        dialog.setVisible(true);

        return handleUserSelection(optionPane.getValue(), saveCallback);
    }

    /**
     * Adds a listener to handle window close events.
     *
     * @param dialog     The dialog to add the listener to.
     * @param optionPane The associated option pane.
     */
    private void addWindowCloseListener(JDialog dialog, JOptionPane optionPane) {
        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                optionPane.setValue("Abbrechen");
                dialog.setVisible(false);
                dialog.dispose();
            }
        });
    }

    /**
     * Adds keyboard listeners to enable interaction via the keyboard.
     * <p>
     * - The <b>Enter</b> key triggers the currently focused button.
     * - The <b>Escape</b> key selects "Abbrechen" (Cancel) and closes the dialog.
     * </p>
     *
     * @param dialog     The dialog to which the listeners will be added.
     * @param optionPane The JOptionPane containing the buttons.
     */
    private void addKeyboardListeners(JDialog dialog, JOptionPane optionPane) {
        JButton yesButton = null, noButton = null, cancelButton = null;

        // Find buttons inside the JOptionPane
        for (Component c : optionPane.getComponents()) {
            if (c instanceof JPanel) {
                for (Component btn : ((JPanel) c).getComponents()) {
                    if (btn instanceof JButton) {
                        JButton button = (JButton) btn;
                        if (button.getText().equals("Ja")) yesButton = button;
                        if (button.getText().equals("Nein")) noButton = button;
                        if (button.getText().equals("Abbrechen")) cancelButton = button;
                    }
                }
            }
        }

        // Key listener to handle Enter and Escape keys
        KeyAdapter keyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    Component focusOwner = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
                    if (focusOwner instanceof JButton) {
                        ((JButton) focusOwner).doClick();
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    optionPane.setValue("Abbrechen");
                    dialog.setVisible(false);
                    dialog.dispose();
                }
            }
        };

        // Apply listeners to dialog and buttons
        dialog.addKeyListener(keyListener);
        dialog.setFocusable(true);
        dialog.requestFocusInWindow();

        if (yesButton != null) yesButton.addKeyListener(keyListener);
        if (noButton != null) noButton.addKeyListener(keyListener);
        if (cancelButton != null) cancelButton.addKeyListener(keyListener);
    }

    /**
     * Handles the user's selection from the dialog.
     *
     * @param selectedValue The selected option.
     * @param saveCallback  The callback function to save changes.
     * @return {@code true} if changes can be discarded, otherwise {@code false}.
     */
    private boolean handleUserSelection(Object selectedValue, BooleanSupplier saveCallback) {
        if (selectedValue == null || selectedValue.equals("Abbrechen")) {
            LoggingHelper.log("Abbrechen gewählt. \n");
            return false;
        }
        if (selectedValue.equals("Ja")) {
            LoggingHelper.log("Ja gewählt -> Speichern wird versucht.");

            // Execute save and check success
            boolean isSaveSuccessful = saveCallback.getAsBoolean();

            if (!isSaveSuccessful) {
                LoggingHelper.log("Speichern abgebrochen oder fehlgeschlagen. \n");
                return false;
            }

            return true;
        }
        if (selectedValue.equals("Nein")) {
            LoggingHelper.log("Nein gewählt -> Änderungen wurden nicht gespeichert. \n");
        }
        return true;
    }
}