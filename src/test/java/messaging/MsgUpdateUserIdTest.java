package messaging;

import frontend.FrontendServlet;
import messaging.messages.MsgUpdateUserId;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class MsgUpdateUserIdTest {
    Address from, to;

    @Before
    public void setUp() throws Exception {
        from = new Address();
        to = new Address();
    }

    @Test
    public void testExec() throws Exception {
        String sid = "1";
        Long id = 1L;

        FrontendServlet frontendServlet = mock(FrontendServlet.class);

        MsgUpdateUserId msg = new MsgUpdateUserId(from, to, sid, id);
        msg.exec(frontendServlet);

        verify(frontendServlet, atLeastOnce()).setId(sid, id);
    }
}
