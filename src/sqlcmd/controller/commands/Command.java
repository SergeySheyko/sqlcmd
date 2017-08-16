package sqlcmd.controller.commands;

import sqlcmd.controller.ParsedCommand;
import sqlcmd.controller.Settings;
import sqlcmd.model.JDBCDatabaseManager;
import sqlcmd.view.View;

/**
 * Created by s.sheyko on 03.08.2017.
 */
public abstract class Command implements Settings {
    public JDBCDatabaseManager manager;
    public View view;

    public abstract void run(ParsedCommand parsedCommand);
}
