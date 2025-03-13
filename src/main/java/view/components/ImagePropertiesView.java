package view.components;

import javax.swing.*;
import java.awt.*;

public class ImagePropertiesView extends JDialog {
    private JComboBox<String> dpiComboBox;
    private Label lastModifiedLabel;
    private Label fileSizeLabel;
    private JTextField widthField, heightField;
    private JRadioButton pixelButton, cmButton, inchButton;
    private JButton okButton, cancelButton, resetButton;

    public ImagePropertiesView(Frame parent) {
        super(parent, "Bildeigenschaften", true);

        setPreferredSize(new Dimension(475,435));
        setMinimumSize(new Dimension(475,435));
        setMaximumSize(new Dimension(475,435));

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 1;
        add(new JLabel("Dateiattribute"), gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        add(new Label("Zuletzt gespeichert:"), gbc);
        gbc.gridx++;
        lastModifiedLabel = new Label("Nicht verfügbar");
        add(lastModifiedLabel, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        add(new Label("Größe auf Datenträger:"), gbc);
        gbc.gridx++;
        fileSizeLabel = new Label("Nicht verfügbar");
        add(fileSizeLabel, gbc);

        gbc.gridy++;
        gbc.gridx = 0; gbc.gridwidth = 4;
        add(new JSeparator(), gbc);

        gbc.gridy++; gbc.gridwidth = 1;
        add(new JLabel("Einheiten:"), gbc);

        ButtonGroup unitGroup = new ButtonGroup();
        pixelButton = new JRadioButton("Pixel (px)", true);
        cmButton = new JRadioButton("Zentimeter (cm)");
        inchButton = new JRadioButton("Zoll (in)");

        pixelButton.setFont(pixelButton.getFont().deriveFont(Font.PLAIN));
        cmButton.setFont(cmButton.getFont().deriveFont(Font.PLAIN));
        inchButton.setFont(inchButton.getFont().deriveFont(Font.PLAIN));

        unitGroup.add(pixelButton);
        unitGroup.add(cmButton);
        unitGroup.add(inchButton);

        gbc.gridy++;
        gbc.insets = new Insets(1, 4, 1, 4);
        add(pixelButton, gbc);
        gbc.gridy++;
        add(cmButton, gbc);
        gbc.gridy++;
        add(inchButton, gbc);

        gbc.insets = new Insets(4, 4, 4, 4);

        gbc.gridy++;
        gbc.gridx = 0; gbc.gridwidth = 4;
        add(new JSeparator(), gbc);

        gbc.gridy++;
        gbc.gridx = 0; gbc.gridwidth = 1;
        add(new Label("Auflösung:"), gbc);

        gbc.gridx++;
        JPanel comboBoxPanel = new JPanel();
        String[] dpiOptions = {"72", "96", "150", "300", "600", "1200"};
        dpiComboBox = new JComboBox<>(dpiOptions);
        dpiComboBox.setEditable(true); // Benutzer kann eigene Werte eingeben
        dpiComboBox.setSelectedItem("96");
        dpiComboBox.setToolTipText("Wähle die DPI (Dots per Inch) zur korrekten Berechnung der Bildgröße in cm / in. \n" +
                "[Standardeinstellung = 96 DPI]");
        comboBoxPanel.add(dpiComboBox);
        comboBoxPanel.add(new Label("DPI"));
        add(comboBoxPanel, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        add(new Label("Breite:"), gbc);

        gbc.gridx++;
        add(new Label("Höhe:"), gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        widthField = new JTextField(10);
        add(widthField, gbc);

        gbc.gridx++;
        heightField = new JTextField(10);
        add(heightField, gbc);

        // NEUER Standard-Button für das Zurücksetzen
        gbc.gridx++; gbc.gridwidth = 2;
        resetButton = new JButton("Standard");
        add(resetButton, gbc);

        gbc.gridy++;
        gbc.gridx = 0; gbc.gridwidth = 4;
        add(new JSeparator(), gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        JPanel buttonPanel = new JPanel(new GridLayout(1,2,4,4));

        okButton = new JButton("OK");
        buttonPanel.add(okButton, gbc);

        cancelButton = new JButton("Abbrechen");
        buttonPanel.add(cancelButton, gbc);

        add(buttonPanel, gbc);

        pack();
        setLocationRelativeTo(parent);
    }

    public JComboBox<String> getDpiComboBox() { return dpiComboBox; }
    public JTextField getWidthField() { return widthField; }
    public JTextField getHeightField() { return heightField; }
    public JRadioButton getPixelButton() { return pixelButton; }
    public JRadioButton getCmButton() { return cmButton; }
    public JRadioButton getInchButton() { return inchButton; }
    public JButton getOkButton() { return okButton; }
    public JButton getCancelButton() { return cancelButton; }
    public JButton getResetButton() { return resetButton; }

    public void updateFileInfo(String lastModified, String fileSize) {
        lastModifiedLabel.setText(lastModified);
        fileSizeLabel.setText(fileSize);
    }
}
