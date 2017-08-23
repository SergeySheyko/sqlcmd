package sqlcmd.controller.command;

import sqlcmd.model.DataSet;
import sqlcmd.view.View;

import java.util.List;

/**
 * Created by s.sheyko on 23.08.2017.
 */
public interface Command {
    void run(String[] commands);

    default void checkArgsQty(String[] commands, int qty, boolean equalOrMore) {
        String moreThan = equalOrMore?" и более":"";
        if (commands.length!=qty && !equalOrMore || equalOrMore && commands.length<qty) throw new IllegalArgumentException(String.format("неверное количество параметров - требуется %d%s, обнаружено %d",qty,moreThan,commands.length));
    }

    default void displayTableData(DataSet dataSet, View view) {
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
