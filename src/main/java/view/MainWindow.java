package view;

import view.components.MenuBarView;
import view.components.PaintingAreaView;
import view.components.StatusBarView;
import view.components.ToolBarView;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {

    MenuBarView menuBarView;
    ToolBarView toolBarView;
    PaintingAreaView paintingAreaView;
    StatusBarView statusBarView;

    public MainWindow() {
        super("BasicPaint");
        setStartingSize();
        setMinimumSize(new Dimension(700, 500));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        centerWindow();
        setLayout(new BorderLayout());

        this.menuBarView = new MenuBarView();
        setJMenuBar(menuBarView);

        this.toolBarView = new ToolBarView();
        add(toolBarView, BorderLayout.NORTH);

        this.paintingAreaView = new PaintingAreaView(1500, 1200);
        JScrollPane scrollPane = new JScrollPane(paintingAreaView);
        add(scrollPane, BorderLayout.CENTER);

        this.statusBarView = new StatusBarView();
        add(statusBarView, BorderLayout.SOUTH);

        setVisible(true);
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
}