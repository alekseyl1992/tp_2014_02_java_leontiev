import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class AccountService {
    private AtomicLong userIdGenerator = new AtomicLong();
    private static final Map<String, String> USERS = new HashMap<String, String>() {
        {
            put("test", "test");
            put("vasja", "vasja");
        }
    };

    public void addUser(String login, String password) {
        USERS.put(login, password);
    }

    public boolean isLoggedIn(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Long userId = (Long) session.getAttribute("userId");

        return userId != null;
    }

    public boolean testLogin(String login, String password) {
        return USERS.containsKey(login) && USERS.get(login).equals(password);
    }

    public void login(HttpServletRequest request) {
        HttpSession session = request.getSession();

        Long userId = userIdGenerator.getAndIncrement();
        session.setAttribute("userId", userId);
    }
}
