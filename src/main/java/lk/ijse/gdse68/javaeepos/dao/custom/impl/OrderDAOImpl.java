package lk.ijse.gdse68.javaeepos.dao.custom.impl;



import lk.ijse.gdse68.javaeepos.dao.custom.OrderDAO;
import lk.ijse.gdse68.javaeepos.dao.util.CrudUtil;
import lk.ijse.gdse68.javaeepos.entity.Order;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrderDAOImpl implements OrderDAO {
    @Override
    public boolean save(Connection connection, Order entity) throws SQLException {
        String sql = "INSERT INTO orders (order_id, date, cust_id, discount, total) VALUES( ?, ?, ?, ?, ? )";
        return CrudUtil.execute(connection, sql, entity.getOrder_id(), entity.getDate(), entity.getCust_id(), entity.getDiscount(), entity.getTotal());
    }

    @Override
    public boolean update(Connection connection, Order entity) throws SQLException {
        return false;
    }

    @Override
    public ArrayList getAll(Connection connection) throws SQLException {
        return null;
    }

    @Override
    public boolean delete(Connection connection, String id) throws SQLException {
        return false;
    }

    @Override
    public Order findBy(Connection connection, String id) throws SQLException {
        String sql = "SELECT * FROM orders WHERE order_id = ?";
        ResultSet rs = CrudUtil.execute(connection, sql, id);

        Order order = new Order();
        if (rs.next()) {
            order.setOrder_id(rs.getString("order_id"));
            order.setDate(rs.getDate("date").toLocalDate());
            order.setCust_id(rs.getString("cust_id"));
            order.setDiscount(rs.getBigDecimal("discount"));
            order.setTotal(rs.getBigDecimal("total"));
        }
        return order;
    }

    @Override
    public String getLastId(Connection connection) throws SQLException {
        String sql = "SELECT order_id FROM orders ORDER BY order_id DESC LIMIT 1";
        ResultSet rs = CrudUtil.execute(connection, sql);

        String lastId = "no_ids";
        if (rs.next()) {
            lastId = rs.getString(1);
        }
        return lastId;
    }
}
