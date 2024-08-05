package lk.ijse.gdse68.javaeepos.bo.custom.impl;



import lk.ijse.gdse68.javaeepos.bo.custom.ItemBO;
import lk.ijse.gdse68.javaeepos.dao.DAOFactory;
import lk.ijse.gdse68.javaeepos.dao.custom.ItemDAO;
import lk.ijse.gdse68.javaeepos.dto.ItemDTO;
import lk.ijse.gdse68.javaeepos.entity.Item;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class ItemBOImpl implements ItemBO {

    ItemDAO itemDAO = DAOFactory.getDAOFactory().getDAO(DAOFactory.DAOTypes.ITEM_DAO);

    @Override
    public boolean saveCustomer(Connection connection, ItemDTO dto) throws SQLException {
        return itemDAO.save(connection, new Item(dto.getCode(), dto.getName(), dto.getPrice(), dto.getQty()));
    }

    @Override
    public boolean updateItem(Connection connection, ItemDTO dto) throws SQLException {
        return itemDAO.update(connection, new Item(dto.getCode(), dto.getName(), dto.getPrice(), dto.getQty()));
    }

    @Override
    public ArrayList<ItemDTO> getAllItems(Connection connection) throws SQLException {
        ArrayList<Item> itemList = itemDAO.getAll(connection);

        ArrayList<ItemDTO> itemDTOList = new ArrayList<ItemDTO>();

        for(Item item : itemList){
            ItemDTO dto = new ItemDTO(
                    item.getCode(),
                    item.getName(),
                    item.getPrice(),
                    item.getQty()
            );

            itemDTOList.add(dto);
        }
        return itemDTOList;
    }

    @Override
    public ItemDTO getItemByCode(Connection connection, String id) throws SQLException {
        Item item = itemDAO.findBy(connection, id);

        return new ItemDTO(
                item.getCode(),
                item.getName(),
                item.getPrice(),
                item.getQty()
        );
    }

    @Override
    public boolean removeItem(Connection connection, String id) throws SQLException {
        return itemDAO.delete(connection, id);
    }
}
