import datasets.UserDataSet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.rewrite.handler.RedirectRegexRule;
import org.eclipse.jetty.rewrite.handler.RewriteHandler;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class Main {
    public static void main(String[] args) throws Exception {
        DatabaseService databaseService = new DatabaseService(DatabaseService.DB.MYSQL);

        FrontendServlet frontendServlet = new FrontendServlet(databaseService);

        Server server = new Server(8081);

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

        server.start();
        server.join();
    }
}