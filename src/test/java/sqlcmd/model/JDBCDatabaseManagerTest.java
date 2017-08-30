package sqlcmd.model;

import org.junit.Before;
import org.junit.Test;
import sqlcmd.model.JDBCDatabaseManager;
import sqlcmd.model.DatabaseManager;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * Created by s.sheyko on 01.08.2017.
 */
public class JDBCDatabaseManagerTest {
    private DatabaseManager manager;

    @Before
    public void setup(){
        manager = new JDBCDatabaseManager();
        manager.connect("sqlcmd","postgres","postgres");
    }

    @Test
    public void testGetTableNames(){
        ArrayList<String> tableNames = manager.getTablesList();
        assertEquals("[user]",tableNames.toString());
    }

    @Test
    public void testCreateTable() {
        manager.dropTable("user");
        String[] columns = {"id","name","password"};
        manager.createTable("user",columns);
        DataSet dataSet = manager.getTableData("user");
        assertEquals(Arrays.toString(columns),Arrays.toString(dataSet.getColumns()));
    }

    @Test
    public void testDropTable() {
        String[] columns = {"id","name","password"};
        manager.createTable("user",columns);
        manager.dropTable("user");
        ArrayList<String> tableNames = manager.getTablesList();
        assertEquals("[]",tableNames.toString());
    }

    @Test
    public void testGetTableData(){
        String tableName = "user";
        manager.delete(tableName,null,null);
        String[] columns = {"id","name","password"};
        DataSet input = new DataSet(columns);
        input.addRow(new Object[]{13,"Ivanov","12345"});
        input.addRow(new Object[]{13,"Petrov","qwerty"});
        input.addRow(new Object[]{13,"Sidorov","789456123"});
        manager.insert(tableName,input);

        DataSet users = manager.getTableData(tableName);
        assertEquals(3,users.getRows().size());

        assertEquals("[id, name, password]",Arrays.toString(users.getColumns()));
        assertEquals("[13, Ivanov, 12345]",Arrays.toString(users.getRows().get(0)));
    }

    @Test
    public void testGetColumnNames() {
//        String[] columnNames = manager.getColumnNames("user");
//        assertEquals("[name, password, id]",Arrays.toString(columnNames));
    }

    @Test
    public void testUpdateTableData(){
//        manager.clear("user");
//
//        DataSet input = new DataSet();
//        input.put("id","13");
//        input.put("name","Stiven");
//        input.put("password","pass");
//        manager.insert(input,"user");
//
//        DataSet newValue = new DataSet();
//        newValue.put("name","Stiv");
//        newValue.put("password","pass2");
//        manager.update("user",13,newValue);

//        DataSet[] users = manager.getTableData("user");
//        assertEquals(1,users.length);
//
//        DataSet user = users[0];
//        assertEquals("[id, name, password]",Arrays.toString(user.getNames()));
//        assertEquals("[13, Stiv, pass2]",Arrays.toString(user.getValues()));
    }


}
