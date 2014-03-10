package server;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static junit.framework.Assert.*;

public class GameServerTest {
    GameServer server;

    @Before
    public void setUp() throws Exception {
        server = new GameServer(8081, DatabaseService.DB.H2);
    }

    @After
    public void tearDown() throws Exception {
        server.stop();
    }

    private void startServer() {
        Thread gameThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    server.start();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        gameThread.start();
    }

    @Test
    public void testStart() throws Exception {
        startServer();
        //wait for game to start
        Thread.sleep(5000);

        assertTrue(server.isRunning());
    }

    @Test
    public void testStop() throws Exception {
        startServer();
        //wait for game to start
        Thread.sleep(5000);

        server.stop();

        assertFalse(server.isRunning());
    }

    @Test
    public void testIsRunning() throws Exception {
        assertFalse(server.isRunning());
    }
}
