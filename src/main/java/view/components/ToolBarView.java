package view.components;

import javax.swing.*;
import java.awt.*;

public class ToolBarView extends JPanel {
    private final JButton pencilButton;
    private final JButton rectangleButton;
    private final JButton circleButton;
    private final JButton colorPickerButton;

    public ToolBarView() {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setPreferredSize(new Dimension(800, 40));

        pencilButton = new JButton("✏");
        rectangleButton = new JButton("⬛");
        circleButton = new JButton("⚫");
        colorPickerButton = new JButton("🎨");

        add(pencilButton);
        add(rectangleButton);
        add(circleButton);
        add(colorPickerButton);
    }

    public JButton getPencilButton() { return pencilButton; }
    public JButton getRectangleButton() { return rectangleButton; }
    public JButton getCircleButton() { return circleButton; }
    public JButton getColorPickerButton() { return colorPickerButton; }
}
