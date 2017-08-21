package sqlcmd.controller.command;

import sqlcmd.view.View;

/**
 * Created by s.sheyko on 21.08.2017.
 */
public interface Command {
    boolean canExecute();

    void run();
}
