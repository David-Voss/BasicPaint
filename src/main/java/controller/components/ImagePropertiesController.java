package controller.components;

import model.ImagePropertiesModel;
import toolbox.LoggingHelper;
import view.components.ImagePropertiesView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

/**
 * Controller for handling image property settings and user interactions.
 */
public class ImagePropertiesController {
    private final ImagePropertiesModel model;
    private final ImagePropertiesView view;
    private boolean confirmed = false;

    /**
     * Constructs an ImagePropertiesController.
     *
     * @param parent  The parent frame for the dialog.
     * @param width   The initial image width in pixels.
     * @param height  The initial image height in pixels.
     * @param imageFile The associated image file.
     */
    public ImagePropertiesController(Frame parent, int width, int height, File imageFile) {
        view = new ImagePropertiesView(parent);
        int initialDpi = getSelectedDpiFromComboBox();
        model = new ImagePropertiesModel(width, height, initialDpi, imageFile);

        view.updateFileInfo(model.getLastModifiedDate(), model.getFileSize());
        updateViewFields(); // Set initial values in UI

        setupEventListeners();
        setupKeyboardShortcuts();
    }

    /**
     * Displays the image properties dialog.
     */
    public void showDialog() {
        view.setVisible(true);
    }

    /**
     * Checks whether the user confirmed the changes.
     *
     * @return {@code true} if the OK button was pressed, otherwise {@code false}.
     */
    public boolean isConfirmed() {
        return confirmed;
    }

    /**
     * Retrieves the final image width in pixels.
     *
     * @return The width in pixels.
     */
    public int getImageWidth() {
        return model.getWidth();
    }

    /**
     * Retrieves the final image height in pixels.
     *
     * @return The height in pixels.
     */
    public int getImageHeight() {
        return model.getHeight();
    }

    /**
     * Configures all event listeners for UI components.
     */
    private void setupEventListeners() {
        view.getDpiComboBox().addActionListener(e -> updateDpiAndRecalculate());

        // OK button saves values and closes the dialog
        view.getOkButton().addActionListener(e -> {
            confirmed = true;
            updateSizeValues();
            view.dispose();
        });

        // Cancel button closes the dialog
        view.getCancelButton().addActionListener(e -> view.dispose());

        // Unit change buttons convert and update values
        view.getPixelButton().addActionListener(e -> changeUnit(ImagePropertiesModel.Unit.PIXEL));
        view.getCmButton().addActionListener(e -> changeUnit(ImagePropertiesModel.Unit.CM));
        view.getInchButton().addActionListener(e -> changeUnit(ImagePropertiesModel.Unit.INCH));

        // Reset button restores default values
        view.getResetButton().addActionListener(e -> resetToDefault());
    }

    /**
     * Configures keyboard shortcuts and focus behaviours.
     */
    private void setupKeyboardShortcuts() {
        // ESC closes the window
        view.getRootPane().registerKeyboardAction(
                e -> view.dispose(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW
        );

        // ENTER confirms the focused element (button, combo box etc.)
        view.getRootPane().registerKeyboardAction(
                e -> {
                    Component focusOwner = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
                    if (focusOwner instanceof JButton) {
                        ((JButton) focusOwner).doClick();
                    } else if (focusOwner instanceof JComboBox) {
                        ((JComboBox<?>) focusOwner).showPopup();
                    }
                },
                KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW
        );

        // Automatically select all text when entering a text field
        FocusListener selectAllOnFocus = new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (e.getComponent() instanceof JTextField textField) {
                    SwingUtilities.invokeLater(textField::selectAll);
                }
            }
        };

        view.getWidthField().addFocusListener(selectAllOnFocus);
        view.getHeightField().addFocusListener(selectAllOnFocus);
    }

    /**
     * Retrieves the selected DPI value from the combo box.
     * Returns 96 DPI if parsing fails or no selection is made.
     *
     * @return The selected DPI value.
     */
    private int getSelectedDpiFromComboBox() {
        try {
            Object selectedItem = view.getDpiComboBox().getSelectedItem();
            return (selectedItem != null) ? Integer.parseInt(selectedItem.toString()) : 96;
        } catch (NumberFormatException e) {
            return 96;
        }
    }

    /**
     * Updates the DPI value in the model and recalculates the image dimensions.
     */
    private void updateDpiAndRecalculate() {
        model.setDpi(getSelectedDpiFromComboBox());
        updateViewFields();
    }

    /**
     * Resets the values to their default settings.
     */
    private void resetToDefault() {
        model.setSizeInPixels(model.getDefaultWidth(), model.getDefaultHeight());
        model.setCurrentUnit(ImagePropertiesModel.Unit.PIXEL);
        updateViewFields();
    }

    /**
     * Updates the input fields with the recalculated values based on the selected unit.
     */
    private void updateViewFields() {
        double width = model.getWidthInCurrentUnit();
        double height = model.getHeightInCurrentUnit();

        // Display integer values for pixels, floating point for cm/inch
        if (model.getCurrentUnit() == ImagePropertiesModel.Unit.PIXEL) {
            view.getWidthField().setText(String.valueOf((int) width));
            view.getHeightField().setText(String.valueOf((int) height));
        } else {
            // If cm or in → Show two decimal places
            view.getWidthField().setText(String.format("%.2f", width));
            view.getHeightField().setText(String.format("%.2f", height));
        }

        // Set the correct unit selection in the UI
        switch (model.getCurrentUnit()) {
            case PIXEL -> view.getPixelButton().setSelected(true);
            case CM -> view.getCmButton().setSelected(true);
            case INCH -> view.getInchButton().setSelected(true);
        }
    }

    /**
     * Changes the measurement unit, converts values accordingly, and updates the UI.
     *
     * @param newUnit The new unit to be applied.
     */
    private void changeUnit(ImagePropertiesModel.Unit newUnit) {
        try {
            // Get current values from input fields (replace comma with period
            double currentWidth = Double.parseDouble(view.getWidthField().getText().replace(",", "."));
            double currentHeight = Double.parseDouble(view.getHeightField().getText().replace(",", "."));

            ImagePropertiesModel.Unit currentUnit = model.getCurrentUnit();

            int pixelWidth = model.convertUnitToPixels(currentWidth, currentUnit);
            int pixelHeight = model.convertUnitToPixels(currentHeight, currentUnit);

            model.setSizeInPixels(pixelWidth, pixelHeight);

            model.convertUnit(newUnit);

            updateViewFields();
        } catch (NumberFormatException ignored) {
            LoggingHelper.log("Ungültige Eingabe: Bitte nur Zahlen eingeben. \n" );
        }
    }

    /**
     * Stores the new size values in the model and converts them to pixels.
     */
    private void updateSizeValues() {
        try {
            double newWidth = Double.parseDouble(view.getWidthField().getText().replace(",", "."));
            double newHeight = Double.parseDouble(view.getHeightField().getText().replace(",", "."));

            model.setSizeInCurrentUnit(newWidth, newHeight);
            model.convertToPixels();
        } catch (NumberFormatException ignored) {
            LoggingHelper.log("Ungültige Eingabe: Bitte nur Zahlen eingeben. \n" );
        }
    }
}
