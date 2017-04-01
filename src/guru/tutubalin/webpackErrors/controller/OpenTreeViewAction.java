package guru.tutubalin.webpackErrors.controller;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.wm.ToolWindowManager;
import guru.tutubalin.webpackErrors.view.WebpackErrorsToolWindowFactory;

public class OpenTreeViewAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        ToolWindowManager.getInstance(getEventProject(e)).getToolWindow(WebpackErrorsToolWindowFactory.ID).activate(null);
    }
}
