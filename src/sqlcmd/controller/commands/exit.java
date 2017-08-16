package sqlcmd.controller.commands;

import sqlcmd.controller.ParsedCommand;

/**
 * Created by s.sheyko on 03.08.2017.
 */
public class exit extends Command {
    public void run(ParsedCommand parsedCommand) {
        if (manager!=null) manager.disconnect();
    }

}
