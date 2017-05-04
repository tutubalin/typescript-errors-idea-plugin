package pro.tutubalin.ideaPlugins.typescriptErrors.view;

import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;
import pro.tutubalin.ideaPlugins.typescriptErrors.controller.PluginController;
import pro.tutubalin.ideaPlugins.typescriptErrors.model.ErrorGroup;

import javax.swing.*;
import javax.swing.table.*;

public class PluginToolWindow implements ToolWindowFactory, DumbAware {

    private JPanel panelContent;
    private JButton btnUpdate;
    private JButton btnBack;
    private JTable table;

    private PluginController controller;

    public static final String ID = "TypeScript Errors";

    private ToolWindow toolWindow;
    private ErrorGroupTableModel tableModel;


    public void displayErrorGroup(ErrorGroup errorGroup) {

        tableModel.setErrorGroup(errorGroup);

        btnBack.setEnabled(errorGroup.getParent() != null);
        toolWindow.setTitle(errorGroup.getTitle() + " - " + errorGroup.getCount());

        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        DefaultTableCellRenderer rightAlignedRenderer = new DefaultTableCellRenderer();
        rightAlignedRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        table.getColumnModel().getColumn(1).setCellRenderer(rightAlignedRenderer);
    }

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull com.intellij.openapi.wm.ToolWindow toolWindow) {
        this.toolWindow = toolWindow;

        tableModel = new ErrorGroupTableModel();
        table.setModel(tableModel);

        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        DefaultTableCellRenderer rightAlignedRenderer = new DefaultTableCellRenderer();
        rightAlignedRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        table.getColumnModel().getColumn(1).setCellRenderer(rightAlignedRenderer);

        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(panelContent, "", false);
        toolWindow.getContentManager().addContent(content);

        controller = new PluginController(this, project);
        initListeners();
    }

    private void initListeners() {
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                ListSelectionModel lsm = (ListSelectionModel) e.getSource();

                if (!lsm.isSelectionEmpty()) {
                    controller.itemClicked(tableModel.getErrorGroup().getChildren().get(table.getSelectedRow()));
                }
            }
        });

        btnUpdate.addActionListener(e -> controller.loadFile());

        btnBack.addActionListener(e -> controller.back());

    }
}
