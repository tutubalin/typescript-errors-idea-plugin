package guru.tutubalin.webpackErrors.view;

import guru.tutubalin.webpackErrors.model.ErrorGroup;
import guru.tutubalin.webpackErrors.model.ErrorGroupLevel;

import javax.swing.*;
import java.awt.*;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;

public class ErrorGroupCellRenderer implements javax.swing.ListCellRenderer<ErrorGroup> {

    private JTextField txtTitle;
    private JTextField txtCount;
    private JPanel panelRenderer;

    public static ResourceBundle generalErrorMessages = ResourceBundle.getBundle("errors");

    @Override
    public Component getListCellRendererComponent(JList<? extends ErrorGroup> list, ErrorGroup errorGroup, int index, boolean isSelected, boolean cellHasFocus) {

        String title = errorGroup.getTitle();

        String errorCode = errorGroup.getErrorInfo().errorCode;
        if (errorGroup.getLevel() == ErrorGroupLevel.ERROR_CODE && generalErrorMessages.containsKey(errorCode)) {
            title += " "+generalErrorMessages.getString(errorCode);
        }

        txtTitle.setText(title);

        txtCount.setText(String.valueOf(errorGroup.getCount()));
        
        return panelRenderer;
    }
}
