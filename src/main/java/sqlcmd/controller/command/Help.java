package sqlcmd.controller.command;

import sqlcmd.model.DatabaseManager;
import sqlcmd.view.View;

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
        view.write("сonnect - подключение к БД");
        view.write("\t формат: connect|database|username|password");
        view.write("tables - Команда выводит список всех таблиц");
        view.write("\t Формат: tables (без параметров)");
        view.write("clear - Команда очищает содержимое указанной (всей) таблицы");
        view.write("\t Формат: clear|tableName");
        view.write("drop - Команда удаляет заданную таблицу");
        view.write("\t Формат: drop|tableName");
        view.write("create - Команда создает новую таблицу с заданными полями");
        view.write("\t Формат: create|tableName|column1|column2|...|columnN");
        view.write("find - Команда для получения содержимого указанной таблицы");
        view.write("\t Формат: find|tableName");
        view.write("insert - Команда для вставки одной строки в заданную таблицу");
        view.write("\t Формат: insert|tableName|column1|value1|column2|value2|...|columnN|valueN");
        view.write("update - Команда обновит запись, установив значение column2 = value2, для которой соблюдается условие column1 = value1");
        view.write("\t Формат: update|tableName|column1|value1|column2|value2");
        view.write("delete - Команда удаляет одну или несколько записей для которых соблюдается условие column = value");
        view.write("\t Формат: delete|tableName|column|value");
        view.write("help - Команда выводит в консоль список всех доступных команд");
        view.write("\t Формат: help (без параметров)");
        view.write("exit - Команда для отключения от БД и выход из приложения");
        view.write("\t Формат: exit (без параметров)");

    }

}
