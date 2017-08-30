package sqlcmd.controller;

import sqlcmd.controller.command.Command;
import sqlcmd.view.View;

import java.util.Map;

/**
 * Created by s.sheyko on 15.08.2017.
 */
public class MainController {
    private View view;
    private Map<String, Command> commandsMap;

    public MainController(View view, Map<String, Command> commandsMap) {
        this.view = view;
        this.commandsMap = commandsMap;
    }

    public void run() {
        view.write("Привет!");
        while (true) {
            try {
                view.write("Введи команду или help для помощи:");
                String commandLine = view.read();
                String[] commands = getPreparedCommands(commandLine);
                if (commandsMap.containsKey(commands[0])) {
                    Command command = commandsMap.get(commands[0]);
                    command.run(commands);
                    if (command.exit()) break;
                } else {
                    view.write("Неверная команда!");
                }
            } catch (Exception e) {
                printError(e);
            }
        }

    }

    private String[] getPreparedCommands(String commandLine) {
        String[] result = commandLine.split("[|]");
        if (result.length == 0) {
            result = new String[1];
            result[0] = commandLine;
        }
        return result;
    }

    public void printError(Exception e) {
        String errorMessage = e.getMessage();
        if (e.getCause() != null) {
            errorMessage += " " + e.getCause().getMessage();
        }
        view.write("Неудача по причине: " + errorMessage);
        view.write("Повтори попытку");
    }

}
