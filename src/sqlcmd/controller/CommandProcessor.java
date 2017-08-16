package sqlcmd.controller;

import sqlcmd.controller.commands.Command;
import sqlcmd.model.DatabaseManager;
import sqlcmd.view.Console;
import sqlcmd.view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by s.sheyko on 03.08.2017.
 */
public class CommandProcessor {
    private DatabaseManager manager;
    private View view = new Console();
    public void run(){
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            while (true){
                ParsedCommand parsedCommand = new ParsedCommand(reader.readLine());
                if (parsedCommand.getCommand()==null) view.write("Wrong command!");
                else {
                    processCommand(parsedCommand);
                    if (parsedCommand.getCommand().equals("exit")) break;
                }
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    private void processCommand(ParsedCommand parsedCommand) {
//        "сonnect","tables","clear","drop","create","find","insert","update","delete","help","exit"
        String commandName = parsedCommand.getCommand();
        String[] params = parsedCommand.getParameters();
//        switch (commandName){
//            case "connect":
//
//        }
        try {
            Command command = (Command)Class.forName("sqlcmd.controller.commands."+parsedCommand.getCommand()).newInstance();
            command.run(parsedCommand);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }catch (Throwable e){
            e.printStackTrace();
        }
//        System.out.println(parsedCommand.getCommand());
//        System.out.println(Arrays.toString(parsedCommand.getParameters()));
//        System.out.println(parsedCommand.getErrorMessage());


    }


}
