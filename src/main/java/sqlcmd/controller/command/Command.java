package sqlcmd.controller.command;

import sqlcmd.model.DataSet;
import sqlcmd.view.View;

import java.util.List;

public interface Command {

    void run(String[] commands);
}
