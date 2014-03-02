package dao;

import datasets.UserDataSet;
import org.hibernate.*;
import org.hibernate.criterion.Restrictions;

public class UserDAO {
    private SessionFactory sessionFactory;

    public UserDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public boolean save(UserDataSet dataSet) {
        Session session = sessionFactory.openSession();
        Transaction trx = null;
        try {
            trx = session.beginTransaction();
            session.save(dataSet);
            trx.commit();
            return true;
        }
        catch(HibernateException ex) {
            trx.rollback();
            return false;
        }
        finally {
            session.close();
        }
    }

    public UserDataSet get(long id) {
        Session session = sessionFactory.openSession();
        return (UserDataSet) session.get(UserDataSet.class, id);
    }

    public UserDataSet get(String login) {
        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(UserDataSet.class);
        return (UserDataSet) criteria.add(Restrictions.eq("login", login)).uniqueResult();
    }
}