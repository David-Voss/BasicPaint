package view.components;

import toolbox.CreateImageButton;
import toolbox.PaintingTool;

import javax.swing.*;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import java.awt.*;

public class ToolBarView extends JPanel {
    private final JComboBox<String> brushSizeDropdown;

    private final JPanel toolsPanel;

    private final JToggleButton pencilButton;
    private final JToggleButton eraserButton;

    private final JPanel paintingToolPanel;
    private final ButtonGroup paintingToolButtonGroup;
    private final JToggleButton lineButton;
    private final JToggleButton ellipseButton;
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
        this.paintingToolButtonGroup = new ButtonGroup();
        this.pencilButton = CreateImageButton.createToggleButton("assets/icons/pencil-solid.png", "Bleistift [P]");

        this.pencilButton.setSelected(true);
        this.eraserButton = CreateImageButton.createToggleButton("assets/icons/eraser-solid.png", "Radierer [E]");

        add(toolsPanel);
        paintingToolButtonGroup.add(pencilButton);
        paintingToolButtonGroup.add(eraserButton);
        toolsPanel.add(pencilButton);
        toolsPanel.add(eraserButton);

        addSeparator();

        this.paintingToolPanel = new JPanel();

        this.lineButton = CreateImageButton.createToggleButton("assets/icons/line-regular.png", "Linie");
        this.ellipseButton = CreateImageButton.createToggleButton("assets/icons/circle-regular.png","Ellipse");
        this.rectangleButton = CreateImageButton.createToggleButton("assets/icons/square-full-regular.png", "Rechteck");

        add(paintingToolPanel);
        paintingToolButtonGroup.add(lineButton);
        paintingToolButtonGroup.add(ellipseButton);
        paintingToolButtonGroup.add(rectangleButton);
        paintingToolPanel.add(lineButton);
        paintingToolPanel.add(ellipseButton);
        paintingToolPanel.add(rectangleButton);

        addSeparator();

        this.colourPanel = setUpColourPanel();

        add(colourPanel);
    }

    public JComboBox<String> getBrushSizeDropdown() { return brushSizeDropdown; }
    public ButtonGroup getPaintingToolButtonGroup() { return paintingToolButtonGroup; }
    public JToggleButton getPencilButton() { return pencilButton; }
    public JToggleButton getEraserButton() { return eraserButton; }
    public JToggleButton getLineButton() { return lineButton; }
    public JToggleButton getEllipseButton() { return ellipseButton; }
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

    public PaintingTool getSelectedTool() {
        if (pencilButton.isSelected() && lineButton.isSelected()) return PaintingTool.LINE;
        if (pencilButton.isSelected() && ellipseButton.isSelected()) return PaintingTool.ELLIPSE;
        if (pencilButton.isSelected() && rectangleButton.isSelected()) return PaintingTool.RECTANGLE;

        if (pencilButton.isSelected()) return PaintingTool.PENCIL;
        if (lineButton.isSelected()) return PaintingTool.LINE;
        if (eraserButton.isSelected()) return PaintingTool.ERASER;
        if (ellipseButton.isSelected()) return PaintingTool.ELLIPSE;
        if (rectangleButton.isSelected()) return PaintingTool.RECTANGLE;
        return null;
    }
}
