package sqlcmd.controller.command;

import sqlcmd.model.DatabaseManager;
import sqlcmd.view.View;

/**
 * Created by s.sheyko on 22.08.2017.
 */
public class Insert extends AbstractCommand implements Command{

    public Insert(View view, DatabaseManager databaseManager) {
        super(view, databaseManager);
    }

    @Override
    public void run(String[] commands) {
        checkArguments(commands,4,true,true,false);
        String tableName = commands[TABLENAMECOLUMN];
        String[] columns = new String[(commands.length-2)/2];
        String[] values = new String[(commands.length-2)/2];
        int columnsIndex = 0;
        int valuesIndex = 0;
        for (int i=2;i<commands.length;i++){
            if (i%2==0) columns[columnsIndex++] = commands[i];
            else values[valuesIndex++] = commands[i];
        }
        view.write("добавлено "+databaseManager.insert(columns,values, tableName)+" строк");
    }

    @Override
    public boolean exit() {
        return false;
    }
}
