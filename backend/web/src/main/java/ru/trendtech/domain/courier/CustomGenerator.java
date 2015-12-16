package ru.trendtech.domain.courier;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by petr on 24.09.2015.
 */
public class CustomGenerator implements IdentifierGenerator {
    private String defaultPrefix = "R&D";
    private int defaultNumber = 1;

    @Override
    public Serializable generate(SessionImplementor session, Object arg1) throws HibernateException {
        String empId = "";
        String digits = "";
        Connection con = session.connection();
        try {
            java.sql.PreparedStatement pst = con.prepareStatement("select id from transact order by created_at desc limit 1");
            ResultSet rs = pst.executeQuery();
            if (rs != null && rs.next()) {
                empId = rs.getString("id");
                System.out.println(empId);
                String prefix = empId.substring(0, 3);
                String str[] = empId.split(prefix);
                digits = String.format("%06d", Integer.parseInt(str[1]) + 1);
                empId = prefix.concat(digits);
            } else {
                digits = String.format("%06d", defaultNumber);
                empId = defaultPrefix.concat(digits);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return empId;
    }

}
