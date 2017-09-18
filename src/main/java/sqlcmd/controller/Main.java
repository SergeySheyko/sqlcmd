package sqlcmd.controller;

import sqlcmd.controller.MainController;
import sqlcmd.controller.command.*;
import sqlcmd.model.DatabaseManager;
import sqlcmd.model.JDBCDatabaseManager;
import sqlcmd.view.Console;
import sqlcmd.view.View;

import java.util.HashMap;
import java.util.Map;


public class Main  {

    public static void main(String[] args) {

        View view = new Console();
        DatabaseManager databaseManager = new JDBCDatabaseManager();

        Map<String,Command> commandsMap = new HashMap<>();
        commandsMap.put("connect",new Connect(view,databaseManager));
        commandsMap.put("tables",new Tables(view,databaseManager));
        commandsMap.put("clear",new Clear(view,databaseManager));
        commandsMap.put("drop",new Drop(view,databaseManager));
        commandsMap.put("create",new Create(view,databaseManager));
        commandsMap.put("find",new Find(view,databaseManager));
        commandsMap.put("insert",new Insert(view,databaseManager));
        commandsMap.put("update",new Update(view,databaseManager));
        commandsMap.put("delete",new Delete(view,databaseManager));
        commandsMap.put("help",new Help(view,null));
        commandsMap.put("exit",new Exit(view,databaseManager));

        MainController mainController = new MainController(view,commandsMap);
        mainController.run();
    }


}
