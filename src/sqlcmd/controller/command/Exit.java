package sqlcmd.controller.command;

import sqlcmd.model.DatabaseManager;
import sqlcmd.view.View;

/**
 * Created by s.sheyko on 22.08.2017.
 */
public class Exit extends CommonCommand {
    public Exit(String[] commands, View view, DatabaseManager databaseManager) {
        super(commands, view, databaseManager);
    }

    @Override
    public void run() {
        databaseManager.disconnect();
        view.write("До свидания!");
        System.exit(0);
    }
}
