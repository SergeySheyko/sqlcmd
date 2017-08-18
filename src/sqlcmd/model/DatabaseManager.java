package sqlcmd.model;

/**
 * Created by ser on 14.08.2017.
 */
public interface DatabaseManager {
    void connect(String databaseName, String userName, String password);

    void clear(String tableName);

    String[] getTablesList();

    void disconnect();

    void create(DataSet input, String tableName);

    DataSet[] getTableData(String tableName);

    void update(String tableName, int id, DataSet newValue);

    void createTable(String tableName, String[] columns);

    void dropTable(String tableName);

    String[] getColumnNames(String tableName);
}
