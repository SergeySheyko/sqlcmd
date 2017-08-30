package sqlcmd.controller.command;

import sqlcmd.model.DatabaseManager;
import sqlcmd.view.View;

public class Connect extends AbstractCommand implements Command{

    public Connect(View view, DatabaseManager databaseManager) {
        super(view, databaseManager);
    }

    @Override
    public void run(String[] commands) {
        checkArguments(commands,4);
        try {
            String databaseName = commands[DATABASECOLUMN];
            String userName = commands[USERNAMECOLUMN];
            String password = commands[PASSWORDCOLUMN];
            databaseManager.connect(databaseName,userName,password);
        }
        finally {}

        view.write("Соединение успешно!");
    }

    @Override
    public boolean exit() {
        return false;
    }
}
