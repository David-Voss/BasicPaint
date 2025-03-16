package view.components;

import toolbox.CreateIcon;

import javax.swing.*;
import java.awt.*;

/**
 * A status bar panel that displays image properties, mouse position, and zoom controls.
 */
public class StatusBarView extends JPanel {
    private JLabel mousePositionLabel;
    private JLabel imageSizeLabel;
    private JComboBox<String> zoomDropdown;
    private JButton zoomOutButton;
    private JButton zoomInButton;
    private JSlider zoomSlider;

    /**
     * Constructs the status bar view with predefined UI elements.
     */
    public StatusBarView() {
        setUpStatusBar();
    }

    /**
     * Getter methods for accessing StatusBar components.
     */
    public JLabel getMousePositionLabel() { return mousePositionLabel; }
    public JLabel getImageSizeLabel() { return imageSizeLabel; }
    public JComboBox<String> getZoomDropdown() { return zoomDropdown; }
    public JButton getZoomOutButton() { return zoomOutButton; }
    public JButton getZoomInButton() { return zoomInButton; }
    public JSlider getZoomSlider() { return zoomSlider; }

    /**
     * Initializes and configures the status bar layout and UI components.
     */
    private void setUpStatusBar() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(800, 35)); // Mindesthöhe setzen
        setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        add(createMousePositionPanel(), BorderLayout.WEST);
        add(createImageSizePanel(), BorderLayout.CENTER);
        // add(createZoomControlPanel(), BorderLayout.EAST); //TODO: Uncomment when zoom is implemented.
    }

    /**
     * Creates the left panel displaying the mouse position.
     *
     * @return The constructed panel.
     */
    private JPanel createMousePositionPanel() {
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        mousePositionLabel = new JLabel("X: 0, Y: 0");
        mousePositionLabel.setToolTipText("Position des Mauszeigers.");
        leftPanel.add(mousePositionLabel);
        return leftPanel;
    }

    /**
     * Creates the center panel displaying image size information.
     *
     * @return The constructed panel.
     */
    private JPanel createImageSizePanel() {
        JPanel centerPanel = new JPanel(new GridLayout(1, 1, 20, 0));
        imageSizeLabel = new JLabel("0 × 0 px");
        imageSizeLabel.setToolTipText("Aktuelle Größe der Zeichenfläche in Pixeln");
        imageSizeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        centerPanel.add(imageSizeLabel);
        return centerPanel;
    }

    //TODO: Use when Zoom function is implemented.
    /**
     * Creates the right panel containing zoom controls.
     * This method is currently not in use but serves as a placeholder for future functionality.
     *
     * @return The constructed panel.
     */
    private JPanel createZoomControlPanel() {
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.X_AXIS));

        zoomOutButton = CreateIcon.createButton("assets/icons/minus-solid.png","Verkleinern");
        zoomDropdown = new JComboBox<>(new String[]{"25 %", "50 %", "75 %", "100 %", "150 %", "200 %", "300 %"});
        zoomDropdown.setToolTipText("Zoomstufe");
        zoomDropdown.setSelectedItem("100 %");
        zoomInButton = CreateIcon.createButton("assets/icons/plus-solid.png","Vergrößern");

        zoomSlider = new JSlider(25, 300, 100);
        zoomSlider.setPreferredSize(new Dimension(100, 20));

        rightPanel.add(zoomOutButton);
        rightPanel.add(Box.createHorizontalStrut(5));
        rightPanel.add(zoomDropdown);
        rightPanel.add(Box.createHorizontalStrut(5));
        rightPanel.add(zoomInButton);
        rightPanel.add(Box.createHorizontalStrut(5));
        rightPanel.add(zoomSlider);

        return rightPanel;
    }
}
