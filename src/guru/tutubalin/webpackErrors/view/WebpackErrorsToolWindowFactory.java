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

public class WebpackErrorsToolWindowFactory implements ToolWindowFactory {

    private JPanel myToolWindowContent;
    private JTextArea txtLog;
    private JButton btnUpdate;
    private JList<ErrorGroup> list;

    private PluginController controller;

    public static String ID = "Webpack Errors";

    private DefaultListModel<ErrorGroup> listModel;


    public void displayErrorGroup(ErrorGroup errorGroup) {

        listModel.clear();
        errorGroup.getChildren().forEach(item -> listModel.addElement(item));

    }

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        listModel = new DefaultListModel<>();
        list.setModel(listModel);

        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(myToolWindowContent, "", false);
        toolWindow.getContentManager().addContent(content);

        controller = new PluginController(this);
        initListeners();
    }

    private void initListeners() {
        list.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                ListSelectionModel lsm = (ListSelectionModel) e.getSource();

                if (!lsm.isSelectionEmpty()) {
                    controller.itemClicked( list.getSelectedValue() );
                }
            }
        });

        btnUpdate.addActionListener(e -> {
            controller.reloadFile();
        });

    }

    public void log(String message) {
        txtLog.append(message);
        txtLog.append("\n");
    }
}
