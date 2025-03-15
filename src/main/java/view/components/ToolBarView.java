package view.components;

import toolbox.CreateIcon;
import toolbox.PaintingTool;

import javax.swing.*;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents the toolbar component of the application.
 * Provides tools for drawing, shape selection, and colour selection.
 */
public class ToolBarView extends JPanel {
    private JComboBox<String> brushSizeSelector;

    private ButtonGroup paintingToolsButtonGroup;

    private JPanel toolsPanel;
    private JToggleButton pencilButton;
    private JToggleButton fillButton;
    private JToggleButton eraserButton;
    private JToggleButton magnifierButton;

    private JPanel shapeToolsPanel;
    private JToggleButton lineButton;
    private JToggleButton ellipseButton;
    private JToggleButton rectangleButton;

    private JPanel colourPanel;
    private JButton colourChooserButton;
    private JColorChooser colourChooser;

    private Map<JToggleButton, PaintingTool> toolMapping = new HashMap<>();

    /**
     * Constructs a new toolbar view and initialises all components.
     */
    public ToolBarView() {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setPreferredSize(new Dimension(1200, 125));

        this.paintingToolsButtonGroup = new ButtonGroup();

        initBrushSizeSelector();
        initToolsPanel();
        initShapeToolsPanel();
        initColourPanel();
        initToolMapping();
    }

    /**
     * Retrieves the currently selected painting tool.
     * @return The selected PaintingTool, or null if none is selected.
     */
    public PaintingTool getSelectedTool() {
        return toolMapping.entrySet()
                .stream()
                .filter(entry -> entry.getKey().isSelected())
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(null);
    }

    /**
     * Getter of ToolBarView
     */
    public JComboBox<String> getBrushSizeSelector() { return brushSizeSelector; }

    public ButtonGroup getPaintingToolsButtonGroup() { return paintingToolsButtonGroup; }

    public JToggleButton getPencilButton() { return pencilButton; }
    public JToggleButton getFillButton() { return fillButton; }
    public JToggleButton getEraserButton() { return eraserButton; }
    public JToggleButton getMagnifierButton() { return magnifierButton; }

    public JToggleButton getLineButton() { return lineButton; }
    public JToggleButton getEllipseButton() { return ellipseButton; }
    public JToggleButton getRectangleButton() { return rectangleButton; }

    public JPanel getColourPanel() { return colourPanel; }
    public JButton getColourChooserButton() { return colourChooserButton; }
    public JColorChooser getColourChooser() { return colourChooser; }
    public Color getSelectedColour() { return colourChooser.getColor(); }

    /**
     * Initialises the brush size selector, allowing users to choose different brush thicknesses.
     */
    private void initBrushSizeSelector() {
        this.brushSizeSelector = new JComboBox<>(new String[]{"1 px", "2 px", "3 px", "5 px", "10 px", "20 px", "30 px", "50 px", "100 px", "150 px", "200 px", "250 px"});
        this.brushSizeSelector.setSelectedItem("3 px");
        this.brushSizeSelector.setToolTipText("Strichstärke | Kleiner [Q] / Größer [W]");

        add(new JLabel(CreateIcon.loadIcon("assets/icons/pencil-solid.png", 15, 15)));
        add(new JLabel(" / "));
        add(new JLabel(CreateIcon.loadIcon("assets/icons/eraser-solid.png",15,15)));
        add(brushSizeSelector);

        addSeparator();
    }

