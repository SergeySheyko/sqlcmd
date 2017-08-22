package sqlcmd.controller;

import sqlcmd.controller.command.CommonCommand;
import sqlcmd.model.DatabaseManager;
import sqlcmd.view.View;

import java.lang.reflect.Constructor;
import java.util.Map;

/**
 * Created by s.sheyko on 15.08.2017.
 */
public class MainController {
    private View view;
    private DatabaseManager databaseManager;
    private final Map<String,Class<?>> commandTypes;

    public MainController(View view, DatabaseManager databaseManager) {
        this.view = view;
        this.databaseManager = databaseManager;
        this.commandTypes = CommonCommand.getCommandTypes();
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
                if (commandTypes.containsKey(commands[0])){
                    Class<?> command = commandTypes.get(commands[0]);
                    Constructor<?> constructor = command.getConstructor(String[].class,View.class,DatabaseManager.class);
                    CommonCommand instance = (CommonCommand) constructor.newInstance(commands,view,databaseManager);
                    instance.run();
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
