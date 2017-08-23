package sqlcmd.controller.command;

import sqlcmd.model.DatabaseManager;
import sqlcmd.view.View;

import java.util.Arrays;

/**
 * Created by s.sheyko on 22.08.2017.
 */
public class Tables implements Command {
    private View view;
    private DatabaseManager databaseManager;

    public Tables(View view, DatabaseManager databaseManager) {
        this.view = view;
        this.databaseManager = databaseManager;
    }

    @Override
    public void run(String[] commands) {
        view.write(Arrays.toString(databaseManager.getTablesList()));
    }

}
