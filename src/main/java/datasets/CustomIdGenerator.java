package datasets;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomIdGenerator implements IdentifierGenerator {
    long id1 = 0L;
    long id2 = 1L;

    @Override
    public synchronized Serializable generate(SessionImplementor sessionImplementor, Object o)
            throws HibernateException
    {
        if (id1 == 0L) {
            //get top two ids from db
            Connection conn = sessionImplementor.connection();

            try (PreparedStatement ps = conn.prepareStatement(
                    "select id from users order by id desc limit 2");
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    id2 = rs.getInt("id");
                if (rs.next()) {
                    id1 = id2;
                    id2 = rs.getInt("id");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        long id = id1 + id2;

        id1 = id2;
        id2 = id;

        return id;
    }
}
