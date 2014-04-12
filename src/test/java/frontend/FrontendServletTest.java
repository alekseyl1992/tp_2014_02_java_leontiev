package frontend;

import messaging.AddressService;
import messaging.MessageSystem;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import server.IAccountService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class FrontendServletTest {
    FrontendServlet frontend;
    HttpServletRequest request;
    HttpServletResponse response;
    HttpSession httpSession;
    StringWriter stringWriter;
    IAccountService accountService;

    @Before
    public void setUp() throws Exception {
        accountService = mock(IAccountService.class);

        MessageSystem ms = mock(MessageSystem.class);
        AddressService as = mock(AddressService.class);
        when(ms.getAddressService()).thenReturn(as);

        frontend = new FrontendServlet(ms);

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
        when(request.getPathInfo()).thenReturn(Locations.INDEX);
        frontend.doGet(request, response);

        assertTrue(stringWriter.toString()
                .contains("action=\"/login\""));
    }

    @Test
    public void testDoGetRegistration() throws Exception {
        when(request.getPathInfo()).thenReturn(Locations.REGISTRATION);
        frontend.doGet(request, response);

        assertTrue(stringWriter.toString()
                .contains("action=\"/register\""));
    }

    @Test
    public void testDoGetTimer() throws Exception {
        when(request.getPathInfo()).thenReturn(Locations.TIMER);
        frontend.doGet(request, response);
        verify(response, atLeastOnce()).sendRedirect(Locations.INDEX);
    }

    @Test
    public void testDoGetWrongPage() throws Exception {
        when(request.getPathInfo()).thenReturn("wrongPage");
        frontend.doGet(request, response);
        verify(response, atLeastOnce()).sendRedirect(Locations.INDEX);
    }

    @Test
    public void testDoPostToTimer() throws Exception {
        when(request.getPathInfo()).thenReturn(Locations.TIMER);
        frontend.doPost(request, response);
        verify(response, atLeastOnce()).sendRedirect(Locations.INDEX);
    }

    private void registerUser(String name) throws Exception {
        when(request.getParameter("login")).thenReturn(name);
        when(request.getParameter("password")).thenReturn(name);
        when(request.getParameter("email")).thenReturn(name);

        when(request.getPathInfo()).thenReturn(Locations.REGISTRATION);
        frontend.doPost(request, response);
    }

    @Test
    public void testDoPostToLoginOk() throws Exception {
        when(request.getPathInfo()).thenReturn(Locations.LOGIN);
        when(request.getParameter("login")).thenReturn("user");
        when(request.getParameter("password")).thenReturn("pswd");

        frontend.doPost(request, response);
        assertTrue(stringWriter.toString().contains("auth started"));
    }

    @Test
    public void testDoPostToLoginFailed() throws Exception {
        when(request.getPathInfo()).thenReturn(Locations.LOGIN);
        when(request.getParameter("login")).thenReturn("");
        when(request.getParameter("password")).thenReturn(null);

        frontend.doPost(request, response);
        assertTrue(stringWriter.toString().contains("wrong"));
    }

    @Test
    public void testDoPostToRegisterOk() throws Exception {
        when(request.getPathInfo()).thenReturn(Locations.REGISTER);
        when(request.getParameter("login")).thenReturn("user");
        when(request.getParameter("password")).thenReturn("pswd");
        when(request.getParameter("email")).thenReturn("a@b.com");

        frontend.doPost(request, response);
        assertTrue(stringWriter.toString().contains("registration started"));
    }

    @Test
    public void testDoPostToRegisterFailed() throws Exception {
        when(request.getPathInfo()).thenReturn(Locations.REGISTER);
        when(request.getParameter("login")).thenReturn("");
        when(request.getParameter("password")).thenReturn("pswd");
        when(request.getParameter("password")).thenReturn("a@b.com");

        frontend.doPost(request, response);
        assertTrue(stringWriter.toString().contains("wrong"));
    }
}
