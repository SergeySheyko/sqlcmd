package controller.command;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import sqlcmd.controller.command.Command;
import sqlcmd.controller.command.Find;
import sqlcmd.model.DatabaseManager;
import sqlcmd.view.View;

public class FindTest {
    private View view;
    private DatabaseManager databaseManager;

    @Before
    public void setup(){
        databaseManager = Mockito.mock(DatabaseManager.class);
        view = Mockito.mock(View.class);
    }

    @Test
    public void Test(){
        Command command = new Find(view, databaseManager);
        command.run(new String[]{"find","user"});
    }
}
