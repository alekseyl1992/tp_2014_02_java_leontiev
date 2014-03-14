package server;

import org.hibernate.cfg.Configuration;

public class MySqlDatabaseService extends DatabaseService {
    public MySqlDatabaseService() throws Exception {
    }

    protected void configure(Configuration configuration) {
        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        configuration.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
        configuration.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/javagame");
        configuration.setProperty("hibernate.connection.username", "JavaGameUser");
        configuration.setProperty("hibernate.connection.password", "JavaGamePswd");
        configuration.setProperty("hibernate.show_sql", "true");
        configuration.setProperty("hibernate.hbm2ddl.auto", "update");
    }
}
