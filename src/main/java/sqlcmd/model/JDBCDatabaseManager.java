package sqlcmd.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by s.sheyko on 01.08.2017.
 */
public class JDBCDatabaseManager implements DatabaseManager {
    private Connection connection;
    private final String DRIVERNAME = "org.postgresql.Driver";
    private final String URL = "jdbc:postgresql://localhost:5432/";

    @Override
    public void connect(String databaseName, String userName, String password) {
        try {
            Class.forName(DRIVERNAME);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Драйвер не найден!", e);
        }
        try {
            Logger logger = Logger.getLogger(DRIVERNAME);
            logger.setLevel(Level.OFF);
            connection = DriverManager.getConnection(URL + databaseName, userName, password);
        } catch (SQLException e) {
            connection = null;
            throw new RuntimeException("Не удается подключиться к базе", e);
        }
    }

    @Override
    public void disconnect() {
        if (connection != null) try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при отключении от базы", e);
        }
    }

    @Override
    public void delete(String tableName, String column, String value) {
        checkConnection();
        String whereStatement = "";
        if (column != null) {
            whereStatement = String.format(" WHERE %s=?",column);
        }
        String sql = String.format("DELETE FROM public.%s %s",tableName,whereStatement);
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            if (column != null) {
                if (column.equals("id")) {
                    preparedStatement.setInt(1, Integer.parseInt(value));
                } else {
                    preparedStatement.setString(1, value);
                }
            }
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при очистке таблицы " + tableName, e);
        }
    }

    @Override
    public ArrayList<String> getTablesList() {
        checkConnection();
        String sql = "SELECT table_name FROM information_schema.tables WHERE table_schema='public' AND table_type='BASE TABLE'";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            ArrayList<String> tables = new ArrayList<>();
            while (resultSet.next()) {
                tables.add(resultSet.getString("table_name"));
            }
            return tables;
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении списка таблиц!", e);
        }
    }

    @Override
    public int insert(String tableName, DataSet dataSet) {
        checkConnection();
        int result = 0;
        String[] columns = dataSet.getColumns();
        ArrayList<Object[]> values = dataSet.getRows();
        String preparedColumns = getPreparedNames(columns);
        for (Object[] row : values) {
            String preparedValues = getPreparedValues(row);
            String sql = String.format("INSERT INTO public.%s (%s) VALUES (%s)", tableName, preparedColumns, preparedValues);
            try (Statement statement = connection.createStatement()) {
                result += statement.executeUpdate(sql);
            } catch (SQLException e) {
                throw new RuntimeException("Ошибка при добавлении данных", e);
            }
        }
        return result;
    }

    private String getPreparedNames(String[] names) {
        String result = "";
        for (String name : names) {
            if (result.length() > 0) {
                result += ",";
            }
            result += name;
        }
        return result;
    }

    private String getPreparedValues(Object[] values) {
        String result = "";
        for (Object value : values) {
            if (result.length() > 0) {
                result += ",";
            }
            result += "'" + value.toString() + "'";
        }
        return result;
    }

    @Override
    public DataSet getTableData(String tableName) {
        checkConnection();
        DataSet data = null;
        String sql = "SELECT * FROM public." + tableName;
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            String[] columns = new String[resultSetMetaData.getColumnCount()];
            for (int i = 1; i <= columns.length; i++) {
                columns[i - 1] = resultSetMetaData.getColumnName(i);
            }
            data = new DataSet(columns);
            while (resultSet.next()) {
                Object[] row = new Object[columns.length];
                for (int i = 1; i <= columns.length; i++) {
                    row[i - 1] = resultSet.getObject(i);
                }
                data.addRow(row);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка в операции SELECT!", e);
        }
        return data;
    }

    private void checkConnection() {
        if (connection == null) {
            throw new RuntimeException("Соединение с базой не установлено!");
        }
    }

    @Override
    public void update(String tableName, String checkedColumn, String checkedValue, String[] updatedColumns, String[] newValues) {
        checkConnection();
        String setStatement = "";
        for (String updatedColumn : updatedColumns) {
            if (setStatement.length()!=0){
                setStatement+=",";
            }
            setStatement += updatedColumn + " = ?";
        }
        String sql = String.format("UPDATE public.%s Set %s WHERE %s=?",tableName,setStatement,checkedColumn);
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            for (int i = 0; i < newValues.length; i++) {
                preparedStatement.setString(i + 1, newValues[i]);
            }
            if (checkedColumn.equals("id")) {
                preparedStatement.setInt(newValues.length + 1, Integer.parseInt(checkedValue));
            } else {
                preparedStatement.setString(newValues.length + 1, checkedValue);
            }
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка в операции UPDATE!", e);
        }
    }

    @Override
    public void dropTable(String tableName) {
        checkConnection();
        String sql = String.format("DROP TABLE public.%s",tableName);
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при удалении таблицы " + tableName, e);
        }
    }

    @Override
    public void createTable(String tableName, String[] columns) {
        checkConnection();
        String columnsList = "";
        for (String column : columns) {
            if (!column.toLowerCase().equals("id")) {
                columnsList += ","+column + " TEXT";
            }
        }
        String sql = String.format("CREATE TABLE IF NOT EXISTS public.%s (id SERIAL NOT NULL PRIMARY KEY%s)",tableName,columnsList);
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при создании таблицы " + tableName, e);
        }
    }

}
