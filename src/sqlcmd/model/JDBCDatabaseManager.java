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

    public Connection getConnection(){return connection;}

    @Override
    public void clear(String tableName) {
        String sql = "DELETE FROM public."+tableName;
        int affectedRows = 0;
        try (PreparedStatement psmt = connection.prepareStatement(sql)){
            affectedRows = psmt.executeUpdate();
            System.out.println(tableName+" was cleared. Deleted "+affectedRows+" rows");
        } catch (SQLException e) {
            System.out.println("Can't clear table!");
            e.printStackTrace();
        }
    }

    @Override
    public String[] getTablesList() {
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

    public void disconnect(){
        if (connection!=null) try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void create(DataSet input, String tableName) {
        String[] names = input.getNames();
        Object[] values = input.getValues();
        String sql = "Insert into public."+tableName+" ("+getPreparedNames(names)+") VALUES ("+getPreparedValues(values)+")";
        try (Statement stmt = connection.createStatement();){
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
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
    public DataSet[] getTableData(String tableName) {
        if (connection==null) return new DataSet[0];
        int size = getSize(tableName);
        DataSet[] data = new DataSet[size];
        int index=0;
        String sql = "SELECT * FROM public."+tableName;
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql);) {
            while (rs.next()) {
                ResultSetMetaData rsmd = rs.getMetaData();
                DataSet dataSet = new DataSet();
                data[index++] = dataSet;
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    dataSet.put(rsmd.getColumnName(i), rs.getObject(i));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    private int getSize(String tableName) {
        String sql = "SELECT COUNT (*) FROM "+tableName;
        try (Statement stmt = connection.createStatement();){
            ResultSet rs = stmt.executeQuery(sql);
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
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
        if (connection==null) return;
        String sql = "DROP TABLE public."+tableName;
        try (PreparedStatement pstmt = connection.prepareStatement(sql);){
            pstmt.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
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
