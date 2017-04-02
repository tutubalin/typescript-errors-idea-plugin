package guru.tutubalin.webpackErrors.view;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import guru.tutubalin.webpackErrors.controller.PluginController;
import guru.tutubalin.webpackErrors.model.ErrorGroup;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class PluginToolWindow implements ToolWindowFactory {

    private JPanel panelContent;
    private JButton btnUpdate;
    private JList<ErrorGroup> list;
    private JButton btnBack;

    private PluginController controller;

    public static final String ID = "Webpack Errors";

    private DefaultListModel<ErrorGroup> listModel;
    private ToolWindow toolWindow;


    public void displayErrorGroup(ErrorGroup errorGroup) {

        listModel.clear();
        errorGroup.getChildren().forEach(item -> listModel.addElement(item));

        btnBack.setEnabled(errorGroup.getParent() != null);

        toolWindow.setTitle(errorGroup.getTitle() + " - " + errorGroup.getCount());
    }

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull com.intellij.openapi.wm.ToolWindow toolWindow) {
        this.toolWindow = toolWindow;

        listModel = new DefaultListModel<>();
        list.setModel(listModel);

        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(panelContent, "", false);
        toolWindow.getContentManager().addContent(content);

        controller = new PluginController(this);
        initListeners();
    }

    private void initListeners() {
        list.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                ListSelectionModel lsm = (ListSelectionModel) e.getSource();

                if (!lsm.isSelectionEmpty()) {
                    controller.itemClicked(list.getSelectedValue());
                }
            }
        });

        btnUpdate.addActionListener(e -> controller.loadFile());

        btnBack.addActionListener(e -> controller.back());

    }
}
