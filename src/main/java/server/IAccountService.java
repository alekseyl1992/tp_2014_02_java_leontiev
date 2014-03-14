package server;

import dao.UserDAO;
import datasets.UserDataSet;

public interface IAccountService {
    Long tryRegister(String login, String password, String email);
    Long tryLogin(String login, String password);
    UserDataSet getUser(Long userId);
    boolean exists(Long userId);
}
