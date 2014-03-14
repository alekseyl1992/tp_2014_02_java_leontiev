package server;

import org.hibernate.Session;
import org.junit.*;
import server.DatabaseService;

import static junit.framework.Assert.*;

public class DatabaseServiceTest {
    DatabaseService databaseService;

    @Test
    public void testGetSessionFactoryForMySQL() throws Exception {
        databaseService = new MySqlDatabaseService();
        Session session = databaseService.getSessionFactory().openSession();
        assertTrue(session.isConnected());
        session.close();
    }

    @Test
    public void testGetSessionFactoryForH2() throws Exception {
        databaseService = new H2DatabaseService();
        Session session = databaseService.getSessionFactory().openSession();
        assertTrue(session.isConnected());
        session.close();
    }
}
