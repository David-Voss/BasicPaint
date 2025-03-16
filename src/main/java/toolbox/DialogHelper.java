package toolbox;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class DialogHelper {

    public enum DialogResult {
        YES,
        NO,
        CANCEL
    }

    /*public static JDialog createDiscardDialog(JFrame parent) {
        // Erstelle eine Option-Pane mit den Buttons "Ja", "Nein" und "Abbrechen"
        String[] options = {"Ja", "Nein", "Abbrechen"};
        JOptionPane optionPane = new JOptionPane(
                "Die aktuelle Zeichnung wurde nicht gespeichert. Bild speichern?",
                JOptionPane.WARNING_MESSAGE,
                JOptionPane.YES_NO_CANCEL_OPTION,
                null,
                options,
                options[0]); // Standard ist "Ja"

        // Erstelle den Dialog
        JDialog dialog = optionPane.createDialog(parent, "Ungespeicherte Änderungen");
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        addCloseHandler(dialog, optionPane);
        addKeyboardShortcuts(dialog, optionPane);
        return dialog;
    }

    private static void addCloseHandler(JDialog dialog, JOptionPane optionPane) {
        // **Schließen mit X = "Abbrechen"**
        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                optionPane.setValue("Abbrechen");
                dialog.setVisible(false);
                dialog.dispose();
            }
        });
    }

    private static void addKeyboardShortcuts(JDialog dialog, JOptionPane optionPane) {
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
    }

    public static Object getSelectedOption(JDialog dialog) {
        JOptionPane optionPane = (JOptionPane) dialog.getContentPane().getComponent(0);
        return optionPane.getValue();
    }*/


    public static DialogResult confirmDiscardChanges(JFrame parent, boolean hasUnsavedChanges) {
        if (!hasUnsavedChanges) {
            return DialogResult.YES; // Keine ungespeicherten Änderungen, direkt fortfahren.
        }

        String[] options = {"Ja", "Nein", "Abbrechen"};
        JOptionPane optionPane = new JOptionPane(
                "Die aktuelle Zeichnung wurde nicht gespeichert. Bild speichern?",
                JOptionPane.WARNING_MESSAGE,
                JOptionPane.YES_NO_CANCEL_OPTION,
                null,
                options,
                options[0]
        );

        JDialog dialog = optionPane.createDialog(parent, "Ungespeicherte Änderungen");
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        addCloseHandler(dialog, optionPane);
        addKeyboardShortcuts(dialog, optionPane);

        dialog.setVisible(true);

        return mapUserSelection(optionPane.getValue());
    }

    private static void addCloseHandler(JDialog dialog, JOptionPane optionPane) {
        dialog.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                optionPane.setValue("Abbrechen");
                dialog.setVisible(false);
                dialog.dispose();
            }
        });
    }

    private static void addKeyboardShortcuts(JDialog dialog, JOptionPane optionPane) {
        JButton[] buttons = extractDialogButtons(optionPane);
        KeyAdapter keyListener = createKeyListener(dialog, optionPane);

        dialog.addKeyListener(keyListener);
        dialog.setFocusable(true);
        dialog.requestFocusInWindow();

        for (JButton button : buttons) {
            if (button != null) {
                button.addKeyListener(keyListener);
            }
        }
    }

    private static JButton[] extractDialogButtons(JOptionPane optionPane) {
        JButton yesButton = null, noButton = null, cancelButton = null;

        for (Component c : optionPane.getComponents()) {
            if (c instanceof JPanel) {
                for (Component btn : ((JPanel) c).getComponents()) {
                    if (btn instanceof JButton) {
                        JButton button = (JButton) btn;
                        switch (button.getText()) {
                            case "Ja" -> yesButton = button;
                            case "Nein" -> noButton = button;
                            case "Abbrechen" -> cancelButton = button;
                        }
                    }
                }
            }
        }

        return new JButton[]{yesButton, noButton, cancelButton};
    }

    private static KeyAdapter createKeyListener(JDialog dialog, JOptionPane optionPane) {
        return new KeyAdapter() {
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
    }

    private static DialogResult mapUserSelection(Object selectedValue) {
        if (selectedValue == null || selectedValue.equals("Abbrechen")) {
            LoggingHelper.log("Abbrechen gewählt.");
            return DialogResult.CANCEL;
        }
        if (selectedValue.equals("Ja")) {
            LoggingHelper.log("Ja gewählt -> Speichervorgang wird eingeleitet.");
            return DialogResult.YES;
        }
        LoggingHelper.log("Nein gewählt -> Änderungen wurden nicht gespeichert.");
        return DialogResult.NO;
    }
}
