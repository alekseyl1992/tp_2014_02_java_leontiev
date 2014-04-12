package frontend;

import datasets.UserDataSet;
import messaging.*;
import server.IAccountService;
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
    private IAccountService accountService;
    private MessageSystem ms;
    private Address address;

    private Map<String, UserSession> sessionIdToUserSession = new HashMap<>();

    public FrontendServlet(MessageSystem ms, IAccountService accountService) {
        this.accountService = accountService;

        this.ms = ms;

        this.address = new Address();
        ms.addService(this);
        ms.getAddressService().setAccountService(address);
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
            case Locations.POLL: {
                pollView(request, response);

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
            case Locations.LOGIN: {
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

        String sessionId = request.getSession().getId();
        UserSession userSession = new UserSession(sessionId, login, ms.getAddressService());
        sessionIdToUserSession.put(sessionId, userSession);

        Address frontendAddress = getAddress();
        Address accountServiceAddress = userSession.getAccountService();

        ms.sendMessage(new MsgGetUserId(frontendAddress, accountServiceAddress, login, password, sessionId));

        response.getWriter().print("auth started");
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
        pageVariables.put("serverTime", formatter.format(new Date()));

        HttpSession session = request.getSession();
        Long userId = (Long) session.getAttribute("userId");

        UserDataSet user = accountService.getUser(userId);
        pageVariables.put("login", user.getLogin());

        response.getWriter().println(PageGenerator.getPage(Templates.TIMER, pageVariables));
    }

    private void pollView(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException  {

        HttpSession session = request.getSession();
        UserSession userSession = sessionIdToUserSession.get(session.getId());
        String result = "";

        if (userSession == null)
            result = "error";
        else if (userSession.getUserId() == null)
            result = "wait";
        else
            result = "ok";

        response.getWriter().print(result);
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
}
