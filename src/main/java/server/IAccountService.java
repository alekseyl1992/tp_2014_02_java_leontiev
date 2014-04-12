package server;

import datasets.UserDataSet;
import messaging.Subscriber;

public interface IAccountService extends Subscriber, Runnable {
    Long tryRegister(String login, String password, String email);
    Long tryLogin(String login, String password);
    UserDataSet getUser(Long userId);
    boolean exists(Long userId);
}
