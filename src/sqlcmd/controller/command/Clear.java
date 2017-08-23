package sqlcmd.controller.command;

import sqlcmd.model.DatabaseManager;
import sqlcmd.view.View;

/**
 * Created by s.sheyko on 22.08.2017.
 */
public class Clear implements Command{
    private View view;
    private DatabaseManager databaseManager;

    public Clear(View view, DatabaseManager databaseManager) {
        this.view = view;
        this.databaseManager = databaseManager;
    }


    @Override
    public void run(String[] commands) {
        checkArgsQty(commands,2,false);
        String tableName = commands[1];
        databaseManager.delete(tableName,null,null);
        view.write("Таблица "+tableName+" успешно очищена.");
    }
}
