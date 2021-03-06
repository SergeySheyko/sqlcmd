package sqlcmd.controller.command;

import sqlcmd.model.DataSet;
import sqlcmd.model.DatabaseManager;
import sqlcmd.view.View;

public class Insert extends AbstractCommand implements Command {

    public Insert(View view, DatabaseManager databaseManager) {
        super(view, databaseManager);
    }

    @Override
    public void run(String[] commands) {
        checkParameters(commands, 4, true, true, false);
        String tableName = commands[TABLENAMECOLUMN];
        String[] columns = new String[(commands.length - 2) / 2];
        String[] values = new String[(commands.length - 2) / 2];
        int columnsIndex = 0;
        int valuesIndex = 0;
        for (int i = 2; i < commands.length; i++) {
            if (i % 2 == 0) {
                columns[columnsIndex++] = commands[i];
            } else {
                values[valuesIndex++] = commands[i];
            }
        }
        DataSet dataSet = new DataSet(columns);
        dataSet.addRow(values);
        int affectedRows = databaseManager.insert(tableName, dataSet);
        view.write("Добавлено %d строк", affectedRows);
    }

    @Override
    public boolean exit() {
        return false;
    }
}
