package sqlcmd.controller.command;

import sqlcmd.model.DatabaseManager;
import sqlcmd.view.View;

/**
 * Created by s.sheyko on 22.08.2017.
 */
public class Connect implements Command{
    private View view;
    private DatabaseManager databaseManager;

    public Connect(View view, DatabaseManager databaseManager) {
        this.view = view;
        this.databaseManager = databaseManager;
    }

    @Override
    public void run(String[] commands) {
        checkArgsQty(commands,4,false);
        try {
            String databaseName = commands[1];
            String userName = commands[2];
            String password = commands[3];
            databaseManager.connect(databaseName,userName,password);
        }
        finally {}

        view.write("Соединение успешно!");
    }
}
