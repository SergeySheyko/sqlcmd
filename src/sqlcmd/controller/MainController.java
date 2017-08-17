package sqlcmd.controller;

import sqlcmd.model.DatabaseManager;
import sqlcmd.model.JDBCDatabaseManager;
import sqlcmd.view.Console;
import sqlcmd.view.View;

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
        view.write("Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: ИмяБазы|ИмяПользователя|Пароль");
        while (true){
            try {
            String string = view.read();
            String[] data = string.split("[|]");
            if (data.length!=3){
                throw new IllegalArgumentException("Неудача по причине: неверное количество параметров - требуется 3, обнаружено "+data.length);
            }
            String databaseName = data[0];
            String userName = data[1];
            String password = data[2];
                databaseManager.connect(databaseName,userName,password);
                break;
            }
            catch (Exception e){
                printError(e);
            }
        }
        view.write("Успех!");
    }

    private void printError(Exception e) {
        String errorMessage = e.getMessage();
        if (e.getCause()!=null) errorMessage+=" "+e.getCause().getMessage();
        view.write("Неудача по причине: "+errorMessage);
        view.write("Повтори попытку");
    }

}
