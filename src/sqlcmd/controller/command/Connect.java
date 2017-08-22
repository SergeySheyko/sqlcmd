package sqlcmd.controller.command;

import sqlcmd.model.DatabaseManager;
import sqlcmd.view.View;

/**
 * Created by s.sheyko on 22.08.2017.
 */
public class Connect extends CommonCommand{

    public Connect(String[] commands, View view, DatabaseManager databaseManager) {
        super(commands, view, databaseManager);
    }

    @Override
    public void run() {
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
