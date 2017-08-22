package sqlcmd.controller.command;

import sqlcmd.model.DatabaseManager;
import sqlcmd.view.View;

import java.util.Arrays;

/**
 * Created by s.sheyko on 22.08.2017.
 */
public class Tables extends CommonCommand {

    public Tables(String[] commands, View view, DatabaseManager databaseManager) {
        super(commands, view, databaseManager);
    }

    @Override
    public void run() {
        view.write(Arrays.toString(databaseManager.getTablesList()));
    }

}
