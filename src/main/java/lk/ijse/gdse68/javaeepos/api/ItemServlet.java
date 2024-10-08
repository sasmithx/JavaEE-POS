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
import lk.ijse.gdse68.javaeepos.bo.custom.ItemBO;
import lk.ijse.gdse68.javaeepos.dto.ItemDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;

@WebServlet(name = "item", urlPatterns = "/item")
public class ItemServlet extends HttpServlet {

    ItemBO itemBO = BOFactory.getBOFactory().getBO(BOFactory.BOTypes.ITEM_BO);

    static Logger logger = LoggerFactory.getLogger(ItemServlet.class);

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

        if (function.equals("getAll")){
            try (Connection connection = connectionPool.getConnection()){
                ArrayList<ItemDTO> customerDTOList = itemBO.getAllItems(connection);

                Jsonb jsonb = JsonbBuilder.create();
                String json = jsonb.toJson(customerDTOList);
                resp.getWriter().write(json);
            } catch (JsonbException e) {
                logger.error("Error while converting to Jsonb", e);
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            } catch (IOException e) {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            } catch (SQLException e) {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            }
        }else if(function.equals("getById")){
            String id = req.getParameter("id");
            try (Connection connection = connectionPool.getConnection()){
                ItemDTO itemDTO = itemBO.getItemByCode(connection, id);

                Jsonb jsonb = JsonbBuilder.create();
                String json = jsonb.toJson(itemDTO);
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

            ItemDTO itemDTO = jsonb.fromJson(req.getReader(), ItemDTO.class);
            System.out.println(itemDTO);

            if(itemDTO.getCode()==null || !itemDTO.getCode().matches("^(I00-)[0-9]{3}$")){
                logger.error("item code is empty or invalid");
                resp.getWriter().write("item code is empty or invalid");
                return;
            }else if(itemDTO.getName()==null || !itemDTO.getName().matches("^.{3,}$")){
                logger.error("item name is empty or invalid");
                resp.getWriter().write("name is empty or invalid");
                return;
            }else if(itemDTO.getPrice()==null || !itemDTO.getPrice().toString().matches("\\d+(\\.\\d{1,2})")){
                logger.error("item price is empty or invalid");
                resp.getWriter().write("address is empty or invalid");
                return;
            }else if(String.valueOf(itemDTO.getQty())==null || !String.valueOf(itemDTO.getQty()).matches("^\\d+(\\.\\d{1,2})?$")){
                logger.error("item qty is empty or invalid");
                resp.getWriter().write("contact is empty or invalid");
                return;
            }

            boolean isSaved = itemBO.saveCustomer(connection, itemDTO);
            if(isSaved){
                resp.setStatus(HttpServletResponse.SC_CREATED);
                logger.info("Item Saved Successfully");
            }else{
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "failed to add item");
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            logger.error("Item Already Exists");
            resp.sendError(HttpServletResponse.SC_CONFLICT, "Duplicate values. Please check again");
        }catch (Exception e) {
            logger.error("Error While Saving Item");
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while processing the request.");
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (Connection connection = connectionPool.getConnection()){
            Jsonb jsonb = JsonbBuilder.create();

            ItemDTO itemDTO = jsonb.fromJson(req.getReader(), ItemDTO.class);
            System.out.println(itemDTO);

            if(itemDTO.getCode()==null || !itemDTO.getCode().matches("^(I00-)[0-9]{3}$")){
                logger.error("item code is empty or invalid");
                resp.getWriter().write("item code is empty or invalid");
                return;
            }else if(itemDTO.getName()==null || !itemDTO.getName().matches("^[A-Za-z ]{4,}$")){
                logger.error("item name is empty or invalid");
                resp.getWriter().write("name is empty or invalid");
                return;
            }else if(itemDTO.getPrice()==null || !itemDTO.getPrice().toString().matches("\\d+(\\.\\d{1,2})")){
                logger.error("item price is empty or invalid");
                resp.getWriter().write("price is empty or invalid");
                return;
            }else if(String.valueOf(itemDTO.getQty())==null || !String.valueOf(itemDTO.getQty()).matches("^\\d+(\\.\\d{1,2})?$")){
                logger.error("item qty is empty or invalid");
                resp.getWriter().write("qty is empty or invalid");
                return;
            }

            boolean isUpdated = itemBO.updateItem(connection, itemDTO);
            if(isUpdated){
                logger.info("Item Updated Successfully");
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            }else{
                logger.error("Error While Updating Item");
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "failed to update item details");
            }
        }catch (SQLIntegrityConstraintViolationException e) {
            logger.error("Item Already Exists");
            resp.sendError(HttpServletResponse.SC_CONFLICT, "Duplicate values. Please check again");
        }catch (Exception e) {
            logger.error("An error occurred while processing the request");
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while processing the request.");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        try (Connection connection = connectionPool.getConnection()){
            boolean isDeleted = itemBO.removeItem(connection, id);
            if(isDeleted){
                logger.info("Item Deleted Successfully");
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            }else{
                logger.error("Error While Deleting Item");
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "failed to remove item");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

