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
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataLoader {

    private static Pattern patternSplitter = Pattern.compile("ERROR\\sin\\s(?:\\[at-loader]\\s)?", Pattern.MULTILINE);
    private static Pattern patternError = Pattern.compile("([^:]+)(?::(\\d+):(\\d+))?\\s*\\n^\\s*([^:]+):\\s*(.*)",Pattern.MULTILINE);
    private static Pattern patternStackTrace = Pattern.compile("\\s@\\s(?:\\S*\\s(?:(\\d+):(\\d+)-(\\d+))?)",Pattern.MULTILINE);

    public void loadData(String filePath, final Consumer<ErrorGroup> onSuccess) {

        DataContext dataContext = DataManager.getInstance().getDataContextFromFocus().getResult();
        Project project = DataKeys.PROJECT.getData(dataContext);

        ProgressManager.getInstance().run(new Task.Backgroundable(project, "Loading Log File") {

            private ErrorGroup root;
            private CharSequence fileContent;
            private ProgressIndicator progressIndicator;

            @Override
            public void run(@NotNull ProgressIndicator progressIndicator) {
                this.progressIndicator = progressIndicator;

                Application application = ApplicationManager.getApplication();
                
                application.runReadAction(() -> {
                    VirtualFile file;

                    Project project1 = getProject();

                    if (project1 != null) {

                        file = LocalFileSystem.getInstance().findFileByIoFile(new File(project1.getBasePath(), filePath));
                        if (file != null && file.isValid()) {
                            fileContent = LoadTextUtil.loadText(file);
                            application.invokeLater(this::parseFile);
                        }
                    }
                });

            }

            private void parseFile() {
                progressIndicator.setFraction(0.10);
                progressIndicator.setText("Parsing...");

                root = new ErrorGroup("All");

                patternSplitter.splitAsStream(fileContent)
                        .skip(1)
                        .map(this::parseError)
                        .forEach( error -> root.add(error));

                //fileContent.spl  (patternSplitter);

                //patternSplitter.split()

                /*root.add(new ErrorInformation() {
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
                        errorCode = "1002";
                        description = "Shit happened";
                        file = "index.ts";
                        startIndex = 1;
                        endIndex = 10;
                        line = 4;
                    }
                });

                root.add(new ErrorInformation() {
                    {
                        errorCode = "1003";
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
                }); */
            }

            private ErrorInformation parseError(String errorMessage) {

                Matcher errorMatcher = patternError.matcher(errorMessage);

                if (errorMatcher.lookingAt()) {
                    ErrorInformation result = new ErrorInformation() {{
                        file = fixPath(errorMatcher.group(1));
                        line = safeParseInt(errorMatcher.group(2), -1);
                        startIndex = safeParseInt(errorMatcher.group(3), -1);
                        errorCode = errorMatcher.group(4);
                        description = errorMatcher.group(5);
                    }};

                    Matcher stackTraceMatcher = patternStackTrace.matcher(errorMessage.substring(errorMatcher.end()));

                    if (stackTraceMatcher.lookingAt()) {
                        result.line = safeParseInt(stackTraceMatcher.group(1), result.line);
                        result.startIndex = safeParseInt(stackTraceMatcher.group(2), result.startIndex);
                        result.endIndex = safeParseInt(stackTraceMatcher.group(3), result.endIndex);
                    }

                    return result;
                }


                return null;
            }

            private String fixPath(String path) {
                return path;
            }

            private int safeParseInt(String string, int defaultValue) {
                try {
                    return Integer.parseInt(string);
                } catch (NumberFormatException e) {
                    return defaultValue;
                }
            }

            @Override
            public void onSuccess() {
                super.onSuccess();
                ApplicationManager.getApplication().invokeLater(()->{
                    onSuccess.accept(root);
                });
            }

        });
        
    }




}
