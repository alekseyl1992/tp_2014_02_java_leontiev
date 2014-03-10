import datasets.UserDataSet;
import org.junit.*;
import static junit.framework.Assert.*;
import static org.mockito.Mockito.*;

public class AccountServiceTest {
    AccountService accountService;

    @Before
    public void setUp() throws Exception {
        DatabaseService databaseService = new DatabaseService(DatabaseService.DB.H2);
        accountService = new AccountService(databaseService);
    }

    @Test
    public void testTryRegister() throws Exception {
        assertTrue(accountService.tryRegister(
                "testTryRegister",
                "testTryRegister",
                "testTryRegister") != null);
    }

    @Test
    public void testTryLogin() throws Exception {
        String login = "testTryLogin";
        String password = "testTryLogin";
        String email = "testTryLogin";

        accountService.tryRegister(login, password, email);

        assertTrue(accountService.tryLogin(login, password) != null);
    }

    @Test
    public void testGetUser() throws Exception {
        String login = "testGetUser";
        String password = "testGetUser";
        String email = "testGetUser";

        Long uid = accountService.tryRegister(login, password, email);
        UserDataSet user = accountService.getUser(uid);

        assertTrue(user.getId() == uid
                && user.getLogin().equals(login)
                && user.getPassword().equals(password)
                && user.getEmail().equals(email));
    }

    @Test
    public void testExists() throws Exception {
        String login = "testExists";
        String password = "testExists";
        String email = "testExists";

        Long uid = accountService.tryRegister(login, password, email);       UserDataSet user = accountService.getUser(uid);

        assertTrue(accountService.exists(uid));
    }
}
