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
import lk.ijse.gdse68.javaeepos.bo.custom.OrderDetailBO;
import lk.ijse.gdse68.javaeepos.dto.OrderDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;

@WebServlet(name = "orderDetail", urlPatterns = "/orderDetail")
public class OrderDetailServlet extends HttpServlet {

    OrderDetailBO orderDetailsBO = BOFactory.getBOFactory().getBO(BOFactory.BOTypes.ORDER_DETAIL_BO);

    static Logger logger = LoggerFactory.getLogger(OrderDetailServlet.class);

    DataSource connectionPool;

    @Override
    public void init() throws ServletException {
        try {
            InitialContext ic = new InitialContext();
            connectionPool = (DataSource) ic.lookup("java:/comp/env/jdbc/pos");
            logger.debug("DB Connection Initialized: {}",connectionPool);
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String function = req.getParameter("function");

        if(function.equals("getById")){
            String id = req.getParameter("id");
            try (Connection connection = connectionPool.getConnection()){
                OrderDTO orderDTO = orderDetailsBO.getOrderDetailsById(connection, id);

                Jsonb jsonb = JsonbBuilder.create();
                String json = jsonb.toJson(orderDTO);
                resp.getWriter().write(json);
            } catch (JsonbException e) {
                logger.error("Error while getting order details by id");
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            } catch (IOException e) {
                logger.error("Error while getting order details by id");
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            } catch (Exception e) {
                logger.error("Error while getting order details by id");
                e.printStackTrace();
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            }
        }
    }
}
