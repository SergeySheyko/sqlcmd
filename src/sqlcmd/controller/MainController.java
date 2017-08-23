package sqlcmd.controller;

import sqlcmd.controller.command.Command;
import sqlcmd.model.DatabaseManager;
import sqlcmd.view.View;

import java.util.Map;

/**
 * Created by s.sheyko on 15.08.2017.
 */
public class MainController {
    private View view;
    private DatabaseManager databaseManager;
    private Map<String, Command> commandMap;

    public MainController(View view, DatabaseManager databaseManager, Map<String, Command> commandMap) {
        this.view = view;
        this.databaseManager = databaseManager;
        this.commandMap = commandMap;
    }

    public void run() {
        view.write("Hello, user!");
        while (true){
            try {
                view.write("Введи команду или help для помощи:");
                String commandLine = view.read();
                String[] commands = commandLine.split("[|]");
                if (commands.length==0) {
                    commands = new String[1];
                    commands[0] = commandLine;
                }
                if (commandMap.containsKey(commands[0])){
                    Command command = commandMap.get(commands[0]);
                    command.run(commands);
                }
                else view.write("Неверная команда!");
            }
            catch (Exception e){
                printError(e);
            }
        }

    }

    public void printError(Exception e) {
        String errorMessage = e.getMessage();
        if (e.getCause()!=null) errorMessage+=" "+e.getCause().getMessage();
        view.write("Неудача по причине: "+errorMessage);
        view.write("Повтори попытку");
    }

}
