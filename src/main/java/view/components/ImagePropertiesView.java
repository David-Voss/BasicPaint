package view.components;

import javax.swing.*;
import java.awt.*;

public class ImagePropertiesView extends JDialog {
    private JTextField widthField, heightField;
    private JRadioButton pixelButton, cmButton, inchButton;
    private JButton okButton, cancelButton;

    public ImagePropertiesView(Frame parent) {
        super(parent, "Bildeigenschaften", true);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(new JLabel("Auflösung: 96 DPI"), gbc);

        gbc.gridy++;
        add(new JSeparator(), gbc);

        gbc.gridy++; gbc.gridwidth = 1;
        add(new JLabel("Einheiten:"), gbc);

        ButtonGroup unitGroup = new ButtonGroup();
        pixelButton = new JRadioButton("Pixel", true);
        cmButton = new JRadioButton("Zentimeter");
        inchButton = new JRadioButton("Zoll");

        unitGroup.add(pixelButton);
        unitGroup.add(cmButton);
        unitGroup.add(inchButton);

        gbc.gridy++;
        gbc.gridx = 0; add(pixelButton, gbc);
        gbc.gridx = 1; add(cmButton, gbc);
        gbc.gridy++; gbc.gridx = 1; add(inchButton, gbc);

        gbc.gridy++; gbc.gridx = 0;
        add(new JLabel("Breite:"), gbc);
        gbc.gridx = 1;
        widthField = new JTextField(10);
        add(widthField, gbc);

        gbc.gridy++; gbc.gridx = 0;
        add(new JLabel("Höhe:"), gbc);
        gbc.gridx = 1;
        heightField = new JTextField(10);
        add(heightField, gbc);

        gbc.gridy++; gbc.gridx = 0;
        okButton = new JButton("OK");
        add(okButton, gbc);

        gbc.gridx = 1;
        cancelButton = new JButton("Abbrechen");
        add(cancelButton, gbc);

        pack();
        setLocationRelativeTo(parent);
    }

    public JTextField getWidthField() { return widthField; }
    public JTextField getHeightField() { return heightField; }
    public JRadioButton getPixelButton() { return pixelButton; }
    public JRadioButton getCmButton() { return cmButton; }
    public JRadioButton getInchButton() { return inchButton; }
    public JButton getOkButton() { return okButton; }
    public JButton getCancelButton() { return cancelButton; }
}
