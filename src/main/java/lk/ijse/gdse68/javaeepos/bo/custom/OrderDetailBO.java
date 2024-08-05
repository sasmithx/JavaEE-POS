package lk.ijse.gdse68.javaeepos.bo.custom;



import lk.ijse.gdse68.javaeepos.bo.SuperBO;
import lk.ijse.gdse68.javaeepos.dto.OrderDTO;

import java.sql.Connection;
import java.sql.SQLException;

public interface OrderDetailBO extends SuperBO {

    OrderDTO getOrderDetailsById(Connection connection, String id) throws SQLException;
}
