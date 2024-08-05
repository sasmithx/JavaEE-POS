package lk.ijse.gdse68.javaeepos.dao.custom.impl;



import lk.ijse.gdse68.javaeepos.dao.custom.ItemDAO;
import lk.ijse.gdse68.javaeepos.dao.util.CrudUtil;
import lk.ijse.gdse68.javaeepos.entity.Item;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ItemDAOImpl implements ItemDAO {
    @Override
    public boolean save(Connection connection, Item entity) throws SQLException {
        String sql ="INSERT INTO item (code,name,price,qty) VALUES (?,?,?,?)";
        return CrudUtil.execute(connection, sql, entity.getCode(), entity.getName(), entity.getPrice(), entity.getQty());
    }

    @Override
    public boolean update(Connection connection, Item entity) throws SQLException {
        String sql = "UPDATE item SET name = ?, price = ?, qty = ? WHERE code = ?";
        return CrudUtil.execute(connection, sql, entity.getName(), entity.getPrice(), entity.getQty(), entity.getCode());
    }

    @Override
    public ArrayList<Item> getAll(Connection connection) throws SQLException {
        String sql = "SELECT * FROM item";
        ArrayList<Item> itemList = new ArrayList<Item>();
        ResultSet rs = CrudUtil.execute(connection, sql);

        while(rs.next()){
            Item item = new Item(
                    rs.getString(1),
                    rs.getString(2),
                    rs.getBigDecimal(3),
                    rs.getInt(4)
            );

            itemList.add(item);
        }
        return itemList;
    }

    @Override
    public boolean delete(Connection connection, String id) throws SQLException {
        String sql = "DELETE FROM item WHERE code = ?";
        return CrudUtil.execute(connection, sql, id);
    }

    @Override
    public Item findBy(Connection connection, String id) throws SQLException {
        String sql = "SELECT * FROM item WHERE code = ?";
        Item item = new Item();
        ResultSet rs = CrudUtil.execute(connection, sql, id);

        if(rs.next()) {
            item.setCode(rs.getString(1));
            item.setName(rs.getString(2));
            item.setPrice(rs.getBigDecimal(3));
            item.setQty(rs.getInt(4));
        }
        return item;
    }

    @Override
    public boolean reduceQty(Connection connection, Item item) throws SQLException {
        String sql = "UPDATE item SET qty = ( qty - ? ) WHERE code = ?";
        return CrudUtil.execute(connection, sql, item.getQty(), item.getCode());
    }
}
