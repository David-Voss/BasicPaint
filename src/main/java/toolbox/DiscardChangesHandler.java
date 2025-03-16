package toolbox;

import view.MainWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.function.BooleanSupplier;

public class DiscardChangesHandler {

    private final MainWindow mainWindow;

    public DiscardChangesHandler(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }

    public boolean confirmDiscardChanges(boolean hasUnsavedChanges, BooleanSupplier saveCallback) {
        if (!hasUnsavedChanges) {
            return true;
        }

        return showDiscardChangesDialog(saveCallback);
    }

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

    private void addKeyboardListeners(JDialog dialog, JOptionPane optionPane) {
        JButton yesButton = null, noButton = null, cancelButton = null;

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

        dialog.addKeyListener(keyListener);
        dialog.setFocusable(true);
        dialog.requestFocusInWindow();

        if (yesButton != null) yesButton.addKeyListener(keyListener);
        if (noButton != null) noButton.addKeyListener(keyListener);
        if (cancelButton != null) cancelButton.addKeyListener(keyListener);
    }

    private boolean handleUserSelection(Object selectedValue, BooleanSupplier saveCallback) {
        if (selectedValue == null || selectedValue.equals("Abbrechen")) {
            LoggingHelper.log("Abbrechen gewählt.");
            return false;
        }
        if (selectedValue.equals("Ja")) {
            LoggingHelper.log("Ja gewählt -> Speichern wird versucht.");

            // Speichern ausführen und Erfolg prüfen
            boolean saveSuccessful = saveCallback.getAsBoolean();

            if (!saveSuccessful) {
                LoggingHelper.log("Speichern wurde abgebrochen oder ist fehlgeschlagen.");
                return false; // Falls Speichern fehlschlägt, den Abbruch weitergeben.
            }

            return true;
        }
        if (selectedValue.equals("Nein")) {
            LoggingHelper.log("Nein gewählt -> Änderungen wurden nicht gespeichert.");
        }
        return true;
    }
}
