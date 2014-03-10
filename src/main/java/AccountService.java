import dao.UserDAO;
import datasets.UserDataSet;
import org.hibernate.SessionFactory;

public class AccountService {
    private DatabaseService databaseService;

    public AccountService(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    public Long tryRegister(String login, String password, String email) {
        UserDAO dao = new UserDAO(databaseService.getSessionFactory());
        UserDataSet user = new UserDataSet(login, password, email);

        if (dao.save(user))
            return user.getId();
        else
            return null;
    }

    public Long tryLogin(String login, String password) {
        UserDAO dao = new UserDAO(databaseService.getSessionFactory());
        UserDataSet user = dao.get(login);

        if (user != null && user.getPassword().equals(password))
            return user.getId();
        else
            return null;
    }

    public UserDataSet getUser(Long userId) {
        if (userId == null)
            return null;

        UserDAO dao = new UserDAO(databaseService.getSessionFactory());
        return dao.get(userId);
    }

    public boolean exists(Long userId) {
        return getUser(userId) != null;
    }
}
