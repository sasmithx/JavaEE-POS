package lk.ijse.gdse68.javaeepos.dao.custom;



import lk.ijse.gdse68.javaeepos.dao.CrudDAO;
import lk.ijse.gdse68.javaeepos.entity.OrderDetail;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface OrderDetailDAO extends CrudDAO<OrderDetail> {
    List<OrderDetail> getAllById(Connection connection, String id) throws SQLException;
}
