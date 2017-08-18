package sqlcmd;

import sqlcmd.controller.MainController;
import sqlcmd.model.DatabaseManager;
import sqlcmd.model.JDBCDatabaseManager;
import sqlcmd.view.Console;
import sqlcmd.view.View;


/**
 * Created by s.sheyko on 01.08.2017.
 */
public class Main  {

    public static void main(String[] args) {

        View view = new Console();
        DatabaseManager databaseManager = new JDBCDatabaseManager();
        MainController mainController = new MainController(view,databaseManager);
        mainController.run();
    }


}
