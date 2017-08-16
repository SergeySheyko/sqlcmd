import org.junit.Before;
import org.junit.Test;
import sqlcmd.model.DatabaseManager;
import sqlcmd.model.DataSet;
import sqlcmd.model.JDBCDatabaseManager;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * Created by s.sheyko on 01.08.2017.
 */
public class TestDataBaseManager {
    private DatabaseManager manager;

    @Before
    public void setup(){
        manager = new JDBCDatabaseManager();
        manager.connect("sqlcmd","postgres","aassa");
    }

    @Test
    public void testGetTableNames(){
        String[] tableNames = manager.getTablesList();
        assertEquals("[user]",Arrays.toString(tableNames));
    }

    @Test
    public void testCreateTable() {
        manager.dropTable("user");
        String[] columns = {"id","name","password"};
        manager.createTable("user",columns);
        DataSet[] data = manager.getTableData("user");
    }

    @Test
    public void testDropTable() {
        manager.dropTable("user");
    }

    @Test
    public void testGetTableData(){
        manager.clear("user");
        DataSet input = new DataSet();
        input.put("id","13");
        input.put("name","Stiven");
        input.put("password","pass");
        manager.create(input,"user");

        DataSet[] users = manager.getTableData("user");
        assertEquals(1,users.length);

        DataSet user = users[0];
        assertEquals("[id, name, password]",Arrays.toString(user.getNames()));
        assertEquals("[13, Stiven, pass]",Arrays.toString(user.getValues()));
    }

    @Test
    public void testGetColumnNames() {
        String[] columnNames = manager.getColumnNames("user");
        assertEquals("[name, password, id]",Arrays.toString(columnNames));
    }

    @Test
    public void testUpdateTableData(){
        manager.clear("user");

        DataSet input = new DataSet();
        input.put("id","13");
        input.put("name","Stiven");
        input.put("password","pass");
        manager.create(input,"user");

        DataSet newValue = new DataSet();
        newValue.put("name","Stiv");
        newValue.put("password","pass2");
        manager.update("user",13,newValue);

        DataSet[] users = manager.getTableData("user");
        assertEquals(1,users.length);

        DataSet user = users[0];
        assertEquals("[id, name, password]",Arrays.toString(user.getNames()));
        assertEquals("[13, Stiv, pass2]",Arrays.toString(user.getValues()));
    }


}
