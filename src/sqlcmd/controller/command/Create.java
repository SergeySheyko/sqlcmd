package sqlcmd.controller.command;

import sqlcmd.model.DatabaseManager;
import sqlcmd.view.View;

/**
 * Created by s.sheyko on 22.08.2017.
 */
public class Create implements Command{
    private View view;
    private DatabaseManager databaseManager;

    public Create(View view, DatabaseManager databaseManager) {
        this.view = view;
        this.databaseManager = databaseManager;
    }


    @Override
    public void run(String[] commands) {
        checkArgsQty(commands,3,true);
        String tablename;
        String[] columns = new String[commands.length-2];
        tablename = commands[1];
        int index = 0;
        for (int i=2;i<commands.length;i++){
            columns[index++] = commands[i];
        }
        databaseManager.createTable(tablename,columns);
    }
}
