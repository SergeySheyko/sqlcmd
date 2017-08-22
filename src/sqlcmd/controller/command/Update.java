package sqlcmd.controller.command;

import sqlcmd.model.DataSet;
import sqlcmd.model.DatabaseManager;
import sqlcmd.view.View;

import java.util.Arrays;

/**
 * Created by s.sheyko on 22.08.2017.
 */
public class Update extends CommonCommand{
    public Update(String[] commands, View view, DatabaseManager databaseManager) {
        super(commands, view, databaseManager);
    }

    @Override
    public void run() {
        checkArgsQty(commands,6,true);
        if (commands.length%2!=0) throw new IllegalArgumentException("неверное количество параметров - требуется четное количество!");
        String tableName = commands[1];
        String checkedColumn = commands[2];
        String checkedValue = commands[3];
        String[] columns = new String[(commands.length-4)/2];
        String[] values = new String[(commands.length-4)/2];
        int columnsIndex = 0;
        int valuesIndex = 0;
        for (int i=4;i<commands.length;i++){
            if (i%2==0) columns[columnsIndex++] = commands[i];
            else values[valuesIndex++] = commands[i];
        }
        databaseManager.update(tableName,checkedColumn,checkedValue,columns,values);
        DataSet dataSet = databaseManager.getTableData(tableName);
        displayTableData(dataSet);
    }
}
