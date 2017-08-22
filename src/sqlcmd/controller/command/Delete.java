package sqlcmd.controller.command;

import sqlcmd.model.DataSet;
import sqlcmd.model.DatabaseManager;
import sqlcmd.view.View;

import java.util.Arrays;

/**
 * Created by s.sheyko on 22.08.2017.
 */
public class Delete extends CommonCommand{
    public Delete(String[] commands, View view, DatabaseManager databaseManager) {
        super(commands, view, databaseManager);
    }

    @Override
    public void run() {
        checkArgsQty(commands,4,false);
        String tableName = commands[1];
        String checkedColumn = commands[2];
        String checkedValue = commands[3];
        databaseManager.delete(tableName,checkedColumn,checkedValue);
        DataSet dataSet = databaseManager.getTableData(tableName);
        displayTableData(dataSet);
    }
}
