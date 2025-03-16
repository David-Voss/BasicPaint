package controller.components;

import model.ImagePropertiesModel;
import view.components.ImagePropertiesView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;

public class ImagePropertiesController {
    private final ImagePropertiesModel model;
    private final ImagePropertiesView view;
    private boolean confirmed = false;

    public ImagePropertiesController(Frame parent, int width, int height, File imageFile) {

        view = new ImagePropertiesView(parent);

        int initialDpi = getSelectedDpiFromComboBox();
        model = new ImagePropertiesModel(width, height, initialDpi, imageFile);

        view.updateFileInfo(model.getLastModifiedDate(), model.getFileSize());


        // Setze Startwerte
        updateViewFields();

        view.getDpiComboBox().addActionListener(e -> {
            updateDpiAndRecalculate();
            view.revalidate();
            view.repaint();
        });

        // OK-Button speichert die Werte
        view.getOkButton().addActionListener(e -> {
            confirmed = true;
            updateSizeValues();
            view.dispose();
        });

        // **ESC-Taste schließt das Fenster**
        view.getRootPane().registerKeyboardAction(
                e -> view.dispose(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW
        );

        // Abbrechen-Button schließt das Fenster
        view.getCancelButton().addActionListener(e -> view.dispose());

        // Einheit wechseln → Werte sofort umrechnen und aktualisieren
        view.getPixelButton().addActionListener(e -> changeUnit(ImagePropertiesModel.Unit.PIXEL));
        view.getCmButton().addActionListener(e -> changeUnit(ImagePropertiesModel.Unit.CM));
        view.getInchButton().addActionListener(e -> changeUnit(ImagePropertiesModel.Unit.INCH));

        // Standard-Button setzt Werte zurück
        view.getResetButton().addActionListener(e -> resetToDefault());
    }

    private int getSelectedDpiFromComboBox() {
        try {
            return Integer.parseInt(view.getDpiComboBox().getSelectedItem().toString());
        } catch (NumberFormatException e) {
            return 96;
        }
    }

    private void updateDpiAndRecalculate() {
        model.setDpi(getSelectedDpiFromComboBox());
        updateViewFields();
    }

    /**
     * Setzt die Werte auf den Standardwert zurück.
     */
    private void resetToDefault() {
        model.setSizeInPixels(model.getDefaultWidth(), model.getDefaultHeight());
        model.setCurrentUnit(ImagePropertiesModel.Unit.PIXEL);
        updateViewFields();
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
        try {
            // 1️⃣ Aktuelle Werte aus den Eingabefeldern holen (Komma zu Punkt ersetzen)
            double currentWidth = Double.parseDouble(view.getWidthField().getText().replace(",", "."));
            double currentHeight = Double.parseDouble(view.getHeightField().getText().replace(",", "."));

            // 2️⃣ Aktuelle Einheit holen
            ImagePropertiesModel.Unit currentUnit = model.getCurrentUnit();

            // 3️⃣ Werte in Pixel umrechnen (immer auf ganze Zahlen runden!)
            int pixelWidth = model.convertUnitToPixels(currentWidth, currentUnit);
            int pixelHeight = model.convertUnitToPixels(currentHeight, currentUnit);

            // 4️⃣ Pixelwerte im Modell speichern (verhindert Akkumulation von Rundungsfehlern)
            model.setSizeInPixels(pixelWidth, pixelHeight);

            // 5️⃣ Neue Einheit setzen und Werte konvertieren
            model.convertUnit(newUnit);

            // 6️⃣ UI-Felder mit den umgerechneten Werten aktualisieren
            updateViewFields();
        } catch (NumberFormatException ignored) {
            System.out.println("Ungültige Eingabe! Bitte nur Zahlen eingeben.");
        }
    }


    /**
     * Speichert die neuen Werte in der aktuellen Einheit und konvertiert sie in Pixel.
     */
    private void updateSizeValues() {
        try {
            double newWidth = Double.parseDouble(view.getWidthField().getText().replace(",", "."));
            double newHeight = Double.parseDouble(view.getHeightField().getText().replace(",", "."));

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
