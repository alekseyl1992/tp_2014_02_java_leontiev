package messaging;

import messaging.messages.MsgRegisterUser;
import org.junit.Before;
import org.junit.Test;
import server.AccountService;

import static org.mockito.Mockito.*;

public class MsgRegisterUserTest {
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
        String email = "a@b.com";
        String sid = "1";

        AccountService accountService = mock(AccountService.class);
        MessageSystem ms = mock(MessageSystem.class);
        when(accountService.getMessageSystem()).thenReturn(ms);

        MsgRegisterUser msg = new MsgRegisterUser(from, to, login, password, email, sid);
        msg.exec(accountService);

        verify(accountService, atLeastOnce()).tryRegister(login, password, email);
    }
}
