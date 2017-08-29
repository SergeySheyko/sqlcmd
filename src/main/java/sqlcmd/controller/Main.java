package sqlcmd.controller;

import sqlcmd.controller.MainController;
import sqlcmd.controller.command.*;
import sqlcmd.model.DatabaseManager;
import sqlcmd.model.JDBCDatabaseManager;
import sqlcmd.view.Console;
import sqlcmd.view.View;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by s.sheyko on 01.08.2017.
 */
public class Main  {

    public static void main(String[] args) {

        View view = new Console();
        DatabaseManager databaseManager = new JDBCDatabaseManager();
        Map<String,Command> commandMap = new HashMap<>();
        commandMap.put("connect",new Connect(view,databaseManager));
        commandMap.put("tables",new Tables(view,databaseManager));
        commandMap.put("clear",new Clear(view,databaseManager));
        commandMap.put("drop",new Drop(view,databaseManager));
        commandMap.put("create",new Create(view,databaseManager));
        commandMap.put("find",new Find(view,databaseManager));
        commandMap.put("insert",new Insert(view,databaseManager));
        commandMap.put("update",new Update(view,databaseManager));
        commandMap.put("delete",new Delete(view,databaseManager));
        commandMap.put("help",new Help(view,null));
        commandMap.put("exit",new Exit(view,databaseManager));

        MainController mainController = new MainController(view,commandMap);
        mainController.run();
    }


}
