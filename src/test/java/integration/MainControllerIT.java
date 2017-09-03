package integration;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import sqlcmd.controller.Main;
import sqlcmd.model.DatabaseManager;
import sqlcmd.model.JDBCDatabaseManager;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

public class MainControllerIT {
    private ConfigurableInputStream in;
    private ByteArrayOutputStream out;
    private DatabaseManager databaseManager;

    @Before
    public void setup(){
        databaseManager = new JDBCDatabaseManager();
        out = new ByteArrayOutputStream();
        in = new ConfigurableInputStream();

        System.setIn(in);
        System.setOut(new PrintStream(out));
    }

    @Test
    public void testHelp() {
        // given
        in.add("help");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Привет!\r\n" +
                "Введи команду или help для помощи:\r\n" +
                "Доступны следующие команды:\r\n" +
                "+------------------+-----------------------------------------------------+-------------------------------------------------------------------+\r\n" +
                "| Название команды | Описание                                            | Формат                                                            |\r\n" +
                "+------------------+-----------------------------------------------------+-------------------------------------------------------------------+\r\n" +
                "| сonnect          | Подключение к БД                                    | connect|database|username|password                                |\r\n" +
                "+------------------+-----------------------------------------------------+-------------------------------------------------------------------+\r\n" +
                "| tables           | Вывод списка таблиц                                 | tables                                                            |\r\n" +
                "+------------------+-----------------------------------------------------+-------------------------------------------------------------------+\r\n" +
                "| clear            | Очистка таблицы                                     | clear|tableName                                                   |\r\n" +
                "+------------------+-----------------------------------------------------+-------------------------------------------------------------------+\r\n" +
                "| drop             | Удаление таблицы                                    | drop|tableName                                                    |\r\n" +
                "+------------------+-----------------------------------------------------+-------------------------------------------------------------------+\r\n" +
                "| create           | Создание новой таблицы (id создается автоматически) | create|tableName|column1|column2|...|columnN                      |\r\n" +
                "+------------------+-----------------------------------------------------+-------------------------------------------------------------------+\r\n" +
                "| find             | Получение содержимого таблицы                       | find|tableName                                                    |\r\n" +
                "+------------------+-----------------------------------------------------+-------------------------------------------------------------------+\r\n" +
                "| insert           | Добавление строки в таблицу                         | insert|tableName|column1|value1|column2|value2|...|columnN|valueN |\r\n" +
                "+------------------+-----------------------------------------------------+-------------------------------------------------------------------+\r\n" +
                "| help             | Вывод списка команд                                 | help                                                              |\r\n" +
                "+------------------+-----------------------------------------------------+-------------------------------------------------------------------+\r\n" +
                "| exit             | Отключение от БД и выход из приложения              | exit                                                              |\r\n" +
                "+------------------+-----------------------------------------------------+-------------------------------------------------------------------+\r\n" +
                "Введи команду или help для помощи:\r\n" +
                "До свидания!\r\n", getData());
    }

    public String getData() {
        try {
            String result = new String(out.toByteArray(), "UTF-8");
            out.reset();
            return result;
        } catch (UnsupportedEncodingException e) {
            return e.getMessage();
        }
    }
}
