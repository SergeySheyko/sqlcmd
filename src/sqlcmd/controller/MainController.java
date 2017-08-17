package sqlcmd.controller;

import sqlcmd.model.DatabaseManager;
import sqlcmd.model.JDBCDatabaseManager;
import sqlcmd.view.Console;
import sqlcmd.view.View;

/**
 * Created by s.sheyko on 15.08.2017.
 */
public class MainController {
    public static void main(String[] args) {
        View view = new Console();
        DatabaseManager manager = new JDBCDatabaseManager();

        view.write("Hello, user!");
        view.write("Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: ИмяБазы|ИмяПользователя|Пароль");
        while (true){
            String string = view.read();
            String[] data = string.split("[|]");
            String databaseName = data[0];
            String userName = data[1];
            String password = data[2];
            try {
                manager.connect(databaseName,userName,password);
                break;
            }
            catch (Exception e){
                view.write("Неудача по причине:"+e.getMessage()+" "+e.getCause().getMessage());
                view.write("Повтори попытку");
                //
            }
        }
        view.write("Успех!");
    }
}
