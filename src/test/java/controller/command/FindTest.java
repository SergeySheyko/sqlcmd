package controller.command;

import org.junit.Before;
import org.junit.Test;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import sqlcmd.controller.command.Command;
import sqlcmd.controller.command.Find;
import sqlcmd.model.DataSet;
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
        DataSet input = new DataSet(new String[]{"id","name","password"});
        input.addRow(new Object[]{13, "Ivanov", "12345"});
        input.addRow(new Object[]{15, "Petrov", "qwerty"});
        input.addRow(new Object[]{17, "Sidorov", "789456123"});
        Mockito.when(databaseManager.getTableData("user")).thenReturn(input);

        command.run(new String[]{"find","user"});

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(view, Mockito.atLeastOnce()).write(captor.capture());
        assertEquals("",captor.getAllValues().toString());
    }
}
