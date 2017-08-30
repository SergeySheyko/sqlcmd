package sqlcmd.controller.command;

import sqlcmd.model.DataSet;
import sqlcmd.model.DatabaseManager;
import sqlcmd.view.View;

import java.util.Arrays;

/**
 * Created by s.sheyko on 22.08.2017.
 */
public class Help extends AbstractCommand implements Command {

    public Help(View view, DatabaseManager databaseManager) {
        super(view, databaseManager);
    }

    @Override
    public void run(String[] commands) {
        view.write("Доступны следующие команды:");

        String[] headers = {"Название команды","Описание","Формат"};
        DataSet dataSet = new DataSet(headers);
        dataSet.addRow(new String[]{"сonnect","Подключение к БД","connect|database|username|password"});
        dataSet.addRow(new String[]{"tables","Вывод списка таблиц","tables"});
        dataSet.addRow(new String[]{"clear","Очистка таблицы","clear|tableName"});
        dataSet.addRow(new String[]{"drop","Удаление таблицы","drop|tableName"});
        dataSet.addRow(new String[]{"create","Создание новой таблицы (id создается автоматически)","create|tableName|column1|column2|...|columnN"});
        dataSet.addRow(new String[]{"find","Получение содержимого таблицы","find|tableName"});
        dataSet.addRow(new String[]{"insert","Добавление строки в таблицу","insert|tableName|column1|value1|column2|value2|...|columnN|valueN"});
        dataSet.addRow(new String[]{"help","Вывод списка команд","help"});
        dataSet.addRow(new String[]{"exit","Отключение от БД и выход из приложения","exit"});

        displayTableData(dataSet,view);
    }

    @Override
    public boolean exit() {
        return false;
    }

}
