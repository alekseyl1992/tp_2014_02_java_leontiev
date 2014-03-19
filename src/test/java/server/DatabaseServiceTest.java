package server;

import org.hibernate.Session;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

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
