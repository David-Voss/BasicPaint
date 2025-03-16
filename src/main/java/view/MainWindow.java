package view;

import view.components.*;

import javax.swing.*;
import java.awt.*;

/**
 * The main application window for the Paint Application.
 */
public class MainWindow extends JFrame {

    MenuBarView menuBarView;
    ToolBarView toolBarView;
    PaintingPanelView paintingPanelView;
    StatusBarView statusBarView;

    public MainWindow() {
        super("BasicPaint | Unbenannt");
        setUpUI();

        this.menuBarView = new MenuBarView();
        setJMenuBar(menuBarView);

        this.toolBarView = new ToolBarView();
        add(toolBarView, BorderLayout.NORTH);

        this.paintingPanelView = new PaintingPanelView();
        setUpPaintingPanel();

        this.statusBarView = new StatusBarView();
        add(statusBarView, BorderLayout.SOUTH);

        setVisible(true);
    }

    /**
     * Getter methods for accessing MainWindow components.
     */
    public MenuBarView getMenuBarView() { return menuBarView; }
    public ToolBarView getToolBarView() { return toolBarView; }
    public PaintingPanelView getPaintingPanelView() { return paintingPanelView; }
    public StatusBarView getStatusBarView() { return statusBarView; }

    /**
     * Configures the main window properties.
     */
    private void setUpUI() {
        //setLookAndFeel(); // TODO: Implement function to give user the option to set the look and feel.
        setStartingSize();
        setMinimumSize(new Dimension(1000, 500));
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        centerWindow();
        setLayout(new BorderLayout());
        setApplicationIcon();
    }

    /**
     * Initialises the painting panel within a scroll pane.
     */
    private void setUpPaintingPanel() {
        JScrollPane scrollPane = new JScrollPane(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS
        );
        scrollPane.setViewportView(paintingPanelView);
        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Sets the look and feel to match the system default.
     */
    // TODO: Implement function to give user the option to set the look and feel.
    private void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sets the initial window size based on the screen resolution.
     * <p>
     * The width is set to one-third of the screen width, and the height to half of the screen height.
     * This ensures a reasonable starting size regardless of the screen resolution.
     * </p>
     */
    private void setStartingSize() {
        Dimension startingSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (startingSize.width - getWidth()) / 3;
        int height = (startingSize.height - getHeight()) / 2;
        setSize(width, height);
    }

    /**
     * Centers the window on the screen.
     * <p>
     * This method calculates the screen dimensions and positions the window
     * so that it appears in the centre of the display.
     * </p>
     */
    private void centerWindow() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - getWidth()) / 2;
        int y = (screenSize.height - getHeight()) / 2;
        setLocation(x, y);
    }

    /**
     * Sets the application icon.
     */
    private void setApplicationIcon() {
        int maxSize = (System.getProperty("os.name").toLowerCase().contains("win")) ? 32 : 64;

        ImageIcon icon = new ImageIcon("assets/icons/monkey-with-brush.png");
        Image scaledIcon = icon.getImage().getScaledInstance(maxSize, maxSize, Image.SCALE_SMOOTH);
        setIconImage(scaledIcon);
    }
}