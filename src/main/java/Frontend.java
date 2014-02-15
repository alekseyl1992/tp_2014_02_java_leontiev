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

        if (request.getPathInfo().equals("/timer")) {
            HttpSession session = request.getSession();
            Long userId = (Long) session.getAttribute("userId");
            if (userId == null) {
                response.sendRedirect("/");
            }
            else
                timerView(request, response);

            return;
        }
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);

        //test login and password
        if (users.containsKey(request.getParameter("login"))
                && users.get(request.getParameter("login")).equals(request.getParameter("password"))) {

            HttpSession session = request.getSession();

            //generate new uid
            Long userId = userIdGenerator.getAndIncrement();
            session.setAttribute("userId", userId);

            response.sendRedirect("/timer");
        }
        else {
            //wrong login
            response.sendRedirect("/");
        }
    }

    private void timerView(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException  {
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("refreshPeriod", "1000");
        pageVariables.put("serverTime", getTime());

        HttpSession session = request.getSession();
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null)
            response.sendRedirect("/");

        pageVariables.put("userId", userId);

        response.getWriter().println(PageGenerator.getPage("timer.tml", pageVariables));
    }
}
