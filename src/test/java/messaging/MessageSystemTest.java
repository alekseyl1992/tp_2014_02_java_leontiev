package messaging;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class MessageSystemTest {
    MessageSystem ms;

    @Before
    public void setUp() throws Exception {
        ms = new MessageSystem();
    }

    @Test
    public void testSendMessage() throws Exception {
        Subscriber subscriber = mock(Subscriber.class);
        Address adr = mock(Address.class);
        when(subscriber.getAddress()).thenReturn(adr);

        Msg msg = mock(Msg.class);
        when(msg.getTo()).thenReturn(subscriber.getAddress());

        ms.addService(subscriber);
        ms.sendMessage(msg);
    }

    @Test
    public void testExecForSubscriber() throws Exception {
        Subscriber subscriber = mock(Subscriber.class);
        Address adr = mock(Address.class);
        when(subscriber.getAddress()).thenReturn(adr);

        Msg msg = mock(Msg.class);
        when(msg.getTo()).thenReturn(subscriber.getAddress());

        ms.addService(subscriber);
        ms.sendMessage(msg);

        ms.execForSubscriber(subscriber);
        verify(msg, atLeastOnce()).exec(subscriber);
    }
}
