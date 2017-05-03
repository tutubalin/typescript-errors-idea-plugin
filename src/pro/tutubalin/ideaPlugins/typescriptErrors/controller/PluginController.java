package pro.tutubalin.ideaPlugins.typescriptErrors.controller;

import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.editor.ScrollType;
import com.intellij.openapi.editor.markup.*;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.JBColor;
import pro.tutubalin.ideaPlugins.typescriptErrors.model.ErrorGroup;
import pro.tutubalin.ideaPlugins.typescriptErrors.model.ErrorInformation;
import pro.tutubalin.ideaPlugins.typescriptErrors.view.PluginToolWindow;

import java.awt.*;
import java.io.File;

public class PluginController {

    private final Project project;
    private PluginToolWindow view;
    private ErrorGroup currentGroup;
    private static final TextAttributes hightlightAttributes = new TextAttributes(
            null,
            new JBColor(new Color(0xC06060), new Color(0x600000)),
            JBColor.RED,
            EffectType.WAVE_UNDERSCORE,
            0);

    private RangeHighlighter highlighter;
    private String inputFileName = "compilation.log";

    public PluginController(PluginToolWindow view, Project project) {
        this.view = view;
        this.project = project;
    }

    public void itemClicked(ErrorGroup errorGroup) {
        if (errorGroup.isLeaf()) {
            jumpToCode(errorGroup.getErrorInfo());
        } else {
            displayGroup(errorGroup);
        }
    }

    private void displayGroup(ErrorGroup errorGroup) {
        currentGroup = errorGroup;
        view.displayErrorGroup(errorGroup);
    }

    private void jumpToCode(ErrorInformation errorInfo) {

        VirtualFile file = LocalFileSystem.getInstance().findFileByIoFile(new File(project.getBasePath(), errorInfo.file));
        if (file != null && file.isValid()) {
            OpenFileDescriptor descriptor = new OpenFileDescriptor(project, file);
            descriptor.navigate(true);

            Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();

            if (editor != null) {
                CaretModel caretModel = editor.getCaretModel();
                int line = errorInfo.line-1;
                int startColumn = errorInfo.startIndex-1;
                int highlightLength = errorInfo.endIndex != -1 ? errorInfo.endIndex-startColumn : 0;
                LogicalPosition logicalPosition = new LogicalPosition(line, startColumn);

                caretModel.moveToLogicalPosition(logicalPosition);
                editor.getScrollingModel().scrollToCaret(ScrollType.MAKE_VISIBLE);

                int startOffset = caretModel.getOffset();
                int endOffset = startOffset + highlightLength;

                MarkupModel markupModel = editor.getMarkupModel();
                
                if (highlighter != null) {
                    try {
                        markupModel.removeHighlighter(highlighter);
                        highlighter = null;
                    } catch (Error e) {
                        // ignore
                    }
                }

                highlighter = markupModel.addRangeHighlighter(
                        startOffset,
                        endOffset,
                        HighlighterLayer.ERROR,
                        hightlightAttributes,
                        HighlighterTargetArea.EXACT_RANGE);

                highlighter.setErrorStripeTooltip("Test");

                highlighter.setErrorStripeMarkColor(JBColor.RED);

            }
        }
    }

    /*
    MessageBusConnection busConnection = project.getMessageBus().connect();
    busConnection.subscribe(ProjectManager.TOPIC, new ProjectManagerListener() {
      @Override
      public void projectOpened(Project project) {
        if (project == myProject) {
          ToolWindowManagerImpl.this.projectOpened();
        }
      }

      @Override
      public void projectClosed(Project project) {
        if (project == myProject) {
          ToolWindowManagerImpl.this.projectClosed();
        }
      }
    });
     */

    public void loadFile() {
        DataLoader.loadData(project, inputFileName, this::displayGroup);
    }

    public void back() {
        displayGroup(currentGroup.getParent());
    }

}
