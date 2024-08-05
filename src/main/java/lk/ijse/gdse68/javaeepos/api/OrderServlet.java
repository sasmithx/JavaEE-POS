package lk.ijse.gdse68.javaeepos.api;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.ijse.gdse68.javaeepos.bo.BOFactory;
import lk.ijse.gdse68.javaeepos.bo.custom.OrderBO;
import lk.ijse.gdse68.javaeepos.dto.OrderDTO;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet(name = "orders", urlPatterns = "/order")
public class OrderServlet extends HttpServlet {

    OrderBO orderBO = BOFactory.getBOFactory().getBO(BOFactory.BOTypes.ORDER_BO);

    DataSource connectionPool;

    @Override
    public void init() throws ServletException {
        try {
            InitialContext ic = new InitialContext();
            connectionPool = (DataSource) ic.lookup("java:/comp/env/jdbc/eeAssignment");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String function = req.getParameter("function");

        if (function.equals("getLastId")){
            try (Connection connection = connectionPool.getConnection()){
                String lastId = orderBO.getLastId(connection);
                resp.getWriter().write(lastId);
            } catch (SQLException e) {
                e.printStackTrace();
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            }
        }else if(function.equals("getById")){
            String id = req.getParameter("id");
            try (Connection connection = connectionPool.getConnection()){
                OrderDTO orderDTO = orderBO.getOrderById(connection, id);

                Jsonb jsonb = JsonbBuilder.create();
                String json = jsonb.toJson(orderDTO);
                resp.getWriter().write(json);
            } catch (JsonbException e) {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            } catch (IOException e) {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            } catch (SQLException e) {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (Connection connection = connectionPool.getConnection()){

            Jsonb jsonb = JsonbBuilder.create();

            OrderDTO orderDTO = jsonb.fromJson(req.getReader(), OrderDTO.class);
            System.out.println(orderDTO);

            if(orderDTO.getOrder_id()==null || !orderDTO.getOrder_id().matches("^(ORD-)[0-9]{3}$")){
                resp.getWriter().write("order id is empty or invalid");
                return;
            }else if(orderDTO.getDate()==null || !orderDTO.getDate().toString().matches("\\d{4}-\\d{2}-\\d{2}")){
                resp.getWriter().write("date is empty or invalid");
                return;
            }else if(orderDTO.getCust_id()==null || !orderDTO.getCust_id().matches("^(C00-)[0-9]{3}$")){
                resp.getWriter().write("customer id is empty or invalid");
                return;
            }else if(orderDTO.getDiscount()==null || !orderDTO.getDiscount().toString().matches("\\d+(\\.\\d+)?")){
                resp.getWriter().write("discount is empty or invalid");
                return;
            }else if(orderDTO.getTotal()==null || !orderDTO.getTotal().toString().matches("\\d+(\\.\\d+)?")){
                resp.getWriter().write("total is empty or invalid");
                return;
            }else if(orderDTO.getOrder_list().size()==0){
                resp.getWriter().write("order details list is empty");
                return;
            }

            boolean isTransactionDone = orderBO.placeOrder(connection, orderDTO);
            if(isTransactionDone){
                resp.setStatus(HttpServletResponse.SC_CREATED);
            }else{
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "failed transaction");
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "transaction failed");
        }
    }
}
