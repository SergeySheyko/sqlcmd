package sqlcmd.controller;

import java.util.Arrays;

/**
 * Created by s.sheyko on 02.08.2017.
 */
public class ParsedCommand implements Settings{
    private String command;
    private String errorMessage="";
    private String[] parameters;

    public ParsedCommand(String commandLine) {
        parseCommand(commandLine);
    }

    private void parseCommand(String commandLine){

        for (int i = 0; i< COMMANDS.length; i++){
            String definedCommand = COMMANDS[i];
            String[] partsOfCommand = getSplittedString(commandLine, "[|]");
            if (commandLine.equals(definedCommand) ||
                    partsOfCommand.length>0 && partsOfCommand[0].equals(definedCommand)
                    ){
                this.command = definedCommand;
                if (partsOfCommand.length>1){
                    parameters = Arrays.copyOfRange(partsOfCommand,1,partsOfCommand.length);
                }
            }
        }
        if (command==null) errorMessage="Command not found!";
    }

    public String[] getSplittedString(String string, String delimiter) {
        String[] splittedString = string.split(delimiter);
        for (int i = 0; i < splittedString.length; i++) {
            splittedString[i] =splittedString[i].trim();
        }
        return splittedString;
    }

    public String getCommand(){
        return command;
    }

    public String[] getParameters(){
        return parameters;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
