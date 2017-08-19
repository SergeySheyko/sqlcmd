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
    public void clear(String tableName) {
        String sql = "DELETE FROM public."+tableName;
        int affectedRows = 0;
        try (PreparedStatement psmt = connection.prepareStatement(sql)){
            affectedRows = psmt.executeUpdate();
//            System.out.println(tableName+" was cleared. Deleted "+affectedRows+" rows");
        } catch (SQLException e) {
            throw new RuntimeException("Error while clearing the table "+tableName, e);
        }
    }

    @Override
    public String[] getTablesList() {
        if (connection==null) throw new RuntimeException("Соединение с базой не установлено!");
        //todo: realize using collections
        String sql = "SELECT table_name FROM information_schema.tables WHERE table_schema='public' AND table_type='BASE TABLE'";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql);){
            String[] tables = new String[100];
            int index = 0;
            while (rs.next()){
                tables[index++] =rs.getString("table_name");
            }
            tables = Arrays.copyOf(tables,index,String[].class);
            return tables;
        }catch (SQLException e){
            e.printStackTrace();
            return new String[0];
        }
    }


    @Override
    public int insert(DataSet input, String tableName) {
        String[] names = input.getNames();
        Object[] values = input.getValues();
        String sql = "Insert into public."+tableName+" ("+getPreparedNames(names)+") VALUES ("+getPreparedValues(values)+")";
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
    public newDataSet getTableData(String tableName) {
        if (connection==null) throw new RuntimeException("Connection is not established!");
        newDataSet data = null;
        String sql = "SELECT * FROM public."+tableName;
        try (Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);) {
            ResultSetMetaData rsmd = rs.getMetaData();
            String[] columns = new String[rsmd.getColumnCount()];
//            int index=0;
            for (int i=1;i<=columns.length;i++) columns[i-1] = rsmd.getColumnName(i);
            data = new newDataSet(columns);
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
    public void update(String tableName, int id, DataSet newValue) {
        String[] names = newValue.getNames();
        Object[] values = newValue.getValues();
        String sql = "Update public."+tableName+" Set ";
        for (int i=0;i<names.length;i++){
            sql += names[i]+" = ?,";
        }
        sql = sql.substring(0,sql.length()-1)+" Where id=?";
        try (PreparedStatement psmt = connection.prepareStatement(sql);){
            for (int i=0;i<values.length;i++){
                psmt.setString(i+1,values[i].toString());
            }
            psmt.setInt(values.length+1,id);
            psmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
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

    public String[] getColumnNames(String tableName) {
        List<String> names = new ArrayList<>();
        String sql = "SELECT * FROM public."+tableName;
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql);){
            ResultSetMetaData metaData = rs.getMetaData();
            for (int i=1;i<=metaData.getColumnCount();i++){
                names.add(metaData.getColumnName(i));
            }
            return names.toArray(new String[names.size()]);
        } catch (SQLException e) {
            e.printStackTrace();
            return new String[0];
        }
    }
}
