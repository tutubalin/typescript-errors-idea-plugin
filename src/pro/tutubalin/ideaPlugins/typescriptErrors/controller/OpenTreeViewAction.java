package pro.tutubalin.ideaPlugins.typescriptErrors.controller;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindowManager;
import pro.tutubalin.ideaPlugins.typescriptErrors.view.PluginToolWindow;

public class OpenTreeViewAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = getEventProject(e);
        
        if (project != null) {
            ToolWindowManager.getInstance(project).getToolWindow(PluginToolWindow.ID).activate(null);
        }
    }
}
