package sqlcmd.controller;

import sqlcmd.model.DatabaseManager;
import sqlcmd.model.DataSet;
import sqlcmd.view.View;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by s.sheyko on 15.08.2017.
 */
public class MainController {
    private View view;
    private DatabaseManager databaseManager;

    public MainController(View view, DatabaseManager databaseManager) {
        this.view = view;
        this.databaseManager = databaseManager;
    }

    public void run() {
        view.write("Hello, user!");
        while (true){
            try {
                view.write("Введи команду или help для помощи:");
                String commandLine = view.read();
                String[] commands = commandLine.split("[|]");
                if (commandLine.equals("exit")){
                    databaseManager.disconnect();
                    view.write("До свидания!");
                    break;
                }
                else if (commandLine.equals("help")) writeHelp();
                else if (commandLine.equals("tables")) getTables();
                else if (commands[0].equals("connect")) connectToDatabase(commands);
                else if (commands[0].equals("clear")) clearTable(commands);
                else if (commands[0].equals("drop")) dropTable(commands);
                else if (commands[0].equals("create")) createTable(commands);
                else if (commands[0].equals("find")) getTableData(commands);
                else if (commands[0].equals("insert")) insertData(commands);
                else if (commands[0].equals("update")) updateData(commands);
                else if (commands[0].equals("delete")) deleteData(commands);
                else view.write("Неверная команда!");
            }
            catch (Exception e){
                printError(e);
            }
        }

    }

    private void deleteData(String[] commands) {
        checkArgsQty(commands,4);
        String tableName = commands[1];
        String checkedColumn = commands[2];
        String checkedValue = commands[3];
        databaseManager.delete(tableName,checkedColumn,checkedValue);
        getTableData(Arrays.copyOf(commands,2));
    }

    private void updateData(String[] commands) {
        if (commands.length<6) throw new IllegalArgumentException("неверное количество параметров - требуется 6 и более, обнаружено "+commands.length);
        if (commands.length%2!=0) throw new IllegalArgumentException("неверное количество параметров - требуется четное количество!");
        String tableName = commands[1];
        String checkedColumn = commands[2];
        String checkedValue = commands[3];
        String[] columns = new String[(commands.length-4)/2];
        String[] values = new String[(commands.length-4)/2];
        int columnsIndex = 0;
        int valuesIndex = 0;
        for (int i=4;i<commands.length;i++){
            if (i%2==0) columns[columnsIndex++] = commands[i];
            else values[valuesIndex++] = commands[i];
        }
        databaseManager.update(tableName,checkedColumn,checkedValue,columns,values);
        getTableData(Arrays.copyOf(commands,2));
    }

    private void insertData(String[] commands) {
        if (commands.length<4) throw new IllegalArgumentException("неверное количество параметров - требуется 4 и более, обнаружено "+commands.length);
        if (commands.length%2!=0) throw new IllegalArgumentException("неверное количество параметров - требуется четное количество!");
        String tableName = commands[1];
        String[] columns = new String[(commands.length-2)/2];
        String[] values = new String[(commands.length-2)/2];
        int columnsIndex = 0;
        int valuesIndex = 0;
        for (int i=2;i<commands.length;i++){
            if (i%2==0) columns[columnsIndex++] = commands[i];
            else values[valuesIndex++] = commands[i];
        }
        view.write("добавлено "+databaseManager.insert(columns,values, tableName)+" строк");
    }

    private void getTableData(String[] commands) {
        checkArgsQty(commands,2);
        DataSet dataSet = databaseManager.getTableData(commands[1]);
        displayTableData(dataSet);
    }

    private void displayTableData(DataSet dataSet) {
        String[] columns = dataSet.getColumns();
        ArrayList<Object[]> rows = dataSet.getRows();
        String delimiterRow = "+";
        String formatLine = "|";
        for (int i=0;i<columns.length;i++){
            int maxLen =columns[i].length();
            if (rows!=null){
                for (Object[] row:rows){
                    if (row[i].toString().length()>maxLen) maxLen =row[i].toString().length();
                }
            }
            formatLine += " %-"+maxLen+"s |";
            delimiterRow += "-";
            for (int j=0;j<maxLen;j++) delimiterRow += "-";
            delimiterRow += "-+";
        }
        System.out.println(delimiterRow);
        System.out.format(formatLine,columns);
        System.out.println();
        System.out.println(delimiterRow);
        if (rows==null) return;
        for (Object[] row:rows){
            System.out.format(formatLine,row);
            System.out.println();
            System.out.println(delimiterRow);
        }
    }

    private void createTable(String[] commands) {
        String tablename;
        String[] columns = new String[commands.length-2];
        if (commands.length<3) throw new IllegalArgumentException("неверное количество параметров - требуется 3 и более, обнаружено "+commands.length);
        tablename = commands[1];
        int index = 0;
        for (int i=2;i<commands.length;i++){
            columns[index++] = commands[i];
        }
        databaseManager.createTable(tablename,columns);
    }

    private void dropTable(String[] commands) {
        checkArgsQty(commands,2);
        databaseManager.dropTable(commands[1]);
    }

    private void clearTable(String[] commands) {
        checkArgsQty(commands,2);
        databaseManager.delete(commands[1],null,null);
    }


    private void getTables() {
        view.write(Arrays.toString(databaseManager.getTablesList()));
    }

    private void writeHelp() {
        //todo
        view.write("Help:");
    }

    private void connectToDatabase(String[] commands) {
        checkArgsQty(commands,4);
        try {
            String databaseName = commands[1];
            String userName = commands[2];
            String password = commands[3];
            databaseManager.connect(databaseName,userName,password);
        } catch (Exception e){
            printError(e);
        }
        view.write("Соединение успешно!");
    }

    private void printError(Exception e) {
        String errorMessage = e.getMessage();
        if (e.getCause()!=null) errorMessage+=" "+e.getCause().getMessage();
        view.write("Неудача по причине: "+errorMessage);
        view.write("Повтори попытку");
    }
    private void checkArgsQty(String[] commands, int qty) {
        if (commands.length!=qty) throw new IllegalArgumentException("неверное количество параметров - требуется 2, обнаружено "+commands.length);
    }

}
