package server;

import datasets.UserDataSet;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public abstract class DatabaseService {
    protected SessionFactory sessionFactory;

    public DatabaseService() throws Exception {
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(UserDataSet.class);

        configure(configuration);

        sessionFactory = createSessionFactory(configuration);
    }

    public SessionFactory getSessionFactory() {

        return sessionFactory;
    }

    protected abstract void configure(Configuration configuration);

    protected SessionFactory createSessionFactory(Configuration configuration) {
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(configuration.getProperties());
        ServiceRegistry serviceRegistry = builder.build();
        return configuration.buildSessionFactory(serviceRegistry);
    }
}
