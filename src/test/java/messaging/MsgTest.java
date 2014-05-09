package messaging;

import messaging.messages.Msg;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class MsgTest {
    Address from, to;
    Msg msg;

    @Before
    public void setUp() throws Exception {
        from = new Address();
        to = new Address();

        msg = new Msg(from, to) {
            @Override
            public void exec(Subscriber subscriber) {}
        };
    }

    @Test
    public void testGetFrom() throws Exception {
        assertTrue(msg.getFrom() == from);
    }

    @Test
    public void testGetTo() throws Exception {
        assertTrue(msg.getTo() == to);
    }
}
