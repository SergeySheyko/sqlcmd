package sqlcmd.controller.command;

import sqlcmd.model.DatabaseManager;
import sqlcmd.view.View;

/**
 * Created by s.sheyko on 22.08.2017.
 */
public class Insert extends CommonCommand{
    public Insert(String[] commands, View view, DatabaseManager databaseManager) {
        super(commands, view, databaseManager);
    }

    @Override
    public void run() {
        checkArgsQty(commands,4,true);
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
}
