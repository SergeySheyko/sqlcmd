package sqlcmd.controller.command;

import sqlcmd.model.DataSet;
import sqlcmd.model.DatabaseManager;
import sqlcmd.view.View;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by s.sheyko on 22.08.2017.
 */
public abstract class CommonCommand {
    String[] commands;
    View view;
    DatabaseManager databaseManager;

    public CommonCommand(String[] commands, View view, DatabaseManager databaseManager) {
        this.commands = commands;
        this.view = view;
        this.databaseManager = databaseManager;
    }

    public static Map<String,Class<?>> getCommandTypes(){
        Map<String,Class<?>> commandTypes = new HashMap<>();
        commandTypes.put("connect",Connect.class);
        commandTypes.put("tables",Tables.class);
        commandTypes.put("clear",Clear.class);
        commandTypes.put("drop",Drop.class);
        commandTypes.put("create",Create.class);
        commandTypes.put("find",Find.class);
        commandTypes.put("insert",Insert.class);
        commandTypes.put("update",Update.class);
        commandTypes.put("delete",Delete.class);
        commandTypes.put("help",Help.class);
        commandTypes.put("exit",Exit.class);
        return commandTypes;
    }

    public abstract void run();

    public void checkArgsQty(String[] commands, int qty, boolean equalOrMore) {
        String moreThan = equalOrMore?" и более":"";
        if (commands.length!=qty && !equalOrMore || equalOrMore && commands.length<qty) throw new IllegalArgumentException(String.format("неверное количество параметров - требуется %d%s, обнаружено %d",qty,moreThan,commands.length));
    }

    public void displayTableData(DataSet dataSet) {
        String[] columns = dataSet.getColumns();
        List<Object[]> rows = dataSet.getRows();
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
        view.write(delimiterRow);
        view.write(String.format(formatLine,columns));
        view.write(delimiterRow);
        if (rows==null) return;
        for (Object[] row:rows){
            view.write(String.format(formatLine,row));
            view.write(delimiterRow);
        }
    }

}
