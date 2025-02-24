package controller.toolbox;

import javax.swing.*;
import java.awt.*;

public class CreateImageButton {

    /**
     * Creates a button / toggle button with an icon and tooltip.
     *
     * @param iconPath The path to the icon file.
     * @param tooltip  The tooltip text for the button.
     * @return The created JButton.
     */
    public static JButton createButton(String iconPath, String tooltip) {
        JButton button = new JButton(loadIcon(iconPath, 15, 15));
        button.setToolTipText(tooltip);
        return button;
    }
    public static JToggleButton createToggleButton(String iconPath, String tooltip) {
        JToggleButton button = new JToggleButton(loadIcon(iconPath, 15, 15));
        button.setToolTipText(tooltip);
        return button;
    }

    /**
     * Loads and scales an icon to the specified dimensions.
     *
     * @param path  The file path of the icon.
     * @param width The desired width.
     * @param height The desired height.
     * @return A scaled {@link ImageIcon}.
     */
    public static ImageIcon loadIcon(String path, int width, int height) {
        ImageIcon icon = new ImageIcon(path);
        Image scaledImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }
}
