package controller.components;

import model.ImagePropertiesModel;
import view.components.ImagePropertiesView;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ImagePropertiesController {
    private final ImagePropertiesModel model;
    private final ImagePropertiesView view;
    private boolean confirmed = false;

    public ImagePropertiesController(Frame parent, int width, int height, int dpi) {
        model = new ImagePropertiesModel(width, height, dpi);
        view = new ImagePropertiesView(parent);

        // Setze Startwerte
        updateViewFields();

        // OK-Button speichert die Werte
        view.getOkButton().addActionListener(e -> {
            confirmed = true;
            updateSizeValues();
            view.dispose();
        });

        // Abbrechen-Button schließt das Fenster
        view.getCancelButton().addActionListener(e -> view.dispose());

        // Einheit wechseln → Werte sofort umrechnen und aktualisieren
        view.getPixelButton().addActionListener(e -> changeUnit(ImagePropertiesModel.Unit.PIXEL));
        view.getCmButton().addActionListener(e -> changeUnit(ImagePropertiesModel.Unit.CM));
        view.getInchButton().addActionListener(e -> changeUnit(ImagePropertiesModel.Unit.INCH));
    }

    /**
     * Aktualisiert die Eingabefelder mit den umgerechneten Werten.
     */
    private void updateViewFields() {
        double width = model.getWidthInCurrentUnit();
        double height = model.getHeightInCurrentUnit();

        // Falls die Einheit Pixel ist → Ganzzahl anzeigen (ohne Komma)
        if (model.getCurrentUnit() == ImagePropertiesModel.Unit.PIXEL) {
            view.getWidthField().setText(String.valueOf((int) width));
            view.getHeightField().setText(String.valueOf((int) height));
        } else {
            // Falls cm oder Zoll → Zwei Nachkommastellen anzeigen
            view.getWidthField().setText(String.format("%.2f", width));
            view.getHeightField().setText(String.format("%.2f", height));
        }

        // Richtige Einheit im UI auswählen
        switch (model.getCurrentUnit()) {
            case PIXEL:
                view.getPixelButton().setSelected(true);
                break;
            case CM:
                view.getCmButton().setSelected(true);
                break;
            case INCH:
                view.getInchButton().setSelected(true);
                break;
        }
    }

    /**
     * Ändert die Maßeinheit, konvertiert die Werte und aktualisiert das UI.
     */
    private void changeUnit(ImagePropertiesModel.Unit newUnit) {
        model.convertToUnit(newUnit);
        updateViewFields();
    }

    /**
     * Speichert die neuen Werte in der aktuellen Einheit und konvertiert sie in Pixel.
     */
    private void updateSizeValues() {
        try {
            double newWidth = Double.parseDouble(view.getWidthField().getText());
            double newHeight = Double.parseDouble(view.getHeightField().getText());

            model.setSizeInCurrentUnit(newWidth, newHeight);
            model.convertToPixels(); // Speichert Werte immer als Pixel für interne Berechnungen
        } catch (NumberFormatException ignored) {
            System.out.println("Ungültige Eingabe!");
        }
    }

    public void showDialog() {
        view.setVisible(true);
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public int getImageWidth() {
        return model.getWidth();
    }

    public int getImageHeight() {
        return model.getHeight();
    }
}
