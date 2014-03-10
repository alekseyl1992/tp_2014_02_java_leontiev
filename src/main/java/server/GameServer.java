package server;

import org.eclipse.jetty.rewrite.handler.RedirectRegexRule;
import org.eclipse.jetty.rewrite.handler.RewriteHandler;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class GameServer {
    Server server;

    public GameServer(int port, DatabaseService.DB db) throws Exception {
        DatabaseService databaseService = new DatabaseService(db);

        FrontendServlet frontendServlet = new FrontendServlet(databaseService);

        Server server = new Server(port);

        RewriteHandler rewriteHandler = new RewriteHandler();

        rewriteHandler.setRewriteRequestURI(false);
        rewriteHandler.setRewritePathInfo(false);
        rewriteHandler.setOriginalPathAttribute("requestedPath");
        RedirectRegexRule rule = new RedirectRegexRule();
        rule.setRegex("/");
        rule.setReplacement("/index");
        rule.setHandling(true);

        rewriteHandler.addRule(rule);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(frontendServlet), "/*");

        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setResourceBase("static");

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{rewriteHandler, resourceHandler, context});
        server.setHandler(handlers);
    }

    public void start() throws Exception {
        server.start();
        server.join();
    }
}
