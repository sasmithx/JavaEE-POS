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
import lk.ijse.gdse68.javaeepos.bo.custom.CustomerBO;
import lk.ijse.gdse68.javaeepos.dto.CustomerDTO;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;

@WebServlet(name = "customer", urlPatterns = "/customer")
public class CustomerServlet extends HttpServlet {

    CustomerBO customerBO = BOFactory.getBOFactory().getBO(BOFactory.BOTypes.CUSTOMER_BO);

    DataSource connectionPool;

    @Override
    public void init() throws ServletException {
        try {
            InitialContext ic = new InitialContext();
            connectionPool = (DataSource) ic.lookup("java:/comp/env/jdbc/pos");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String function = req.getParameter("function");

        if (function.equals("getAll")){
            try (Connection connection = connectionPool.getConnection()){
                ArrayList<CustomerDTO> customerDTOList = customerBO.getAllCustomers(connection);

                Jsonb jsonb = JsonbBuilder.create();
                String json = jsonb.toJson(customerDTOList);
                resp.getWriter().write(json);
            } catch (JsonbException e) {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            } catch (IOException e) {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            } catch (SQLException e) {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            }
        }else if(function.equals("getById")){
            String id = req.getParameter("id");
            try (Connection connection = connectionPool.getConnection()){
                CustomerDTO customerDTO = customerBO.getCustomerById(connection, id);

                Jsonb jsonb = JsonbBuilder.create();
                String json = jsonb.toJson(customerDTO);
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

            CustomerDTO customerDTO = jsonb.fromJson(req.getReader(), CustomerDTO.class);
            System.out.println(customerDTO);

            if(customerDTO.getId()==null || !customerDTO.getId().matches("^(C00-)[0-9]{3}$")){
                resp.getWriter().write("id is empty or invalid");
                return;
            }else if(customerDTO.getName()==null || !customerDTO.getName().matches("^[A-Za-z ]{4,}$")){
                resp.getWriter().write("name is empty or invalid");
                return;
            }else if(customerDTO.getAddress()==null || !customerDTO.getAddress().matches("^[A-Za-z0-9., -]{8,}$")){
                resp.getWriter().write("address is empty or invalid");
                return;
            }else if(customerDTO.getContact()==null || !customerDTO.getContact().matches("^\\+94\\d{9}$|^(0\\d{9})$|^(0\\d{2}-\\d{7})$")){
                resp.getWriter().write("contact is empty or invalid");
                return;
            }

            boolean isSaved = customerBO.saveCustomer(connection, customerDTO);
            if(isSaved){
                resp.setStatus(HttpServletResponse.SC_CREATED);
            }else{
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "failed to save customer");
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            resp.sendError(HttpServletResponse.SC_CONFLICT, "Duplicate values. Please check again");
        }catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while processing the request.");
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (Connection connection = connectionPool.getConnection()){
            Jsonb jsonb = JsonbBuilder.create();

            CustomerDTO customerDTO = jsonb.fromJson(req.getReader(), CustomerDTO.class);
            System.out.println(customerDTO);

            if(customerDTO.getId()==null || !customerDTO.getId().matches("^(C00-)[0-9]{3}$")){
                resp.getWriter().write("id is empty or invalid");
                return;
            }else if(customerDTO.getName()==null || !customerDTO.getName().matches("^[A-Za-z ]{4,}$")){
                resp.getWriter().write("name is empty or invalid");
                return;
            }else if(customerDTO.getAddress()==null || !customerDTO.getAddress().matches("^[A-Za-z0-9., -]{8,}$")){
                resp.getWriter().write("address is empty or invalid");
                return;
            }else if(customerDTO.getContact()==null || !customerDTO.getContact().matches("^\\+94\\d{9}$|^(0\\d{9})$|^(0\\d{2}-\\d{7})$")){
                resp.getWriter().write("contact is empty or invalid");
                return;
            }

            boolean isUpdated = customerBO.updateCustomer(connection, customerDTO);
            if(isUpdated){
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            }else{
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "failed to update customer");
            }
        }catch (SQLIntegrityConstraintViolationException e) {
            resp.sendError(HttpServletResponse.SC_CONFLICT, "Duplicate values. Please check again");
        }catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while processing the request.");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        try (Connection connection = connectionPool.getConnection()){
            boolean isDeleted = customerBO.removeCustomer(connection, id);
            if(isDeleted){
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            }else{
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "failed to delete customer");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

