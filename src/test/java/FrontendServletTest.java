import org.junit.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.io.StringWriter;

import static junit.framework.Assert.*;
import static org.mockito.Mockito.*;

public class FrontendServletTest {
    FrontendServlet frontend;
    HttpServletRequest request;
    HttpServletResponse response;
    HttpSession httpSession;
    StringWriter stringWriter;

    @Before
    public void setUp() throws Exception {
        DatabaseService service = new DatabaseService(DatabaseService.DB.H2);
        frontend = new FrontendServlet(service);

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        httpSession = mock(HttpSession.class);

        stringWriter = new StringWriter();

        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));
        when(request.getSession()).thenReturn(httpSession);
        when(httpSession.getAttribute("userId")).thenReturn(1L);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testDoGetIndex() throws Exception {
        when(request.getPathInfo()).thenReturn(FrontendServlet.Locations.INDEX);
        frontend.doGet(request, response);

        assertTrue(stringWriter.toString()
                .contains("<input type=\"submit\" value=\"Login\" />"));
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
    public void testDoPost() throws Exception {
        when(request.getPathInfo()).thenReturn(FrontendServlet.Locations.TIMER);
        frontend.doPost(request, response);
        verify(response, atLeastOnce()).sendRedirect(FrontendServlet.Locations.INDEX);
    }
}
