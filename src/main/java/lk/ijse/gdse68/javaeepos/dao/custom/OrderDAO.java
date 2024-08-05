package lk.ijse.gdse68.javaeepos.dao.custom;



import lk.ijse.gdse68.javaeepos.dao.CrudDAO;
import lk.ijse.gdse68.javaeepos.entity.Order;

import java.sql.Connection;
import java.sql.SQLException;

public interface OrderDAO extends CrudDAO<Order> {
    String getLastId(Connection connection) throws SQLException;
}
