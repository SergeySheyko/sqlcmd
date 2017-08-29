package sqlcmd.controller.command;

import sqlcmd.model.DatabaseManager;
import sqlcmd.view.View;

public class Clear extends AbstractCommand implements Command{

    public Clear(View view, DatabaseManager databaseManager) {
        super(view, databaseManager);
    }

    @Override
    public void run(String[] commands) {
        checkArgsQty(commands,2,false);
        String tableName = commands[COMMANDCOLUMN];
        databaseManager.delete(tableName,null,null);
        view.write("Таблица %s успешно очищена.",tableName);
    }
}
