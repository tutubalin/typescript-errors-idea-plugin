package pro.tutubalin.ideaPlugins.typescriptErrors.view;

import pro.tutubalin.ideaPlugins.typescriptErrors.model.ErrorGroup;

import javax.swing.table.AbstractTableModel;

public class ErrorGroupTableModel extends AbstractTableModel {

    private ErrorGroup errorGroup;

    @Override
    public int getRowCount() {
        if (errorGroup != null) {
            return errorGroup.getChildren().size();
        } else {
            return 0;
        }
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (errorGroup != null) {
            switch (columnIndex) {
                case 0:
                    return errorGroup.getChildren().get(rowIndex).getTitle();

                case 1:
                    return errorGroup.getChildren().get(rowIndex).getCount();
            }

        }

        return "";
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {

            case 0:
                if (errorGroup == null) {
                    return "Error Message";
                }

                switch (errorGroup.getLevel()) {

                    case ROOT:
                    case ERROR_CODE:
                        return "Error Message";

                    case DESCRIPTION:
                        return "File";

                    case FILE:
                        return "Location";

                }

            case 1:
                return "Count";
        }

        return "";
    }
    
    public ErrorGroup getErrorGroup() {
        return errorGroup;
    }

    public void setErrorGroup(ErrorGroup errorGroup) {
        this.errorGroup = errorGroup;
        //fireTableStructureChanged();
        fireTableDataChanged();
    }
}
