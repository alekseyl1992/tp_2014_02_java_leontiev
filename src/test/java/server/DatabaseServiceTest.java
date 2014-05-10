package server;

import database.DatabaseService;
import org.hibernate.Session;
import org.junit.Test;
import resourcing.ResourceSystem;

import static org.junit.Assert.assertTrue;

public class DatabaseServiceTest {
    DatabaseService databaseService;

    @Test
    public void testGetSessionFactoryForMySQL() throws Exception {
        ResourceSystem rs = ResourceSystem.getInstance();

        databaseService = new DatabaseService(rs.getConfig("mysql"));
        Session session = databaseService.getSessionFactory().openSession();
        assertTrue(session.isConnected());
        session.close();
    }

    @Test
    public void testGetSessionFactoryForH2() throws Exception {
        ResourceSystem rs = ResourceSystem.getInstance();

        databaseService = new DatabaseService(rs.getConfig("h2"));
        Session session = databaseService.getSessionFactory().openSession();
        assertTrue(session.isConnected());
        session.close();
    }
}
