package habitApp.database;

import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.DatabaseConnection;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Database {
    private static final String JDBC_URL;
    private static final String USERNAME;
    private static final String PASSWORD;

    private static final String LIQUIBASE_CHANGELOG;

    static {
        Properties properties = new Properties();
        try (FileInputStream input = new FileInputStream("application.properties")) {
            properties.load(input);
            JDBC_URL = properties.getProperty("jdbc.url");
            USERNAME = properties.getProperty("jdbc.username");
            PASSWORD = properties.getProperty("jdbc.password");
            LIQUIBASE_CHANGELOG = properties.getProperty("jdbc.changelog");
        } catch (IOException e) {
            throw new RuntimeException("Could not load database properties", e);
        }
    }

    public static void runDB() {
        // Создаем подключение к базе данных
        try (Connection connection = connectToDatabase()) {
            System.out.println("Connected to the database.");

            // Запускаем миграции Liquibase
            runLiquibaseMigrations(connection);

        } catch (SQLException | LiquibaseException e) {
            // Выводим полный стек ошибок
//            e.printStackTrace();
            System.out.println("Exception: " + e.getMessage());
        }
    }

    /**
     * Подключение к базе данных PostgreSQL
     * @return Connection объект подключения к базе данных
     * @throws SQLException в случае ошибок подключения
     */
    public static Connection connectToDatabase() throws SQLException {
        System.out.println("Attempting to connect to database...");
        Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
        System.out.println("Successfully connected to the database.");
        return connection;
    }

    /**
     * Запуск миграций Liquibase
     *
     * @param connection объект подключения к базе данных
     * @throws LiquibaseException в случае ошибок Liquibase
     */
    private static void runLiquibaseMigrations(Connection connection) throws LiquibaseException {
        System.out.println("Starting Liquibase migrations...");
        // Создаем объект Liquibase для управления миграциями
        DatabaseConnection liquibaseConnection = new JdbcConnection(connection);
        liquibase.database.Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(liquibaseConnection);

        // Указываем путь к Liquibase changelog файлу
        try (Liquibase liquibase = new Liquibase(LIQUIBASE_CHANGELOG, new ClassLoaderResourceAccessor(), database)) {
            // Выполняем миграции (changelog)
            liquibase.update(new Contexts());
            System.out.println("Liquibase migrations applied successfully.");
        }
    }

    /**
     * Закрытие соединения с базой данных
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Connection closed.");
            } catch (SQLException e) {
                System.out.println("Error while closing connection: " + e.getMessage());
            }
        }
    }
}
