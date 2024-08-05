package lk.ijse.gdse68.javaeepos.bo;


import lk.ijse.gdse68.javaeepos.bo.custom.impl.CustomerBOImpl;
import lk.ijse.gdse68.javaeepos.bo.custom.impl.ItemBOImpl;
import lk.ijse.gdse68.javaeepos.bo.custom.impl.OrderBOImpl;
import lk.ijse.gdse68.javaeepos.bo.custom.impl.OrderDetailBOImpl;

public class BOFactory {

    private static BOFactory boFactory;

    private BOFactory(){

    }

    public static BOFactory getBOFactory(){
        return (boFactory==null) ? boFactory = new BOFactory() : boFactory;
    }

    public enum BOTypes{
        CUSTOMER_BO, ITEM_BO, ORDER_BO, ORDER_DETAIL_BO
    }

    public <T extends SuperBO> T getBO(BOTypes type){
        switch (type){
            case CUSTOMER_BO:
                return (T) new CustomerBOImpl();
            case ITEM_BO:
                return (T) new ItemBOImpl();
            case ORDER_BO:
                return (T) new OrderBOImpl();
            case ORDER_DETAIL_BO:
                return (T) new OrderDetailBOImpl();
            default:
                return null;
        }
    }
}
