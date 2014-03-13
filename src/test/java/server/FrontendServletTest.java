package server;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.io.StringWriter;

import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class FrontendServletTest {
    FrontendServlet frontend;
    HttpServletRequest request;
    HttpServletResponse response;
    HttpSession httpSession;
    StringWriter stringWriter;
    AccountService accountService;

    @Before
    public void setUp() throws Exception {
        DatabaseService service = new DatabaseService(DatabaseService.DB.H2);
        accountService = new AccountService(service);
        frontend = new FrontendServlet(service);

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        httpSession = mock(HttpSession.class);

        stringWriter = new StringWriter();

        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));
        when(request.getSession()).thenReturn(httpSession);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testDoGetIndex() throws Exception {
        when(request.getPathInfo()).thenReturn(FrontendServlet.Locations.INDEX);
        frontend.doGet(request, response);

        assertTrue(stringWriter.toString()
                .contains("<input type=\"submit\" name=\"submit\" value=\"Login\" />"));
    }

    @Test
    public void testDoGetRegistration() throws Exception {
        when(request.getPathInfo()).thenReturn(FrontendServlet.Locations.REGISTRATION);
        frontend.doGet(request, response);

        assertTrue(stringWriter.toString()
                .contains("<input type=\"submit\" value=\"Register\" />"));
    }

    @Test
    public void testDoGetTimer() throws Exception {
        when(request.getPathInfo()).thenReturn(FrontendServlet.Locations.TIMER);
        frontend.doGet(request, response);
        verify(response, atLeastOnce()).sendRedirect(FrontendServlet.Locations.INDEX);
    }

    @Test
    public void testDoPostToTimer() throws Exception {
        when(request.getPathInfo()).thenReturn(FrontendServlet.Locations.TIMER);
        frontend.doPost(request, response);
        verify(response, atLeastOnce()).sendRedirect(FrontendServlet.Locations.INDEX);
    }

    private void registerUser(String name) throws Exception {
        when(request.getParameter("login")).thenReturn(name);
        when(request.getParameter("password")).thenReturn(name);
        when(request.getParameter("email")).thenReturn(name);

        when(request.getPathInfo()).thenReturn(FrontendServlet.Locations.REGISTRATION);
        frontend.doPost(request, response);
    }

    @Test
    public void testDoPostToLoginOk() throws Exception {
        registerUser("testDoPostToRegisterFailed");

        when(request.getPathInfo()).thenReturn(FrontendServlet.Locations.INDEX);
        frontend.doPost(request, response);
        verify(response, atLeastOnce()).sendRedirect(FrontendServlet.Locations.TIMER);
    }

    @Test
    public void testDoPostToLoginFailed() throws Exception {
        String login = "testDoPostToLoginFailed";

        when(request.getParameter("login")).thenReturn(login);
        when(request.getParameter("password")).thenReturn(login);
        when(request.getParameter("email")).thenReturn(login);

        when(request.getPathInfo()).thenReturn(FrontendServlet.Locations.INDEX);
        frontend.doPost(request, response);
        assertTrue(stringWriter.toString().contains("formError"));
    }

    @Test
    public void testDoPostToRegisterOk() throws Exception {
        registerUser("testDoPostToRegisterFailed");

        verify(response, atLeastOnce()).sendRedirect(FrontendServlet.Locations.TIMER);
    }

    @Test
    public void testDoPostToRegisterFailed() throws Exception {
        String login = "testDoPostToRegisterFailed";
        Long uid = accountService.tryRegister(login, login, login);

        registerUser(login);

        assertTrue(stringWriter.toString().contains("formError"));
    }
}
