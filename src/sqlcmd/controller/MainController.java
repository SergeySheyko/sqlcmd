package sqlcmd.controller;

import sqlcmd.model.DataSet;
import sqlcmd.model.DatabaseManager;
import sqlcmd.view.View;

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
                //else if (commands.length<2) view.write("Неверное количество параметров!");
                else if (commands[0].equals("connect")) connectToDatabase(commands);
                else if (commands[0].equals("clear")) clearTable(commands);
                else if (commands[0].equals("drop")) dropTable(commands);
                else if (commands[0].equals("create")) createTable(commands);
                else if (commands[0].equals("find")) getTableData(commands);
                else view.write("Неверная команда!");
            }
            catch (Exception e){
                printError(e);
            }
        }

    }

    private void getTableData(String[] commands) {
        checkArgsQty(commands,2);
        DataSet[] dataSet = databaseManager.getTableData(commands[1]);
        displayTableData(dataSet);
    }

    private void displayTableData(DataSet[] dataSet) {

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
        databaseManager.clear(commands[1]);
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
