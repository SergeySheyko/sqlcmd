package sqlcmd.controller.command;

import sqlcmd.model.DatabaseManager;
import sqlcmd.view.View;

public class Create extends AbstractCommand implements Command{

    public Create(View view, DatabaseManager databaseManager) {
        super(view, databaseManager);
    }

    @Override
    public void run(String[] commands) {
        checkArgsQty(commands,3,true);
        String[] columns = new String[commands.length-2];
        String tableName = commands[TABLENAMECOLUMN];
        int index = 0;
        for (int i=2;i<commands.length;i++){
            columns[index++] = commands[i];
        }
        databaseManager.createTable(tableName,columns);
    }
}
