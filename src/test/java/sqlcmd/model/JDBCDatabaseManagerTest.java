package sqlcmd.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class JDBCDatabaseManagerTest {
    private DatabaseManager manager;
    private final String TEST_TABLENAME = "user64531rhjbs";
    private String[] testColumns = {"id", "name", "password"};
    private boolean doNotDrop = false;

    @Before
    public void setup() {
        manager = new JDBCDatabaseManager();
        manager.connect("sqlcmd", "postgres", "postgres");
        manager.createTable(TEST_TABLENAME, testColumns);
    }

    @Test
    public void testGetTableNames() {
        ArrayList<String> tableNames = manager.getTablesList();
        assertEquals(String.format("[%s]", TEST_TABLENAME), tableNames.toString());
    }

    @Test
    public void testCreateTable() {
        DataSet dataSet = manager.getTableData(TEST_TABLENAME);
        assertEquals(Arrays.toString(testColumns), Arrays.toString(dataSet.getColumns()));
    }

    @Test
    public void testDropTable() {
        manager.dropTable(TEST_TABLENAME);
        ArrayList<String> tableNames = manager.getTablesList();
        assertEquals(false, tableNames.contains(TEST_TABLENAME));
        doNotDrop = true;
    }

    @Test
    public void testGetTableData() {
        DataSet input = new DataSet(testColumns);
        input.addRow(new Object[]{13, "Ivanov", "12345"});
        input.addRow(new Object[]{15, "Petrov", "qwerty"});
        input.addRow(new Object[]{17, "Sidorov", "789456123"});
        manager.insert(TEST_TABLENAME, input);

        DataSet users = manager.getTableData(TEST_TABLENAME);
        assertEquals(3, users.getRows().size());

        assertEquals("[id, name, password]", Arrays.toString(users.getColumns()));
        assertEquals("[13, Ivanov, 12345]", Arrays.toString(users.getRows().get(0)));
    }

    @Test
    public void testUpdateTableData() {
        DataSet input = new DataSet(testColumns);
        input.addRow(new Object[]{13, "Ivanov", "12345"});
        manager.insert(TEST_TABLENAME, input);
        String[] updatedColumns = {"name", "password"};
        String[] updatedValues = {"Jason", "OoOoOo"};
        manager.update(TEST_TABLENAME, "id", "13", updatedColumns, updatedValues);

        DataSet users = manager.getTableData(TEST_TABLENAME);

        assertEquals("[id, name, password]", Arrays.toString(users.getColumns()));
        assertEquals("[13, Jason, OoOoOo]", Arrays.toString(users.getRows().get(0)));
    }

    @After
    public void dropTable() {
        if (!doNotDrop) {
            manager.dropTable(TEST_TABLENAME);
        }
    }


}
