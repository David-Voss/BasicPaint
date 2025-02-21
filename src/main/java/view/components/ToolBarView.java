package view.components;

import javax.swing.*;
import java.awt.*;

public class ToolBarView extends JPanel {
    private final JComboBox<String> brushSizeDropdown;
    private final JButton pencilButton;
    private final JButton rectangleButton;
    private final JButton circleButton;
    private final JButton colorPickerButton;

    public ToolBarView() {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setPreferredSize(new Dimension(800, 40));

        brushSizeDropdown = new JComboBox<>(new String[]{"1px", "2px", "5px", "10px", "20px"});
        //add(new JLabel("Pinsel: "));
        pencilButton = new JButton("✏");
        rectangleButton = new JButton("⬛");
        circleButton = new JButton("⚫");
        colorPickerButton = new JButton("🎨 Farbe wählen");

        add(new JLabel("Pinsel: "));
        add(brushSizeDropdown);
        add(pencilButton);
        add(rectangleButton);
        add(circleButton);
        add(colorPickerButton);
    }

    public JComboBox<String> getBrushSizeDropdown() { return brushSizeDropdown; }
    public JButton getPencilButton() { return pencilButton; }
    public JButton getRectangleButton() { return rectangleButton; }
    public JButton getCircleButton() { return circleButton; }
    public JButton getColorPickerButton() { return colorPickerButton; }
}
