package sqlcmd.controller.command;

import sqlcmd.model.DatabaseManager;
import sqlcmd.view.View;

public class Delete extends AbstractCommand implements Command{

    public Delete(View view, DatabaseManager databaseManager) {
        super(view, databaseManager);
    }

    @Override
    public void run(String[] commands) {
        checkParameters(commands,4);
        String tableName = commands[TABLENAMECOLUMN];
        String checkedColumn = commands[2];
        String checkedValue = commands[3];
        databaseManager.delete(tableName,checkedColumn,checkedValue);
        displayTable(tableName);
    }

    @Override
    public boolean exit() {
        return false;
    }
}
