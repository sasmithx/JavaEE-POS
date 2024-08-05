package lk.ijse.gdse68.javaeepos.bo.custom.impl;



import lk.ijse.gdse68.javaeepos.bo.custom.OrderDetailBO;
import lk.ijse.gdse68.javaeepos.dao.DAOFactory;
import lk.ijse.gdse68.javaeepos.dao.custom.OrderDAO;
import lk.ijse.gdse68.javaeepos.dao.custom.OrderDetailDAO;
import lk.ijse.gdse68.javaeepos.dto.OrderDTO;
import lk.ijse.gdse68.javaeepos.dto.OrderDetailDTO;
import lk.ijse.gdse68.javaeepos.entity.Order;
import lk.ijse.gdse68.javaeepos.entity.OrderDetail;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDetailBOImpl implements OrderDetailBO {

    OrderDAO orderDAO = DAOFactory.getDAOFactory().getDAO(DAOFactory.DAOTypes.ORDER_DAO);
    OrderDetailDAO orderDetailDAO = DAOFactory.getDAOFactory().getDAO(DAOFactory.DAOTypes.ORDER_DETAILS_DAO);

    @Override
    public OrderDTO getOrderDetailsById(Connection connection, String id) throws SQLException {
        OrderDTO orderDTO = new OrderDTO();

        Order order = orderDAO.findBy(connection, id);
        orderDTO.setDate(order.getDate());
        orderDTO.setCust_id(order.getCust_id());
        orderDTO.setDiscount(order.getDiscount());
        orderDTO.setTotal(order.getTotal());

        List<OrderDetail> orderDetailList = orderDetailDAO.getAllById(connection, id);

        List<OrderDetailDTO> orderDetailDTOList = new ArrayList<OrderDetailDTO>();
        for(OrderDetail orderDetail : orderDetailList){
            orderDetailDTOList.add(new OrderDetailDTO(
                    orderDetail.getItem_code(),
                    orderDetail.getUnit_price(),
                    orderDetail.getQty()
            ));
        }
        orderDTO.setOrder_list(orderDetailDTOList);

        return orderDTO;
    }
}
