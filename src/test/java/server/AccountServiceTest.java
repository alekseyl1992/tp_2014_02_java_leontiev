package server;

import datasets.UserDataSet;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AccountServiceTest {
    AccountService accountService;

    @Before
    public void setUp() throws Exception {
        DatabaseService databaseService = new H2DatabaseService();
        accountService = new AccountService(databaseService);
    }

    @Test
    public void testTryRegisterOk() throws Exception {
        String login = "testTryRegisterOk";

        assertTrue(accountService.tryRegister(login, login, login) != null);
    }

    @Test
    public void testTryRegisterFailed() throws Exception {
        String login = "testTryRegisterFailed";

        assertTrue(accountService.tryRegister(login, login, login) != null);
        assertFalse(accountService.tryRegister(login, login, login) != null);
    }

    @Test
    public void testTryLoginOk() throws Exception {
        String login = "testTryLoginOk";

        accountService.tryRegister(login, login, login);

        assertTrue(accountService.tryLogin(login, login) != null);
    }

    @Test
    public void testTryLoginFailed() throws Exception {
        String login = "testTryLoginFailed";

        assertFalse(accountService.tryLogin(login, login) != null);
    }

    @Test
    public void testGetUserOk() throws Exception {
        String login = "testGetUserOk";

        Long uid = accountService.tryRegister(login, login, login);
        UserDataSet user = accountService.getUser(uid);

        assertTrue(user.getId() == uid);
        assertTrue(user.getLogin().equals(login));
        assertTrue(user.getPassword().equals(login));
        assertTrue(user.getEmail().equals(login));
    }

    @Test(expected = NullPointerException.class)
    public void testGetUserFailed() throws Exception {
        String login = "testGetUserFailed";

        Long uid = accountService.tryRegister(login, login, login);
        UserDataSet user = accountService.getUser(uid + 1);

        assertFalse(user.getId() == uid);
        assertFalse(user.getLogin().equals(login));
    }

    public void testGetNullUserFailed() throws Exception {
        UserDataSet user = accountService.getUser(null);
        assertTrue(user == null);
    }

    @Test
    public void testExists() throws Exception {
        String login = "testExists";
        Long uid = accountService.tryRegister(login, login, login);

        assertTrue(accountService.exists(uid));
    }
}
