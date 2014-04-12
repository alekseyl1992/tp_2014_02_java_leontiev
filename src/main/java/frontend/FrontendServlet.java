package frontend;

import messaging.*;
import server.UserSession;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FrontendServlet extends HttpServlet implements Subscriber, Runnable {
    private DateFormat formatter = new SimpleDateFormat("HH.mm.ss");
    private MessageSystem ms;
    private Address address;

    private Map<String, UserSession> sessionIdToUserSession = new HashMap<>();

    public FrontendServlet(MessageSystem ms) {
        this.ms = ms;

        this.address = new Address();
        ms.addService(this);
        ms.getAddressService().setFrontendServlet(address);
    }

    @Override
    public void run() {
        while(true){
            ms.execForSubscriber(this);
            Sleeper.sleep(Sleeper.TICK);
        }
    }

    @Override
    public Address getAddress() {
        return address;
    }

    @Override
    public MessageSystem getMessageSystem() {
        return ms;
    }

    public void setId(String sessionId, Long userId) {
        UserSession userSession = sessionIdToUserSession.get(sessionId);
        if (userSession == null) {
            System.out.append("Can't find user session for: ").append(sessionId);
            return;
        }
        userSession.setUserId(userId);
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);

        switch (request.getPathInfo()) {
            case Locations.TIMER: {
                timerView(request, response);
                return;
            }
            case Locations.INDEX: {
                renderPage(response, Templates.INDEX);
                return;
            }
            case Locations.POLL: {
                pollView(request, response);
                return;
            }
            case Locations.REGISTRATION: {
                renderPage(response, Templates.REGISTRATION);
                return;
            }
            default:
                response.sendRedirect(Locations.INDEX);
        }
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);


        switch (request.getPathInfo()) {
            case Locations.LOGIN: {
                tryLogin(request, response);
                return;
            }
            case Locations.REGISTER: {
                tryRegister(request, response);
                return;
            }
            default:
                response.sendRedirect(Locations.INDEX);
        }

    }

    private void tryLogin(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter("login");
        String password = request.getParameter("password");

        if (!checkCredentials(login, password, login)) {
            response.getWriter().print("wrong");
            return;
        }

        String sessionId = request.getSession().getId();
        UserSession userSession = new UserSession(sessionId, login, ms.getAddressService());
        sessionIdToUserSession.put(sessionId, userSession);

        Address frontendAddress = getAddress();
        Address accountServiceAddress = userSession.getAccountService();

        ms.sendMessage(new MsgLoginUser(frontendAddress, accountServiceAddress,
                login, password, sessionId));

        response.getWriter().print("auth started");
    }

    private void tryRegister(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        String email = request.getParameter("email");

        if (!checkCredentials(login, password, email)) {
            response.getWriter().print("wrong");
            return;
        }

        String sessionId = request.getSession().getId();
        UserSession userSession = new UserSession(sessionId, login, ms.getAddressService());
        sessionIdToUserSession.put(sessionId, userSession);

        Address frontendAddress = getAddress();
        Address accountServiceAddress = userSession.getAccountService();

        ms.sendMessage(new MsgRegisterUser(frontendAddress, accountServiceAddress,
                login, password, email, sessionId));

        response.getWriter().print("registration started");
    }

    private void timerView(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException  {
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("refreshPeriod", "1000");
        pageVariables.put("serverTime", formatter.format(new Date()));

        HttpSession session = request.getSession();
        UserSession userSession = sessionIdToUserSession.get(session.getId());
        if (userSession == null) {
            response.sendRedirect(Locations.INDEX);
        }
        else {
            pageVariables.put("login", userSession.getLogin());
            pageVariables.put("id", userSession.getUserId());
            response.getWriter().println(PageGenerator.getPage(Templates.TIMER, pageVariables));
        }
   }

    private void pollView(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException  {

        HttpSession session = request.getSession();
        UserSession userSession = sessionIdToUserSession.get(session.getId());
        String result;

        if (userSession == null)
            result = "error";
        else if (userSession.isWrong())
            result = "wrong";
        else if (userSession.getUserId() == null)
            result = "wait";
        else
            result = "ok";

        response.getWriter().print(result);
    }

    private void renderPage(HttpServletResponse response, String template) throws IOException {
        response.getWriter().println(PageGenerator.getPage(template, null));
    }

    private boolean checkCredentials(String login, String password, String email) {
        return login     != null && !login.isEmpty() &&
               password  != null && !password.isEmpty() &&
               email     != null && !email.isEmpty();
    }
}
