package lk.ijse.gdse68.javaeepos.dao;


import lk.ijse.gdse68.javaeepos.dao.custom.impl.*;

public class DAOFactory {

    private static DAOFactory daoFactory;

    private DAOFactory(){

    }

    public static DAOFactory getDAOFactory(){
        return (daoFactory==null) ? daoFactory = new DAOFactory() : daoFactory;
    }

    public enum DAOTypes{
        CUSTOMER_DAO, ITEM_DAO, ORDER_DAO, ORDER_DETAILS_DAO, QUERY_DAO
    }

    public <T extends SuperDAO> T getDAO(DAOTypes type){
        switch (type){
            case CUSTOMER_DAO:
                return (T) new CustomerDAOImpl();
            case ITEM_DAO:
                return (T) new ItemDAOImpl();
            case ORDER_DAO:
                return (T) new OrderDAOImpl();
            case ORDER_DETAILS_DAO:
                return (T) new OrderDetailDAOImpl();
            case QUERY_DAO:
                return (T) new QueryDAOImpl();
            default:
                return null;
        }
    }
}
