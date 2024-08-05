package lk.ijse.gdse68.javaeepos.bo.custom.impl;



import lk.ijse.gdse68.javaeepos.bo.custom.OrderBO;
import lk.ijse.gdse68.javaeepos.dao.DAOFactory;
import lk.ijse.gdse68.javaeepos.dao.custom.ItemDAO;
import lk.ijse.gdse68.javaeepos.dao.custom.OrderDAO;
import lk.ijse.gdse68.javaeepos.dao.custom.OrderDetailDAO;
import lk.ijse.gdse68.javaeepos.dto.OrderDTO;
import lk.ijse.gdse68.javaeepos.dto.OrderDetailDTO;
import lk.ijse.gdse68.javaeepos.entity.Item;
import lk.ijse.gdse68.javaeepos.entity.Order;
import lk.ijse.gdse68.javaeepos.entity.OrderDetail;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class OrderBOImpl implements OrderBO {

    OrderDAO orderDAO = DAOFactory.getDAOFactory().getDAO(DAOFactory.DAOTypes.ORDER_DAO);
    OrderDetailDAO orderDetailDAO = DAOFactory.getDAOFactory().getDAO(DAOFactory.DAOTypes.ORDER_DETAILS_DAO);
    ItemDAO itemDAO = DAOFactory.getDAOFactory().getDAO(DAOFactory.DAOTypes.ITEM_DAO);

    @Override
    public String getLastId(Connection connection) throws SQLException {
        return orderDAO.getLastId(connection);
    }

    @Override
    public boolean placeOrder(Connection connection, OrderDTO dto) throws SQLException {
        try {
            connection.setAutoCommit(false);

            boolean isOrderSaved = orderDAO.save(connection, new Order(dto.getOrder_id(), dto.getDate(), dto.getCust_id(), dto.getDiscount(), dto.getTotal()));
            if (isOrderSaved) {
                boolean isOrderDetailsSaved = saveOrderDetails(connection, dto.getOrder_list());
                if (isOrderDetailsSaved) {
                    boolean isItemQtyUpdated = updateItemQty(connection, dto.getOrder_list());
                    if (isItemQtyUpdated) {
                        connection.commit();
                        return true;
                    }
                }
            }
            return false;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            connection.rollback();
            return false;
        }
    }

    @Override
    public OrderDTO getOrderById(Connection connection, String id) throws SQLException {
        Order order = orderDAO.findBy(connection, id);

        return new OrderDTO(
                order.getOrder_id(),
                order.getDate(),
                order.getCust_id(),
                order.getDiscount(),
                order.getTotal()
        );
    }

    private boolean updateItemQty(Connection connection, List<OrderDetailDTO> order_list) throws SQLException {
        for (OrderDetailDTO dto : order_list) {
            Item item = new Item(dto.getItem_code(), dto.getQty());
            if (!itemDAO.reduceQty(connection, item)) {
                return false;
            }
        }
        return true;
    }

    private boolean saveOrderDetails(Connection connection, List<OrderDetailDTO> order_list) throws SQLException {
        for (OrderDetailDTO dto : order_list) {
            OrderDetail orderDetail = new OrderDetail(dto.getOrder_id(), dto.getItem_code(), dto.getUnit_price(), dto.getQty());
            if (!orderDetailDAO.save(connection, orderDetail)) {
                return false;
            }
        }
        return true;
    }
}
