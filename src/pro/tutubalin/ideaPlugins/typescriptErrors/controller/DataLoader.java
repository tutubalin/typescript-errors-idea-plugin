package pro.tutubalin.ideaPlugins.typescriptErrors.controller;

import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.fileEditor.impl.LoadTextUtil;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import pro.tutubalin.ideaPlugins.typescriptErrors.model.ErrorGroup;

import java.io.File;
import java.util.function.Consumer;

class DataLoader {

    static void loadData(Project project, String filePath, final Consumer<ErrorGroup> onSuccess) {

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
