import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class Frontend extends HttpServlet {

    private AtomicLong userIdGenerator = new AtomicLong();
    private static final Map<String, String> users = new HashMap<String, String>() {
        {
            put("test", "test");
            put("vasja", "vasja");
        }
    };

    public static String getTime() {
        Date date = new Date();
        date.getTime();
        DateFormat formatter = new SimpleDateFormat("HH.mm.ss");
        return formatter.format(date);
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);

        switch (request.getPathInfo()) {
            case "/timer": {
                if (isLoggedIn(request))
                    timerView(request, response);
                else
                    response.sendRedirect("/");

                return;
            }

            default: {
                indexView(request, response, false);

                return;
            }
        }
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);

        //test login and password
        String login = request.getParameter("login");
        String password = request.getParameter("password");

        if (testLogin(login, password)) {
            HttpSession session = request.getSession();

            //generate new uid
            Long userId = userIdGenerator.getAndIncrement();
            session.setAttribute("userId", userId);

            response.sendRedirect("/timer");
            return;
        }
        else {
            //wrong login
            indexView(request, response, true);
            return;
        }
    }

    private boolean isLoggedIn(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Long userId = (Long) session.getAttribute("userId");

        return userId != null;
    }

    private boolean testLogin(String login, String password) {
        return users.containsKey(login)
                && users.get(login).equals(password);
    }

    private void timerView(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException  {
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("refreshPeriod", "1000");
        pageVariables.put("serverTime", getTime());

        HttpSession session = request.getSession();
        Long userId = (Long) session.getAttribute("userId");

        pageVariables.put("userId", userId);

        response.getWriter().println(PageGenerator.getPage("timer.tml", pageVariables));
    }

    private void indexView(HttpServletRequest request, HttpServletResponse response, boolean wrongLogin)
            throws ServletException, IOException  {
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("wrongLogin", wrongLogin);

        response.getWriter().println(PageGenerator.getPage("index.tml", pageVariables));
    }
}
