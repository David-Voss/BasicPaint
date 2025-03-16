package toolbox;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Utility class for creating buttons with icons.
 */
public class CreateIcon {

    private static final int defaultIconSize = 15;

    /**
     * Creates a button with an icon and tooltip.
     *
     * @param iconPath The path to the icon file.
     * @param tooltip  The tooltip text for the button.
     * @return A configured {@link JButton}.
     */
    public static JButton createButton(String iconPath, String tooltip) {
        return (JButton) createButtonComponent(new JButton(), iconPath, tooltip);
    }

    /**
     * Creates a toggle button with an icon and tooltip.
     *
     * @param iconPath The path to the icon file.
     * @param tooltip  The tooltip text for the toggle button.
     * @return A configured {@link JToggleButton}.
     */
    public static JToggleButton createToggleButton(String iconPath, String tooltip) {
        return (JToggleButton) createButtonComponent(new JToggleButton(), iconPath, tooltip);
    }

    /**
     * Loads and scales an icon to the specified dimensions.
     * If the file is not found, a default placeholder icon is returned.
     *
     * @param path   The file path of the icon.
     * @param width  The desired width.
     * @param height The desired height.
     * @return A scaled {@link ImageIcon}, or a default icon if loading fails.
     */
    public static ImageIcon loadIcon(String path, int width, int height) {
        File file = new File(path);
        if (!file.exists()) {
            LoggingHelper.log("Hinweis: Icon nicht gefunden -> " + path);
            return getDefaultIcon(width, height);
        }

        ImageIcon icon = new ImageIcon(path);
        Image scaledImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }

    /**
     * Creates a button component (JButton or JToggleButton) with an icon and tooltip.
     *
     * @param button   The button to configure.
     * @param iconPath The path to the icon file.
     * @param tooltip  The tooltip text for the button.
     * @return The configured button.
     */
    private static AbstractButton createButtonComponent(AbstractButton button, String iconPath, String tooltip) {
        button.setIcon(loadIcon(iconPath, defaultIconSize, defaultIconSize));
        button.setToolTipText(tooltip);
        return button;
    }

    /**
     * Returns a default placeholder icon if the specified icon is not found.
     *
     * @param width  The desired width.
     * @param height The desired height.
     * @return A default grey box icon.
     */
    private static ImageIcon getDefaultIcon(int width, int height) {
        BufferedImage placeholder = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = placeholder.createGraphics();
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.fillRect(0, 0, width, height);
        g2d.setColor(Color.DARK_GRAY);
        g2d.drawRect(0, 0, width - 1, height - 1);
        g2d.dispose();
        return new ImageIcon(placeholder);
    }
}
