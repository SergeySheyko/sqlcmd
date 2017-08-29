package sqlcmd.controller.command;

import sqlcmd.model.DataSet;
import sqlcmd.model.DatabaseManager;
import sqlcmd.view.View;

import java.util.Arrays;

public class Delete extends AbstractCommand implements Command{

    public Delete(View view, DatabaseManager databaseManager) {
        super(view, databaseManager);
    }

    @Override
    public void run(String[] commands) {
        checkArgsQty(commands,4,false);
        String tableName = commands[TABLENAMECOLUMN];
        String checkedColumn = commands[2];
        String checkedValue = commands[3];
        databaseManager.delete(tableName,checkedColumn,checkedValue);
        DataSet dataSet = databaseManager.getTableData(tableName);
        displayTableData(dataSet,view);
    }
}
