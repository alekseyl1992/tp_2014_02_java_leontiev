package dao;

import datasets.UserDataSet;
import org.hibernate.*;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.ConstraintViolationException;
import server.AutoSession;
import server.DBException;

public class UserDAO {
    private SessionFactory sessionFactory;

    public UserDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public boolean save(UserDataSet dataSet) throws DBException {
        Transaction trx = null;

        try (AutoSession session = new AutoSession(sessionFactory.openSession())) {
            trx = session.beginTransaction();
            session.save(dataSet);
            trx.commit();
            return true;
        }
        catch (ConstraintViolationException e) {
            try {
                if (trx != null)
                    trx.rollback();
            }
            catch (TransactionException ignore) {}

            return false;
        }
        catch (HibernateException e) {
            try {
                if (trx != null)
                    trx.rollback();
            }
            catch (TransactionException ignore) {}

            throw new DBException(e);
        }
    }

    public UserDataSet get(long id) throws DBException {
        try (AutoSession session = new AutoSession(sessionFactory.openSession())) {
            return (UserDataSet) session.get(UserDataSet.class, id);
        }
        catch (HibernateException e) {
            throw new DBException(e);
        }
    }

    public UserDataSet get(String login) throws DBException {
        try (AutoSession session = new AutoSession(sessionFactory.openSession())) {
            Criteria criteria = session.createCriteria(UserDataSet.class);

            return (UserDataSet) criteria.add(Restrictions.eq("login", login))
                    .uniqueResult();
        }
        catch (HibernateException e) {
            throw new DBException(e);
        }
    }
}