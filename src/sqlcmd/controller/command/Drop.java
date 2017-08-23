package sqlcmd.controller.command;

import sqlcmd.model.DatabaseManager;
import sqlcmd.view.View;

/**
 * Created by s.sheyko on 22.08.2017.
 */
public class Drop implements Command{
    private View view;
    private DatabaseManager databaseManager;

    public Drop(View view, DatabaseManager databaseManager) {
        this.view = view;
        this.databaseManager = databaseManager;
    }

    @Override
    public void run(String[] commands) {
        checkArgsQty(commands,2,false);
        databaseManager.dropTable(commands[1]);
    }
}
