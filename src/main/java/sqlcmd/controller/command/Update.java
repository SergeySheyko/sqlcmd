package sqlcmd.controller.command;

import sqlcmd.model.DatabaseManager;
import sqlcmd.view.View;

/**
 * Created by s.sheyko on 22.08.2017.
 */
public class Update extends AbstractCommand implements Command{

    public Update(View view, DatabaseManager databaseManager) {
        super(view, databaseManager);
    }

    @Override
    public void run(String[] commands) {
        checkArguments(commands,6,true,true,false);
        String tableName = commands[TABLENAMECOLUMN];
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
        displayTable(tableName);
    }

    @Override
    public boolean exit() {
        return false;
    }
}
