package sqlcmd.controller.command;

import sqlcmd.model.DatabaseManager;
import sqlcmd.view.View;

public class Find extends AbstractCommand implements Command{

    public Find(View view, DatabaseManager databaseManager) {
        super(view, databaseManager);
    }

    @Override
    public void run(String[] commands) {
        checkParameters(commands,2);
        String tableName = commands[TABLENAMECOLUMN];
        displayTable(tableName);
    }

    @Override
    public boolean exit() {
        return false;
    }
}
