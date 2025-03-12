package view;

import view.components.*;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {

    MenuBarView menuBarView;
    ToolBarView toolBarView;
    PaintingPanelView paintingPanelView;
    StatusBarView statusBarView;

    public MainWindow() {
        super("BasicPaint | Unbenannt");
        //setLookAndFeel();
        setStartingSize();
        setMinimumSize(new Dimension(1000, 500));
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        centerWindow();
        setLayout(new BorderLayout());

        setApplicationIcon();

        this.menuBarView = new MenuBarView();
        setJMenuBar(menuBarView);

        this.toolBarView = new ToolBarView();
        add(toolBarView, BorderLayout.NORTH);

        this.paintingPanelView = new PaintingPanelView(); // 33 x 33 cm
        JScrollPane scrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setViewportView(paintingPanelView);
        add(scrollPane, BorderLayout.CENTER);

        this.statusBarView = new StatusBarView();
        add(statusBarView, BorderLayout.SOUTH);

        setVisible(true);
    }

    public MenuBarView getMenuBarView() { return menuBarView; }
    public ToolBarView getToolBarView() { return toolBarView; }
    public PaintingPanelView getPaintingPanelView() { return paintingPanelView; }
    public StatusBarView getStatusBarView() { return statusBarView; }

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

    private void setStartingSize() {
        Dimension startingSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (startingSize.width - getWidth()) / 3;
        int height = (startingSize.height - getHeight()) / 2;
        setSize(width, height);
    }

    private void centerWindow() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - getWidth()) / 2;
        int y = (screenSize.height - getHeight()) / 2;
        setLocation(x, y);
    }

    private void setApplicationIcon() {
        int maxSize = (System.getProperty("os.name").toLowerCase().contains("win")) ? 32 : 64;

        ImageIcon icon = new ImageIcon("assets/icons/monkey-with-brush.png");
        Image scaledIcon = icon.getImage().getScaledInstance(maxSize, maxSize, Image.SCALE_SMOOTH);
        setIconImage(scaledIcon);
    }

}