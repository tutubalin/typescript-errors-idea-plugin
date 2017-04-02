package guru.tutubalin.webpackErrors.controller;

import guru.tutubalin.webpackErrors.model.ErrorGroup;
import guru.tutubalin.webpackErrors.model.ErrorInformation;
import guru.tutubalin.webpackErrors.view.PluginToolWindow;

import javax.swing.*;

public class PluginController {


    private PluginToolWindow view;

    private ListModel<ErrorGroup> model;
    
    private ErrorGroup currentGroup;

    public PluginController(PluginToolWindow view) {
        this.view = view;
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
        
    }

    public void loadFile() {
        DataLoader.loadData("log.txt", this::displayGroup);
    }

    public void back() {
        displayGroup(currentGroup.getParent());
    }
}
