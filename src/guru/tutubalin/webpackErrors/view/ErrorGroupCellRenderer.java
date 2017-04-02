package guru.tutubalin.webpackErrors.view;

import guru.tutubalin.webpackErrors.model.ErrorGroup;

import javax.swing.*;
import java.awt.*;

public class ErrorGroupCellRenderer implements javax.swing.ListCellRenderer<ErrorGroup> {

    private JTextField txtTitle;
    private JTextField txtCount;
    private JPanel panelRenderer;

    @Override
    public Component getListCellRendererComponent(JList<? extends ErrorGroup> list, ErrorGroup errorGroup, int index, boolean isSelected, boolean cellHasFocus) {

        txtTitle.setText(errorGroup.getTitle());
        txtCount.setText(String.valueOf(errorGroup.getCount()));
        
        return panelRenderer;
    }
}
