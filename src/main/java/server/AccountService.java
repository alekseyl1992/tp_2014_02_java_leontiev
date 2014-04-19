package server;

import dao.UserDAO;
import datasets.UserDataSet;
import messaging.Address;
import messaging.MessageSystem;
import messaging.Sleeper;
import messaging.Subscriber;

public class AccountService implements IAccountService, Subscriber, Runnable {
    private DatabaseService databaseService;
    private MessageSystem ms;
    private Address address;

    public AccountService(MessageSystem ms, DatabaseService databaseService) {
        this.ms = ms;
        this.databaseService = databaseService;

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

    public Long tryLogin(String login, String password){
        UserDAO dao = new UserDAO(databaseService.getSessionFactory());
        UserDataSet user = dao.get(login);
        if (user != null && user.getPassword().equals(password))
            return user.getId();
        else
            return null;
    }

    public Long tryRegister(String login, String password, String email) {
        UserDAO dao = new UserDAO(databaseService.getSessionFactory());
        UserDataSet user = new UserDataSet(login, password, email);

        if (dao.save(user))
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
