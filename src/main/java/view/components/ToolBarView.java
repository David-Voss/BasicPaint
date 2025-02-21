package view.components;

import javax.swing.*;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import java.awt.*;

public class ToolBarView extends JPanel {
    private final JComboBox<String> brushSizeDropdown;
    private final JButton pencilButton;
    private final JButton rectangleButton;
    private final JButton circleButton;

    private final JPanel colourPanel;
    private JColorChooser colourChooser;

    public ToolBarView() {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setPreferredSize(new Dimension(1200, 125));

        brushSizeDropdown = new JComboBox<>(new String[]{"1px", "2px", "5px", "10px", "20px"});
        //add(new JLabel("Pinsel: "));
        pencilButton = new JButton("✏");
        rectangleButton = new JButton("⬛");
        circleButton = new JButton("⚫");

        add(new JLabel("Pinsel: "));
        add(brushSizeDropdown);
        add(pencilButton);
        add(rectangleButton);
        add(circleButton);

        this.colourPanel = new JPanel(new FlowLayout());
        colourChooser = new JColorChooser();
        colourChooser.setPreviewPanel(new JPanel()); // Remove preview panel to save space
        for (AbstractColorChooserPanel panel : colourChooser.getChooserPanels()) {
            if (!panel.getDisplayName().equals("Swatches")) { // Nur "Swatches" behalten
                colourChooser.removeChooserPanel(panel);
            }
        }
        colourChooser.setPreferredSize(new Dimension(425, 110)); // Adjusted size for better fit
        colourPanel.add(colourChooser);
        add(colourPanel, BorderLayout.SOUTH);
    }

    public JComboBox<String> getBrushSizeDropdown() { return brushSizeDropdown; }
    public JButton getPencilButton() { return pencilButton; }
    public JButton getRectangleButton() { return rectangleButton; }
    public JButton getCircleButton() { return circleButton; }
    public JPanel getColourPanel() { return colourPanel; }
    public JColorChooser getColourChooser() { return colourChooser; }
    public Color getColour() { return colourChooser.getColor(); }
}
