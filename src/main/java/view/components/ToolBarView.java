package view.components;

import controller.toolbox.CreateImageButton;

import javax.swing.*;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import java.awt.*;

public class ToolBarView extends JPanel {
    private final JComboBox<String> brushSizeDropdown;

    private final JPanel toolsPanel;

    private final JToggleButton pencilButton;
    private final JToggleButton eraserButton;

    private final JPanel graphicElementsPanel;
    //private final JButton circleButton;
    private final JToggleButton rectangleButton;

    private final JPanel colourPanel;
    private JColorChooser colourChooser;

    public ToolBarView() {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setPreferredSize(new Dimension(1200, 125));

        this.brushSizeDropdown = new JComboBox<>(new String[]{"1 px", "2 px", "3 px", "5 px", "10 px", "20 px", "30 px", "50 px", "100 px", "150 px", "200 px", "250 px"});
        this.brushSizeDropdown.setSelectedItem("3 px");

        add(new JLabel("Pinsel: "));
        add(brushSizeDropdown);

        addSeparator();

        this.toolsPanel = new JPanel();
        this.pencilButton = CreateImageButton.createToggleButton("assets/icons/pencil-solid.png", "Bleistift [P]");
        this.pencilButton.setSelected(true);
        this.eraserButton = CreateImageButton.createToggleButton("assets/icons/eraser-solid.png", "Radierer [E]");

        add(toolsPanel);
        toolsPanel.add(pencilButton);
        toolsPanel.add(eraserButton);
        //add(circleButton);

        addSeparator();

        this.graphicElementsPanel = new JPanel();
        this.rectangleButton = CreateImageButton.createToggleButton("assets/icons/square-full-regular.png", "Rechteck");

        add(graphicElementsPanel);
        graphicElementsPanel.add(rectangleButton);

        addSeparator();

        this.colourPanel = setUpColourPanel();

        add(colourPanel);
    }

    public JComboBox<String> getBrushSizeDropdown() { return brushSizeDropdown; }
    public JToggleButton getPencilButton() { return pencilButton; }
    public JToggleButton getEraserButton() { return eraserButton; }
    //public JButton getCircleButton() { return circleButton; }
    public JToggleButton getRectangleButton() { return rectangleButton; }
    public JPanel getColourPanel() { return colourPanel; }
    public JColorChooser getColourChooser() { return colourChooser; }
    public Color getColour() { return colourChooser.getColor(); }

    public JPanel setUpColourPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.add(setUpColourChooser());
        return panel;
    }

    public JColorChooser setUpColourChooser() {
        colourChooser = new JColorChooser();
        colourChooser.setPreviewPanel(new JPanel()); // Remove preview panel to save space
        for (AbstractColorChooserPanel panel : colourChooser.getChooserPanels()) {
            if (!panel.getDisplayName().equals("Swatches")) { // Nur "Swatches" behalten
                colourChooser.removeChooserPanel(panel);
            }
        }
        colourChooser.setPreferredSize(new Dimension(425, 110)); // Adjusted size for better fit
        return colourChooser;
    }

    private void addSeparator() {
        add(Box.createHorizontalStrut(2)); // Adds spacing
        JSeparator separator = new JSeparator(SwingConstants.VERTICAL);
        separator.setPreferredSize(new Dimension(2, 100)); // Set width & height
        add(separator);
        add(Box.createHorizontalStrut(2)); // Adds spacing
    }
}