    /**
     * Initialises the painting tools panel, providing buttons for different painting functionalities.
     */
    private void initToolsPanel() {
        this.toolsPanel = new JPanel(new GridLayout(2,3,4,4));

        this.pencilButton = CreateIcon.createToggleButton("assets/icons/pencil-solid.png", "Bleistift [P]");
        this.pencilButton.setSelected(true);
        this.fillButton = CreateIcon.createToggleButton("assets/icons/fill-drip-solid.png","Füllen [B]");
        this.eraserButton = CreateIcon.createToggleButton("assets/icons/eraser-solid.png", "Radierer [E]");
        this.magnifierButton = CreateIcon.createToggleButton("assets/icons/magnifying-glass-solid.png","Lupe [Z]");

        paintingToolsButtonGroup.add(pencilButton);
        paintingToolsButtonGroup.add(fillButton);
        paintingToolsButtonGroup.add(eraserButton);
        paintingToolsButtonGroup.add(magnifierButton);

        toolsPanel.add(pencilButton);
        toolsPanel.add(fillButton);
        toolsPanel.add(eraserButton);
        toolsPanel.add(magnifierButton);

        add(toolsPanel);
        addSeparator();
    }

    /**
     * Initialises the shape selection panel, allowing users to draw predefined shapes.
     */
    private void initShapeToolsPanel() {
        this.shapeToolsPanel = new JPanel();

        this.lineButton = CreateIcon.createToggleButton("assets/icons/line-regular.png", "Linie [L]");
        this.ellipseButton = CreateIcon.createToggleButton("assets/icons/circle-regular.png","Ellipse [O]");
        this.rectangleButton = CreateIcon.createToggleButton("assets/icons/square-full-regular.png", "Rechteck [R]");

        paintingToolsButtonGroup.add(lineButton);
        paintingToolsButtonGroup.add(ellipseButton);
        paintingToolsButtonGroup.add(rectangleButton);

        shapeToolsPanel.add(lineButton);
        shapeToolsPanel.add(ellipseButton);
        shapeToolsPanel.add(rectangleButton);

        add(shapeToolsPanel);

        addSeparator();
    }

    /**
     * Initialises the colour selection panel, enabling users to choose colours for painting.
     */
    private void initColourPanel() {
        this.colourPanel = setUpColourPanel();

        // TODO: The current colour selection in the toolbar should also show the selected colour from the colour chooser called up via the colourChooserButton.
        /*this.colourChooserButton = CreateIcon.createButton("assets/icons/palette-solid.png","Farbe");
        colourChooserButton.setSelected(false);

        add(colourChooserButton);*/

        add(colourPanel);
    }

    /**
     * Creates a panel containing the colour chooser.
     * @return A JPanel containing the colour selection tool.
     */
    private JPanel setUpColourPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.add(setUpColourChooser());
        return panel;
    }

    /**
     * Sets up the JColorChooser, configuring its UI for compact display.
     * @return A configured JColorChooser instance.
     */
    private JColorChooser setUpColourChooser() {
        colourChooser = new JColorChooser();
        colourChooser.setPreviewPanel(new JPanel()); // Remove preview panel to save space

        for (AbstractColorChooserPanel panel : colourChooser.getChooserPanels()) {
            if (!panel.getDisplayName().equals("Swatches")) { // Keep  only "Swatches"
                colourChooser.removeChooserPanel(panel);
            }
        }

        colourChooser.setPreferredSize(new Dimension(425, 110)); // Adjusted size for better fit

        return colourChooser;
    }

    /**
     * Maps the painting tools to their respective buttons.
     */
    private void initToolMapping() {
        toolMapping.put(pencilButton, PaintingTool.PENCIL);
        toolMapping.put(fillButton, PaintingTool.FILL);
        toolMapping.put(eraserButton, PaintingTool.ERASER);
        toolMapping.put(magnifierButton, PaintingTool.MAGNIFIER);
        toolMapping.put(lineButton, PaintingTool.LINE);
        toolMapping.put(ellipseButton, PaintingTool.ELLIPSE);
        toolMapping.put(rectangleButton, PaintingTool.RECTANGLE);
    }

    /**
     * Adds a visual separator to the toolbar for better UI organisation.
     */
    private void addSeparator() {
        add(Box.createHorizontalStrut(2)); // Adds spacing
        JSeparator separator = new JSeparator(SwingConstants.VERTICAL);
        separator.setPreferredSize(new Dimension(2, 100)); // Set width & height
        add(separator);
        add(Box.createHorizontalStrut(2)); // Adds spacing
    }
}
