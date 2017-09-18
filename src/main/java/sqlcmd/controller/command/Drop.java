package sqlcmd.controller.command;

import sqlcmd.model.DatabaseManager;
import sqlcmd.view.View;

public class Drop extends AbstractCommand implements Command{

    public Drop(View view, DatabaseManager databaseManager) {
        super(view, databaseManager);
    }

    @Override
    public void run(String[] commands) {
        checkParameters(commands,2);
        databaseManager.dropTable(commands[TABLENAMECOLUMN]);
        view.write("Таблица успешно удалена");
    }

    @Override
    public boolean exit() {
        return false;
    }
}
