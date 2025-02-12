package view.components;

import javax.swing.*;
import java.awt.*;

public class StatusBarView extends JPanel {
    private final JLabel mousePositionLabel;
    private final JLabel selectionSizeLabel;
    private final JLabel imageSizeLabel;
    private final JComboBox<String> zoomDropdown;
    private final JButton zoomOutButton;
    private final JButton zoomInButton;
    private final JSlider zoomSlider;

    public StatusBarView() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(800, 35)); // Mindesthöhe setzen
        setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        setBackground(new Color(240, 240, 240));

        // 🔹 Linke Seite: Mausposition
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        mousePositionLabel = new JLabel("X: 0, Y: 0");
        leftPanel.add(mousePositionLabel);

        // 🔹 Neue Mitte: Auswahlgröße & Bildgröße
        JPanel combinedCenterPanel = new JPanel(new GridLayout(1, 2, 20, 0)); // 2 Spalten, Abstand 20px
        selectionSizeLabel = new JLabel("Keine Auswahl");
        selectionSizeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageSizeLabel = new JLabel("0 × 0 px");
        imageSizeLabel.setHorizontalAlignment(SwingConstants.CENTER);

        combinedCenterPanel.add(selectionSizeLabel);
        combinedCenterPanel.add(imageSizeLabel);

        // 🔹 Rechte Seite: Zoom-Steuerung
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.X_AXIS));

        zoomOutButton = new JButton("➖");
        zoomDropdown = new JComboBox<>(new String[]{"25%", "50%", "75%", "100%", "150%", "200%", "300%"});
        zoomDropdown.setSelectedItem("100%");
        zoomInButton = new JButton("➕");

        zoomSlider = new JSlider(25, 300, 100);
        zoomSlider.setPreferredSize(new Dimension(100, 20));

        rightPanel.add(zoomOutButton);
        rightPanel.add(Box.createHorizontalStrut(5));
        rightPanel.add(zoomDropdown);
        rightPanel.add(Box.createHorizontalStrut(5));
        rightPanel.add(zoomInButton);
        rightPanel.add(Box.createHorizontalStrut(5));
        rightPanel.add(zoomSlider);

        // Panels zur Statusbar hinzufügen
        add(leftPanel, BorderLayout.WEST);
        add(combinedCenterPanel, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);
    }

    // Getter-Methoden für späteren Controller-Zugriff
    public JLabel getMousePositionLabel() { return mousePositionLabel; }
    public JLabel getSelectionSizeLabel() { return selectionSizeLabel; }
    public JLabel getImageSizeLabel() { return imageSizeLabel; }
    public JComboBox<String> getZoomDropdown() { return zoomDropdown; }
    public JButton getZoomOutButton() { return zoomOutButton; }
    public JButton getZoomInButton() { return zoomInButton; }
    public JSlider getZoomSlider() { return zoomSlider; }
}
