package controller.components;

import view.components.ToolBarView;

public class ToolBarController {
    private final ToolBarView toolBarView;

    public ToolBarController(ToolBarView toolBarView) {
        this.toolBarView = toolBarView;
    }

    public ToolBarView getToolBarView() { return toolBarView; }
}
