package guru.tutubalin.webpackErrors.controller;

import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.fileEditor.impl.LoadTextUtil;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import guru.tutubalin.webpackErrors.model.ErrorGroup;
import guru.tutubalin.webpackErrors.model.ErrorInformation;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class DataLoader {

    static void loadData(String filePath, final Consumer<ErrorGroup> onSuccess) {

        // TODO: use another way to get project
        // dataContext may be null on IDEA startup
        DataContext dataContext = DataManager.getInstance().getDataContextFromFocus().getResult();
        Project project = DataKeys.PROJECT.getData(dataContext);

        ProgressManager.getInstance().run(new Task.Backgroundable(project, "Loading Log File") {

            private ErrorGroup root;

            @Override
            public void run(@NotNull ProgressIndicator progressIndicator) {
                Application application = ApplicationManager.getApplication();
                
                application.runReadAction(() -> {
                    VirtualFile file;

                    Project project1 = getProject();

                    if (project1 != null) {

                        file = LocalFileSystem.getInstance().findFileByIoFile(new File(project1.getBasePath(), filePath));
                        if (file != null && file.isValid()) {
                            CharSequence fileContent = LoadTextUtil.loadText(file);

                            progressIndicator.setFraction(0.10);
                            progressIndicator.setText("Parsing...");

                            application.invokeLater(() -> root = LogFormatDetector.getParser(fileContent).parseFile(fileContent));
                        }
                    }
                });
            }

            @Override
            public void onSuccess() {
                super.onSuccess();
                ApplicationManager.getApplication().invokeLater( ()-> onSuccess.accept(root) );
            }

        });
        
    }




}
