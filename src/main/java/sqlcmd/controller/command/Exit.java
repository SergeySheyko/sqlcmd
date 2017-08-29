package sqlcmd.controller.command;

import sqlcmd.model.DatabaseManager;
import sqlcmd.view.View;

/**
 * Created by s.sheyko on 22.08.2017.
 */
public class Exit extends AbstractCommand implements Command {

    public Exit(View view, DatabaseManager databaseManager) {
        super(view, databaseManager);
    }

    @Override
    public void run(String[] commands) {
        databaseManager.disconnect();
        view.write("До свидания!");
        System.exit(0);
    }
}
