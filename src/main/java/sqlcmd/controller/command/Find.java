package sqlcmd.controller.command;

import sqlcmd.model.DataSet;
import sqlcmd.model.DatabaseManager;
import sqlcmd.view.View;

/**
 * Created by s.sheyko on 22.08.2017.
 */
public class Find extends AbstractCommand implements Command{

    public Find(View view, DatabaseManager databaseManager) {
        super(view, databaseManager);
    }

    @Override
    public void run(String[] commands) {
        checkArgsQty(commands,2,false);
        DataSet dataSet = databaseManager.getTableData(commands[1]);
        displayTableData(dataSet,view);

    }
}
