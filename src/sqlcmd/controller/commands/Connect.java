package sqlcmd.controller.commands;

import sqlcmd.controller.ParsedCommand;
import sqlcmd.model.JDBCDatabaseManager;

/**
 * Created by s.sheyko on 14.08.2017.
 */
public class Connect extends Command {
    @Override
    public void run(ParsedCommand parsedCommand) {
        manager = new JDBCDatabaseManager();
        String[] params = parsedCommand.getParameters();
        manager.connect(params[0],params[1],params[2]);
    }
}
