package lk.ijse.gdse68.javaeepos.dao.custom;



import lk.ijse.gdse68.javaeepos.dao.CrudDAO;
import lk.ijse.gdse68.javaeepos.entity.Item;

import java.sql.Connection;
import java.sql.SQLException;

public interface ItemDAO extends CrudDAO<Item> {
    boolean reduceQty(Connection connection, Item item) throws SQLException;
}
