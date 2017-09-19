package sqlcmd.model;

import java.util.ArrayList;

public class DataSet {
    private String[] columns;
    private ArrayList<Object[]> rows = new ArrayList<Object[]>();

    public DataSet(String[] columns) {
        this.columns = columns;
    }

    public String[] getColumns() {
        return columns;
    }

    public ArrayList<Object[]> getRows() {
        return rows;
    }

    public void addRow(Object[] values) {
        if (values == null || columns == null || values.length != columns.length) {
            return;
        }
        int index = 0;
        Object[] row = new Object[columns.length];
        for (Object value : values) {
            row[index++] = value;
        }
        rows.add(row);
    }
}

