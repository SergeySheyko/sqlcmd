package sqlcmd.controller.command;

import sqlcmd.model.DataSet;
import sqlcmd.model.DatabaseManager;
import sqlcmd.view.View;

import java.util.List;

abstract class AbstractCommand {
    final int COMMANDCOLUMN = 0;
    final int DATABASECOLUMN = 1;
    final int TABLENAMECOLUMN = 1;
    final int USERNAMECOLUMN = 2;
    final int PASSWORDCOLUMN = 3;

    final View view;
    final DatabaseManager databaseManager;

    AbstractCommand(View view, DatabaseManager databaseManager) {
        this.view = view;
        this.databaseManager = databaseManager;
    }

    void checkParameters(String[] commands, int requiredLength){
        checkParameters(commands, requiredLength, false, false, false);
    }

    void checkParameters(String[] commands, int requiredLength, boolean equalOrMore) {
        checkParameters(commands, requiredLength, equalOrMore, false, false);
    }

    void checkParameters(String[] commands, int requiredLength, boolean equalOrMore, boolean evenOnly, boolean oddOnly) {
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
        for (String command:commands){
            if (command.length()==0){
                throw new IllegalArgumentException("ошибка в параметрах");
            }
        }
    }

    void checkTableName(String tableName){
        boolean isCorrect = true;
        if (tableName.length()==0){
            isCorrect = false;
        }
        if (!Character.isLetter(tableName.charAt(0))){
            isCorrect = false;
        }
        if (!isCorrect) {
            throw new IllegalArgumentException("ошибка в названии таблицы");
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
