import org.hibernate.Session;
import org.junit.*;
import static junit.framework.Assert.*;
import static org.mockito.Mockito.*;

public class DatabaseServiceTest {
    DatabaseService databaseService;

    @Test
    public void testGetSessionFactoryForMySQL() throws Exception {
        databaseService = new DatabaseService(DatabaseService.DB.MYSQL);
        Session session = databaseService.getSessionFactory().openSession();
        assertTrue(session.isConnected());
        session.close();
    }

    @Test
    public void testGetSessionFactoryForH2() throws Exception {
        databaseService = new DatabaseService(DatabaseService.DB.H2);
        Session session = databaseService.getSessionFactory().openSession();
        assertTrue(session.isConnected());
        session.close();
    }
}
