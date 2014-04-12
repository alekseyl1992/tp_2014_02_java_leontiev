package messaging;

import org.junit.Before;
import org.junit.Test;
import server.AccountService;

import static org.mockito.Mockito.*;

public class MsgLoginUserTest {
    Address from, to;

    @Before
    public void setUp() throws Exception {
        from = new Address();
        to = new Address();
    }

    @Test
    public void testExec() throws Exception {
        String login = "login";
        String password = "password";
        String sid = "1";

        AccountService accountService = mock(AccountService.class);
        MessageSystem ms = mock(MessageSystem.class);
        when(accountService.getMessageSystem()).thenReturn(ms);

        MsgLoginUser msg = new MsgLoginUser(from, to, login, password, sid);
        msg.exec(accountService);

        verify(accountService, atLeastOnce()).tryLogin(login, password);
    }
}
