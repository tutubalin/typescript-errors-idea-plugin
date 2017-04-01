package guru.tutubalin.webpackErrors.controller;

import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.fileEditor.impl.LoadTextUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import guru.tutubalin.webpackErrors.model.ErrorGroup;
import guru.tutubalin.webpackErrors.model.ErrorGroupLevel;
import guru.tutubalin.webpackErrors.model.ErrorInformation;
import guru.tutubalin.webpackErrors.view.WebpackErrorsToolWindowFactory;

import javax.swing.*;
import java.io.File;
import java.util.StringJoiner;
import java.util.regex.Pattern;

public class PluginController {


    private WebpackErrorsToolWindowFactory view;

    private ListModel<ErrorGroup> model;

    private static Pattern patternError = Pattern.compile("ERROR IN(.*)\n\n", Pattern.MULTILINE);

    public PluginController(WebpackErrorsToolWindowFactory view) {
        this.view = view;

        ErrorGroup root = new ErrorGroup("Root");

        root.add(new ErrorInformation() {
            {
                errorCode = "1000";
                description = "Shit happened";
                file = "index.ts";
                startIndex = 1;
                endIndex = 10;
                line = 4;
            }
        });

        root.add(new ErrorInformation() {
            {
                errorCode = "1000";
                description = "Shit happened";
                file = "huy.ts";
                startIndex = 1;
                endIndex = 10;
                line = 4;
            }
        });

        root.add(new ErrorInformation() {
            {
                errorCode = "1000";
                description = "Almost ok";
                file = "index.ts";
                startIndex = 1;
                endIndex = 10;
                line = 4;
            }
        });

        root.add(new ErrorInformation() {
            {
                errorCode = "1001";
                description = "Shit happened";
                file = "index.ts";
                startIndex = 1;
                endIndex = 10;
                line = 4;
            }
        });

        root.add(new ErrorInformation() {
            {
                errorCode = "1001";
                description = "Shit happened";
                file = "index.ts";
                startIndex = 1;
                endIndex = 10;
                line = 4;
            }
        });

        root.add(new ErrorInformation() {
            {
                errorCode = "1001";
                description = "WTF?";
                file = "index.ts";
                startIndex = 1;
                endIndex = 10;
                line = 4;
            }
        });


        displayGroup(root);
    }

    public void itemClicked(ErrorGroup errorGroup) {
        if (errorGroup.isLeaf()) {
            jumpToCode(errorGroup.getErrorInfo());
        } else {
            displayGroup(errorGroup);
        }
    }

    private void displayGroup(ErrorGroup errorGroup) {
        view.displayErrorGroup(errorGroup);
    }

    private void jumpToCode(ErrorInformation errorInfo) {
        
    }

    public void reloadFile() {
        DataContext dataContext = DataManager.getInstance().getDataContextFromFocus().getResult();
        Project project = DataKeys.PROJECT.getData(dataContext);

        VirtualFile file;
        if (project != null) {
            file = LocalFileSystem.getInstance().findFileByIoFile(new File(project.getBasePath(), "log.txt"));
            if (file != null) {
                CharSequence content = LoadTextUtil.loadText(file);
                //updateData(content.toString());

                log(content.toString());
            } else {
                log("File not found");
            }
        }
    }

    private void log(String message) {
        view.log(message);
    }
}
