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

/**
 * Controller for managing interactions with the ToolBarView.
 * Handles user inputs, tool selection, and event handling.
 */
public class ToolBarController {
    private final MainWindow mainWindow;
    private final MainController mainController;
    private final ToolBarView toolBarView;
    private final PaintingPanelView paintingPanelView;
    private final PaintingModel paintingModel;

    private boolean isZoomDialogShown = false;

    /**
     * Constructs a ToolBarController and binds the toolbar components.
     * @param mainWindow The main application window.
     * @param mainController The main application controller.
     */
    public ToolBarController(MainWindow mainWindow, MainController mainController) {
        this.mainWindow = mainWindow;
        this.mainController = mainController;
        this.toolBarView = mainWindow.getToolBarView();
        this.paintingPanelView = mainWindow.getPaintingPanelView();
        this.paintingModel = paintingPanelView.getPaintingModel();

        initToolBarFunctions();
    }

    /**
     * Initializes toolbar functions such as key bindings and action listeners.
     */
    private void initToolBarFunctions() {
        registerKeyBindings();
        registerActionListeners();
    }

    /**
     * Registers key bindings for various toolbar functions.
     */
    private void registerKeyBindings() {
        JComponent mainWindowRootPane = mainWindow.getRootPane();

        registerBrushSizeKeyBindings(mainWindowRootPane);
        registerToolKeyBindings(mainWindowRootPane);
        registerShapeToolKeyBindings(mainWindowRootPane);
    }

    /**
     * Registers key bindings for adjusting the brush size.
     * @param component The component on which key bindings are registered.
     */
    private void registerBrushSizeKeyBindings(JComponent component) {
        registerKeyBinding(component, KeyEvent.VK_Q, this::decreaseBrushSize);
        registerKeyBinding(component, KeyEvent.VK_W, this::increaseBrushSize);
    }

    /**
     * Registers key bindings for tool selection.
     * @param component The component on which key bindings are registered.
     */
    private void registerToolKeyBindings(JComponent component) {
        registerKeyBinding(component, KeyEvent.VK_P, () -> toolBarView.getPencilButton().doClick());
        registerKeyBinding(component, KeyEvent.VK_B, () -> toolBarView.getFillButton().doClick());
        registerKeyBinding(component, KeyEvent.VK_E, () -> toolBarView.getEraserButton().doClick());
        registerKeyBinding(component, KeyEvent.VK_Z, this::showZoomDialog);
    }

    /**
     * Registers key bindings for shape tool selection.
     * @param component The component on which key bindings are registered.
     */
    private void registerShapeToolKeyBindings(JComponent component) {
        registerKeyBinding(component, KeyEvent.VK_L, () -> toolBarView.getLineButton().doClick());
        registerKeyBinding(component, KeyEvent.VK_O, () -> toolBarView.getEllipseButton().doClick());
        registerKeyBinding(component, KeyEvent.VK_R, () -> toolBarView.getRectangleButton().doClick());
    }

    /**
     * Displays a message dialog when the magnifier tool is selected.
     */
    private void showZoomDialog() {
        toolBarView.getMagnifierButton().doClick();
        if (!isZoomDialogShown) {
            JOptionPane.showMessageDialog(mainWindow,
                    "Die Zoomfunktion konnte leider noch nicht zufriedenstellend implementiert werden. \n" +
                            "Sie enthält daher noch keine Funktionen \n\n" +
                            "Diese Meldung wird in dieser Sitzung kein weiteres Mal angezeigt.",
                    "Hinweis: Funktion noch nicht vorhanden",
                    JOptionPane.INFORMATION_MESSAGE);
            isZoomDialogShown = true;
        }
    }

    /**
     * Registers action listeners for UI components.
     */
    private void registerActionListeners() {
        toolBarView.getBrushSizeSelector().addActionListener(e -> {
            updateStrokeWidth();
            ensureFocus();
        });

        toolBarView.getColourChooser().getSelectionModel().addChangeListener(e -> {
            changeColour();
            ensureFocus();
        });

        // TODO: The current colour selection in the toolbar should also show the selected colour from the colour chooser called up via the colourChooserButton.
        //toolBarView.getColourChooserButton().addActionListener(e -> openColourChooser());
    }

    /**
     * Ensures that the painting panel remains focused after user actions.
     */
    private void ensureFocus() {
        SwingUtilities.invokeLater(() -> {
            if (!paintingPanelView.isFocusOwner()) {
                paintingPanelView.requestFocusInWindow();
            }
        });
    }

    /**
     * Increases the brush size by selecting the next available option in the dropdown.
     */
    private void increaseBrushSize() {
        JComboBox<String> brushSizeDropdown = toolBarView.getBrushSizeSelector(); // Zugriff auf die ComboBox
        int currentIndex = brushSizeDropdown.getSelectedIndex();

        if (currentIndex < brushSizeDropdown.getItemCount() - 1) {
            brushSizeDropdown.setSelectedIndex(currentIndex + 1); // Nächste größere Größe wählen
            paintingPanelView.repaint();
        }
    }

    /**
     * Decreases the brush size by selecting the previous available option in the dropdown.
     */
    private void decreaseBrushSize() {
        JComboBox<String> brushSizeDropdown = toolBarView.getBrushSizeSelector();
        int currentIndex = brushSizeDropdown.getSelectedIndex();

        if (currentIndex > 0) {
            brushSizeDropdown.setSelectedIndex(currentIndex - 1); // Nächste kleinere Größe wählen
            paintingPanelView.repaint();
        }
    }

    /**
     * Updates the stroke width based on the selected brush size.
     */
    private void updateStrokeWidth() {
        String selectedValue = (String) toolBarView.getBrushSizeSelector().getSelectedItem();

        if (selectedValue != null) {
            try {
                paintingModel.setStrokeWidth(Integer.parseInt(selectedValue.replace(" px", "")));
                System.out.println(timeStamp() + ": Neue Pinselbreite: " + paintingModel.getStrokeWidth() + " px \n");
            } catch (NumberFormatException e) {
                System.err.println(timeStamp() + ": Fehler beim Parsen der Pinselgröße: " + selectedValue + "\n");
            }
        }
    }

    // TODO: The current colour selection in the toolbar should also show the selected colour from the colour chooser called up via the colourChooserButton.
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

    /**
     * Changes the currently selected colour in the painting model.
     */
    private void changeColour() {
        Color newColour = toolBarView.getSelectedColour();
        if (newColour != null) {
            paintingModel.setCurrentColour(newColour);
        }
    }

    /**
     * Registers a key binding to a specific action.
     * @param component The component to register the key binding on.
     * @param keyCode The key code of the binding.
     * @param action The action to be performed when the key is pressed.
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

    /**
     * Retrieves the current timestamp in string format.
     * @return The current timestamp as a formatted string.
     */
    private String timeStamp() {
        return DateTimeStamp.time();
    }
}
