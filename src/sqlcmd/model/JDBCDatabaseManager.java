package sqlcmd.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by s.sheyko on 01.08.2017.
 */
public class JDBCDatabaseManager implements DatabaseManager {
    private Connection connection;

    @Override
    public void connect(String databaseName, String userName, String password){

        String url ="jdbc:postgresql://localhost:5432/" + databaseName;

        try {
            Class.forName("org.postgresql.Driver");

        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver is not found!", e);
        }
        try {
            Logger logger = Logger.getLogger("org.postgresql.Driver");
            logger.setLevel(Level.OFF );
            connection = DriverManager.getConnection(url, userName, password);
        } catch (SQLException e) {
            connection = null;
            throw new RuntimeException("Can't connect to database", e);
        }
    }

    @Override
    public void disconnect(){
        if (connection!=null) try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error while disconnecting from database", e);
        }
    }

    @Override
    public void delete(String tableName,String column,String value){
        String sql = "DELETE FROM public."+tableName;
        if (column!=null) sql += " WHERE "+column+"=?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)){
            if (column!=null){
                if (column.equals("id")) pstmt.setInt(1,Integer.parseInt(value));
                else pstmt.setString(1,value);
            }
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error while deleting from a table "+tableName, e);
        }
    }

    @Override
    public String[] getTablesList() {
        if (connection==null) throw new RuntimeException("Соединение с базой не установлено!");
        String sql = "SELECT table_name FROM information_schema.tables WHERE table_schema='public' AND table_type='BASE TABLE'";
        try (Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);){
            ArrayList<String> tables = new ArrayList<>();
            while (rs.next()){
                tables.add(rs.getString("table_name"));
            }
            return tables.toArray(new String[tables.size()]);
        }catch (SQLException e){
            throw new RuntimeException("Error while getting tables list!",e);
        }
    }

    @Override
    public int insert(String[] columns, String[] values, String tableName) {
        String sql = "Insert into public."+tableName+" ("+getPreparedNames(columns)+") VALUES ("+getPreparedValues(values)+")";
        try (Statement stmt = connection.createStatement();){
            return stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Error while inserting data!",e);
        }
    }

    private String getPreparedNames(String[] names){
        String result = "";
        for (String name:names) result += name+",";
        return result.substring(0,result.length()-1);
    }

    private String getPreparedValues(Object[] values){
        String result = "";
        for (Object value:values) result += "'"+value.toString()+"',";
        return result.substring(0,result.length()-1);
    }

    @Override
    public DataSet getTableData(String tableName) {
        if (connection==null) throw new RuntimeException("Connection is not established!");
        DataSet data = null;
        String sql = "SELECT * FROM public."+tableName;
        try (Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);) {
            ResultSetMetaData rsmd = rs.getMetaData();
            String[] columns = new String[rsmd.getColumnCount()];
            for (int i=1;i<=columns.length;i++) columns[i-1] = rsmd.getColumnName(i);
            data = new DataSet(columns);
            while (rs.next()) {
                Object[] row = new Object[columns.length];
                for (int i=1;i<=columns.length;i++) {
                    row[i-1] = rs.getObject(i);}
                data.addRow(row);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error in SELECT operation!",e);
        }
        return data;
    }

    @Override
    public void update(String tableName, String checkedColumn, String checkedValue, String[] updatedColumns, String[] updatedValues) {
        String sql = "Update public."+tableName+" Set ";
        for (int i=0;i<updatedColumns.length;i++){
            sql += updatedColumns[i]+" = ?,";
        }
        sql = sql.substring(0,sql.length()-1)+" Where "+checkedColumn+"=?";
        try (PreparedStatement psmt = connection.prepareStatement(sql);){
            for (int i=0;i<updatedValues.length;i++){
                psmt.setString(i+1,updatedValues[i]);
            }
            if (checkedColumn.equals("id")) psmt.setInt(updatedValues.length+1,Integer.parseInt(checkedValue));
            else psmt.setString(updatedValues.length+1,checkedValue);
            psmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error while operating update statement!",e);
        }
    }

    public void dropTable(String tableName) {
        String sql = "DROP TABLE public."+tableName;
        try (PreparedStatement pstmt = connection.prepareStatement(sql);){
            pstmt.executeUpdate();
        }catch (SQLException e){
            throw new RuntimeException("Error while deleting the table "+tableName, e);
        }
    }

    @Override
    public void createTable(String tableName, String[] columns) {
        if (connection==null) return;
        String sql = "CREATE TABLE public."+tableName+" (id SERIAL NOT NULL PRIMARY KEY,";
        for (String column:columns) {
            if(!column.toLowerCase().equals("id")) sql+=column+" TEXT,";
        }
        sql = sql.substring(0,sql.length()-1);
        sql += ")";
        try (PreparedStatement pstmt = connection.prepareStatement(sql);){
            pstmt.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

}
