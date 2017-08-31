package sqlcmd.model;

import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * Created by s.sheyko on 01.08.2017.
 */
public class JDBCDatabaseManagerTest {
    private DatabaseManager manager;
    private final String DEFAULTTABLENAME = "user";

    @Before
    public void setup(){
        manager = new JDBCDatabaseManager();
        manager.connect("sqlcmd","postgres","postgres");
    }

    @Test
    public void testGetTableNames(){
        ArrayList<String> tableNames = manager.getTablesList();
        assertEquals(String.format("[%s]",DEFAULTTABLENAME),tableNames.toString());
    }

    @Test
    public void testCreateTable() {
        manager.dropTable(DEFAULTTABLENAME);
        String[] columns = {"id","name","password"};
        manager.createTable(DEFAULTTABLENAME,columns);
        DataSet dataSet = manager.getTableData(DEFAULTTABLENAME);
        assertEquals(Arrays.toString(columns),Arrays.toString(dataSet.getColumns()));
    }

    @Test
    public void testDropTable() {
        String[] columns = {"id","name","password"};
        manager.createTable(DEFAULTTABLENAME,columns);
        manager.dropTable(DEFAULTTABLENAME);
        ArrayList<String> tableNames = manager.getTablesList();
        assertEquals("[]",tableNames.toString());
    }

    @Test
    public void testGetTableData(){
        manager.delete(DEFAULTTABLENAME,null,null);
        String[] columns = {"id","name","password"};
        DataSet input = new DataSet(columns);
        input.addRow(new Object[]{13,"Ivanov","12345"});
        input.addRow(new Object[]{15,"Petrov","qwerty"});
        input.addRow(new Object[]{17,"Sidorov","789456123"});
        manager.insert(DEFAULTTABLENAME,input);

        DataSet users = manager.getTableData(DEFAULTTABLENAME);
        assertEquals(3,users.getRows().size());

        assertEquals("[id, name, password]",Arrays.toString(users.getColumns()));
        assertEquals("[13, Ivanov, 12345]",Arrays.toString(users.getRows().get(0)));
    }

    @Test
    public void testUpdateTableData(){
        manager.delete(DEFAULTTABLENAME,null,null);
        String[] columns = {"id","name","password"};
        DataSet input = new DataSet(columns);
        input.addRow(new Object[]{13,"Ivanov","12345"});
        manager.insert(DEFAULTTABLENAME,input);
        String[] updatedColumns = {"name","password"};
        String[] updatedValues = {"Jason","OoOoOo"};
        manager.update(DEFAULTTABLENAME,"id","13",updatedColumns,updatedValues);

        DataSet users = manager.getTableData(DEFAULTTABLENAME);

        assertEquals("[id, name, password]",Arrays.toString(users.getColumns()));
        assertEquals("[13, Jason, OoOoOo]",Arrays.toString(users.getRows().get(0)));
    }


}
