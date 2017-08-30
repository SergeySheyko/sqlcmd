package sqlcmd.controller.command;

import sqlcmd.model.DataSet;
import sqlcmd.model.DatabaseManager;
import sqlcmd.view.View;

import java.util.List;

public abstract class AbstractCommand {
    final int COMMANDCOLUMN = 0;
    final int DATABASECOLUMN = 1;
    final int TABLENAMECOLUMN = 1;
    final int USERNAMECOLUMN = 2;
    final int PASSWORDCOLUMN = 3;

    View view;
    DatabaseManager databaseManager;

    public AbstractCommand(View view, DatabaseManager databaseManager) {
        this.view = view;
        this.databaseManager = databaseManager;
    }

    void checkArguments(String[] commands, int requiredLength){
        checkArguments(commands, requiredLength, false, false, false);
    }

    void checkArguments(String[] commands, int requiredLength, boolean equalOrMore) {
        checkArguments(commands, requiredLength, equalOrMore, false, false);
    }

    void checkArguments(String[] commands, int requiredLength, boolean equalOrMore, boolean evenOnly, boolean oddOnly) {
        int length = commands.length;

        if (evenOnly && length%2!=0){
            throw new IllegalArgumentException("неверное количество параметров - требуется четное количество");
        }
        if (oddOnly && length%2==0){
            throw new IllegalArgumentException("неверное количество параметров - требуется нечетное количество");
        }
        if (!equalOrMore && length != requiredLength){
            throw new IllegalArgumentException(String.format("неверное количество параметров - требуется %d, обнаружено %d", requiredLength, length));
        }
        if (equalOrMore && length < requiredLength){
            throw new IllegalArgumentException(String.format("неверное количество параметров - требуется %d и более, обнаружено %d", requiredLength, length));
        }
    }

    void displayTableData(DataSet dataSet, View view) {
        String[] columns = dataSet.getColumns();
        List<Object[]> rows = dataSet.getRows();
        String delimiterRow = "+";
        String formatLine = "|";
        for (int i = 0; i < columns.length; i++) {
            int maxLength = columns[i].length();
            if (rows != null) {
                for (Object[] row : rows) {
                    if (row[i].toString().length() > maxLength) maxLength = row[i].toString().length();
                }
            }
            formatLine += " %-" + maxLength + "s |";
            delimiterRow += "-";
            for (int j = 0; j < maxLength; j++) delimiterRow += "-";
            delimiterRow += "-+";
        }
        view.write(delimiterRow);
        view.write(formatLine, (Object[]) columns);
        view.write(delimiterRow);
        if (rows == null) return;
        for (Object[] row : rows) {
            view.write(formatLine, row);
            view.write(delimiterRow);
        }
    }

    void displayTable(String tableName){
        DataSet dataSet = databaseManager.getTableData(tableName);
        displayTableData(dataSet,view);
    }
}
