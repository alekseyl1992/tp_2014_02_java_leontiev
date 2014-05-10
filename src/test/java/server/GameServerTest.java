package server;

import database.DatabaseService;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;
import resourcing.ResourceSystem;

import static org.junit.Assert.*;

public class GameServerTest {
    GameServer server;

    @Rule
    public Timeout globalTimeout = new Timeout(10000);

    @Before
    public void setUp() throws Exception {
        ResourceSystem rs = ResourceSystem.getInstance();
        String port = rs.getConfig("server").get("port");

        server = new GameServer(Integer.parseInt(port), new DatabaseService(rs.getConfig("h2")));
    }

    @After
    public void tearDown() throws Exception {
        server.stop();
    }

    private void startServer() {
        Thread gameThread = new Thread(() -> {
            try {
                server.start();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        });
        gameThread.start();
    }

    @Test
    public void testStart() throws Exception {
        startServer();
        //wait for game to start
        while (!server.isRunning()) {
            Thread.sleep(100);
        }

        assertTrue(server.isRunning());
    }

    @Test
    public void testStop() throws Exception {
        startServer();
        //wait for game to start
        while (!server.isRunning()) {
            Thread.sleep(100);
        }

        server.stop();

        assertFalse(server.isRunning());
    }

    @Test
    public void testIsRunning() throws Exception {
        assertFalse(server.isRunning());
    }
}
