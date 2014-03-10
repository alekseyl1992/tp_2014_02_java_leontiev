package server;

import datasets.UserDataSet;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class DatabaseService {
    SessionFactory sessionFactory;

    public static enum DB {
        MYSQL, H2
    }

    public DatabaseService(DB db) throws Exception {
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(UserDataSet.class);

        switch (db) {
            case MYSQL: {
                configureMySQL(configuration);
                break;
            }
            case H2: {
                configureH2(configuration);
                break;
            }
            default:
                throw new Exception("Unsupported db type");
        }

        sessionFactory = createSessionFactory(configuration);
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    private void configureMySQL(Configuration configuration) {
        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        configuration.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
        configuration.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/javagame");
        configuration.setProperty("hibernate.connection.username", "JavaGameUser");
        configuration.setProperty("hibernate.connection.password", "JavaGamePswd");
        configuration.setProperty("hibernate.show_sql", "true");
        configuration.setProperty("hibernate.hbm2ddl.auto", "update");
    }

    private void configureH2(Configuration configuration) {
        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        configuration.setProperty("hibernate.connection.driver_class", "org.h2.Driver");
        configuration.setProperty("hibernate.connection.url", "jdbc:h2:~/test");
        configuration.setProperty("hibernate.connection.username", "sa");
        configuration.setProperty("hibernate.connection.password", "");
        configuration.setProperty("hibernate.show_sql", "true");
        configuration.setProperty("hibernate.hbm2ddl.auto", "create");
    }

    private SessionFactory createSessionFactory(Configuration configuration) {
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(configuration.getProperties());
        ServiceRegistry serviceRegistry = builder.build();
        return configuration.buildSessionFactory(serviceRegistry);
    }
}
