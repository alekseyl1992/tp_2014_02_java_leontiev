package messaging;

import org.junit.Test;

public class SleeperTest {
    @Test(timeout = Sleeper.TICK * 100)
    public void testSleep() throws Exception {
        Sleeper.sleep(Sleeper.TICK);
    }
}
