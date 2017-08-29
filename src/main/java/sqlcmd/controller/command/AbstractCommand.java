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

    void checkArgsQty(String[] commands, int qty, boolean equalOrMore) {
        String moreThan = equalOrMore ? " и более" : "";
        if (commands.length != qty && !equalOrMore || equalOrMore && commands.length < qty) {
            throw new IllegalArgumentException(String.format("неверное количество параметров - требуется %d%s, обнаружено %d", qty, moreThan, commands.length));
        }
    }



    void displayTableData(DataSet dataSet, View view) {
        String[] columns = dataSet.getColumns();
        List<Object[]> rows = dataSet.getRows();
        String delimiterRow = "+";
        String formatLine = "|";
        for (int i = 0; i < columns.length; i++) {
            int maxLen = columns[i].length();
            if (rows != null) {
                for (Object[] row : rows) {
                    if (row[i].toString().length() > maxLen) maxLen = row[i].toString().length();
                }
            }
            formatLine += " %-" + maxLen + "s |";
            delimiterRow += "-";
            for (int j = 0; j < maxLen; j++) delimiterRow += "-";
            delimiterRow += "-+";
        }
        view.write(delimiterRow);
        view.write(String.format(formatLine, columns));
        view.write(delimiterRow);
        if (rows == null) return;
        for (Object[] row : rows) {
            view.write(String.format(formatLine, row));
            view.write(delimiterRow);
        }
    }
}
