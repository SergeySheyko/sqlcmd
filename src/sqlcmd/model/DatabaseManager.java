package sqlcmd.model;

/**
 * Created by ser on 14.08.2017.
 */
public interface DatabaseManager {
    void connect(String databaseName, String userName, String password);

    void delete(String tableName,String column,String value);

    String[] getTablesList();

    void disconnect();

    int insert(String[] columns, String[] values, String tableName);

    DataSet getTableData(String tableName);

    void update(String tableName, String checkedColumn, String checkedValue, String[] updatedColumns, String[] updatedValues);

    void createTable(String tableName, String[] columns);

    void dropTable(String tableName);

}
