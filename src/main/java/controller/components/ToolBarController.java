package controller.components;

import controller.MainController;
import toolbox.DateTimeStamp;
import view.MainWindow;
import view.components.PaintingPanelView;
import view.components.ToolBarView;
import model.PaintingModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class ToolBarController {
    private final MainWindow mainWindow;
    private final MainController mainController;
    private final ToolBarView toolBarView;
    private final PaintingPanelView paintingPanelView;
    private final PaintingModel paintingModel;

    private boolean zoomDialogShown = false;

    public ToolBarController(MainWindow mainWindow, MainController mainController) {
        this.mainWindow = mainWindow;
        this.mainController = mainController;
        this.toolBarView = mainWindow.getToolBarView();
        this.paintingPanelView = mainWindow.getPaintingPanelView();
        this.paintingModel = paintingPanelView.getPaintingModel();

        initialiseToolBarFunctions();
    }

    public ToolBarView getToolBarView() { return toolBarView; }

        public void initialiseToolBarFunctions() {
        JComponent mainWindowRootPane = mainWindow.getRootPane();

            //  Decrease / Increase brush size
            registerKeyBinding(mainWindowRootPane, KeyEvent.VK_Q, this::decreaseBrushSize);
            registerKeyBinding(mainWindowRootPane, KeyEvent.VK_W, this::increaseBrushSize);

            // Select painting tool
            registerKeyBinding(mainWindowRootPane, KeyEvent.VK_P, () -> toolBarView.getPencilButton().doClick());
            registerKeyBinding(mainWindowRootPane, KeyEvent.VK_B, () -> toolBarView.getFillButton().doClick());
            registerKeyBinding(mainWindowRootPane, KeyEvent.VK_E, () -> toolBarView.getEraserButton().doClick());
            registerKeyBinding(mainWindowRootPane, KeyEvent.VK_Z, () -> {
                toolBarView.getMagnifierButton().doClick();
                if (!zoomDialogShown) {
                    JOptionPane.showMessageDialog(mainWindow,
                            "Die Zoomfunktion konnte leider noch nicht zufriedenstellend implementiert werden. \n" +
                                    "Sie enthält daher noch keine Funktionen \n\n" +
                                    "Diese Meldung wird in dieser Sitzung kein weiteres Mal angezeigt.",
                            "Hinweis: Funktion noch nicht vorhanden",
                            JOptionPane.INFORMATION_MESSAGE);
                    zoomDialogShown = true;
                }
            });

            // Select Shape tool
            registerKeyBinding(mainWindowRootPane, KeyEvent.VK_L, () -> toolBarView.getLineButton().doClick());
            registerKeyBinding(mainWindowRootPane, KeyEvent.VK_O, () -> toolBarView.getEllipseButton().doClick());
            registerKeyBinding(mainWindowRootPane, KeyEvent.VK_R, () -> toolBarView.getRectangleButton().doClick());

            toolBarView.getBrushSizeSelector().addActionListener(e -> {
                updateStrokeWidth();
                ensureFocus();
            });

            toolBarView.getColourChooser().getSelectionModel().addChangeListener(e -> {
                changeColour();
                ensureFocus();
            });

            // To be implemented
            //toolBarView.getColourChooserButton().addActionListener(e -> openColourChooser());
        }

    /**
     * Registriert eine Tastenkombination für eine beliebige Swing-Komponente.
     *
     * @param component Die Swing-Komponente, auf der die Taste registriert wird (z. B. `getRootPane()`).
     * @param keyCode   Der KeyEvent-Keycode (z. B. KeyEvent.VK_ESCAPE für ESC).
     * @param action    Die Aktion, die bei Tastendruck ausgeführt wird.
     */
    private void registerKeyBinding(JComponent component, int keyCode, Runnable action) {
        component.registerKeyboardAction(
                e -> {
                    action.run();
                    ensureFocus();
                },
                KeyStroke.getKeyStroke(keyCode, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW
        );
    }

    private void ensureFocus() {
        SwingUtilities.invokeLater(() -> paintingPanelView.requestFocusInWindow());
    }

    private void increaseBrushSize() {
        JComboBox<String> brushSizeDropdown = toolBarView.getBrushSizeSelector(); // Zugriff auf die ComboBox
        int currentIndex = brushSizeDropdown.getSelectedIndex();

        if (currentIndex < brushSizeDropdown.getItemCount() - 1) {
            brushSizeDropdown.setSelectedIndex(currentIndex + 1); // Nächste größere Größe wählen
            paintingPanelView.repaint();
        }
    }

    private void decreaseBrushSize() {
        JComboBox<String> brushSizeDropdown = toolBarView.getBrushSizeSelector();
        int currentIndex = brushSizeDropdown.getSelectedIndex();

        if (currentIndex > 0) {
            brushSizeDropdown.setSelectedIndex(currentIndex - 1); // Nächste kleinere Größe wählen
            paintingPanelView.repaint();
        }
    }

    private void updateStrokeWidth() {
        String selectedValue = (String) toolBarView.getBrushSizeSelector().getSelectedItem();

        if(selectedValue != null) {
            paintingModel.setStrokeWidth(Integer.parseInt(selectedValue.replace(" px", "")));
            System.out.println(timeStamp() + ": Neue Pinselbreite: " + paintingModel.getStrokeWidth() + " px");
        }
    }

    // To be implemented
    /*private void openColourChooser() {
        Color initialColour = paintingModel.getCurrentColour(); // Aktuelle Farbe holen
        Color selectedColour = JColorChooser.showDialog(mainWindow, "Farbe auswählen", initialColour);

        if (selectedColour != null) {
            paintingModel.setCurrentColour(selectedColour);
            toolBarView.getColourChooser().setColor(selectedColour);
            System.out.println(timeStamp() + ": Neue Farbe ausgewählt: " + selectedColour);
        }

        ensureFocus(); // Stellt sicher, dass das Zeichenfeld weiter aktiv ist
    }*/


    private void changeColour() {
        Color newColour = toolBarView.getSelectedColour();
        if (newColour != null) {
            paintingModel.setCurrentColour(newColour);
        }
    }

    private String timeStamp() {
        return DateTimeStamp.time();
    }
}
