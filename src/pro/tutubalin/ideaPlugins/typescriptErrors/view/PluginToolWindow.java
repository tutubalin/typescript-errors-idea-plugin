package pro.tutubalin.ideaPlugins.typescriptErrors.view;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import pro.tutubalin.ideaPlugins.typescriptErrors.controller.PluginController;
import pro.tutubalin.ideaPlugins.typescriptErrors.model.ErrorGroup;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class PluginToolWindow implements ToolWindowFactory {

    private JPanel panelContent;
    private JButton btnUpdate;
    private JList<ErrorGroup> list;
    private JButton btnBack;

    private PluginController controller;

    public static final String ID = "Webpack Errors";

    private DefaultListModel<ErrorGroup> listModel;
    private ToolWindow toolWindow;
    private DefaultTableModel tableModel;


    public void displayErrorGroup(ErrorGroup errorGroup) {

        listModel.clear();
        errorGroup.getChildren().forEach(item -> listModel.addElement(item));

        //errorGroup.getChildren().forEach(item -> tableModel.addRow(new Object[]{item.getTitle(), item.getCount()}));

        btnBack.setEnabled(errorGroup.getParent() != null);

        toolWindow.setTitle(errorGroup.getTitle() + " - " + errorGroup.getCount());
    }

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull com.intellij.openapi.wm.ToolWindow toolWindow) {
        this.toolWindow = toolWindow;

        listModel = new DefaultListModel<>();
        list.setModel(listModel);
        list.setCellRenderer(new ErrorGroupCellRenderer());

        /*tableModel = new DefaultTableModel(new String[]{"Error", "Count"}, 20);
        table.setModel(tableModel); */

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
