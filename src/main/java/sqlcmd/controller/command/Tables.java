package sqlcmd.controller.command;

import sqlcmd.model.DatabaseManager;
import sqlcmd.view.View;

import java.util.ArrayList;
import java.util.Arrays;

public class Tables extends AbstractCommand implements Command {

    public Tables(View view, DatabaseManager databaseManager) {
        super(view, databaseManager);
    }

    @Override
    public void run(String[] commands) {
        ArrayList<String> tablesList = databaseManager.getTablesList();
        view.write(tablesList.toString());
    }

    @Override
    public boolean exit() {
        return false;
    }

}
