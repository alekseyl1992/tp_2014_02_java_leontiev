import datasets.UserDataSet;
import org.hibernate.SessionFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class FrontendServlet extends HttpServlet {
    public abstract class Locations {
        public static final String INDEX = "/index";
        public static final String TIMER = "/timer";
        public static final String REGISTRATION = "/registration";
    }

    public abstract class Templates {
        public static final String INDEX = "index.tml";
        public static final String TIMER = "timer.tml";
        public static final String REGISTRATION = "registration.tml";
    }

    private AccountService accountService;

    public FrontendServlet(DatabaseService databaseService) {
        accountService = new AccountService(databaseService);
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);

        switch (request.getPathInfo()) {
            case Locations.TIMER: {
                HttpSession session = request.getSession();
                Long userId = (Long) session.getAttribute("userId");

                if (accountService.exists(userId))
                    timerView(request, response);
                else
                    response.sendRedirect(Locations.INDEX);

                return;
            }
            case Locations.INDEX: {
                indexView(request, response, false);

                return;
            }
            case Locations.REGISTRATION: {
                registrationView(request, response, false);

                return;
            }

            default: {
                response.sendRedirect(Locations.INDEX);

                return;
            }
        }
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);


        switch (request.getPathInfo()) {
            case Locations.INDEX: {
                tryLogin(request, response);

                return;
            }
            case Locations.REGISTRATION: {
                tryRegister(request, response);

                return;
            }

            default: {
                response.sendRedirect(Locations.INDEX);

                return;
            }
        }

    }

    private void tryLogin(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter("login");
        String password = request.getParameter("password");

        Long userId = accountService.tryLogin(login, password);

        if (userId != null) {
            HttpSession session = request.getSession();
            session.setAttribute("userId", userId);

            response.sendRedirect(Locations.TIMER);
            return;
        }
        else {
            indexView(request, response, true);
            return;
        }
    }

    private void tryRegister(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        String email = request.getParameter("email");

        Long userId = accountService.tryRegister(login, password, email);

        if (userId != null) {
            HttpSession session = request.getSession();
            session.setAttribute("userId", userId);

            response.sendRedirect(Locations.TIMER);
            return;
        }
        else {
            registrationView(request, response, true);
            return;
        }
    }

    private void timerView(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException  {
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("refreshPeriod", "1000");
        pageVariables.put("serverTime", getTime());

        HttpSession session = request.getSession();
        Long userId = (Long) session.getAttribute("userId");

        UserDataSet user = accountService.getUser(userId);
        pageVariables.put("login", user.getLogin());

        response.getWriter().println(PageGenerator.getPage(Templates.TIMER, pageVariables));
    }

    private void indexView(HttpServletRequest request, HttpServletResponse response, boolean failed)
            throws ServletException, IOException  {
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("failed", failed);

        response.getWriter().println(PageGenerator.getPage(Templates.INDEX, pageVariables));
    }

    private void registrationView(HttpServletRequest request, HttpServletResponse response, boolean failed)
            throws ServletException, IOException  {
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("failed", failed);

        response.getWriter().println(PageGenerator.getPage(Templates.REGISTRATION, pageVariables));
    }

    private static String getTime() {
        Date date = new Date();
        DateFormat formatter = new SimpleDateFormat("HH.mm.ss");
        return formatter.format(date);
    }
}
