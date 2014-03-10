package server;

public class Main {
    public static void main(String[] args) throws Exception {
        GameServer server = new GameServer(8081, DatabaseService.DB.MYSQL);
        server.start();
    }
}