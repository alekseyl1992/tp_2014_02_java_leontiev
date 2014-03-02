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
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(UserDataSet.class);

        SessionFactory sessionFactory = createSessionFactory(configuration);

        FrontendServlet frontendServlet = new FrontendServlet(sessionFactory);

        Server server = new Server(8081);

        RewriteHandler rewriteHandler = new RewriteHandler();
        rewriteHandler.setRewriteRequestURI(true);
        rewriteHandler.setRewritePathInfo(true);
        rewriteHandler.setOriginalPathAttribute("requestedPath");
        RedirectRegexRule rule = new RedirectRegexRule();
        rule.setRegex("/");
        rule.setReplacement("/index");
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

    public static SessionFactory createSessionFactory(Configuration configuration) {
        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        configuration.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
        configuration.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/javagame");
        configuration.setProperty("hibernate.connection.username", "JavaGameUser");
        configuration.setProperty("hibernate.connection.password", "JavaGamePswd");
        configuration.setProperty("hibernate.show_sql", "true");
        configuration.setProperty("hibernate.hbm2ddl.auto", "update");

        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(configuration.getProperties());
        ServiceRegistry serviceRegistry = builder.build();
        return configuration.buildSessionFactory(serviceRegistry);
    }
}