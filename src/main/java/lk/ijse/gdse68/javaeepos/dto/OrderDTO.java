package lk.ijse.gdse68.javaeepos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderDTO {

    String order_id;
    LocalDate date;
    String cust_id;
    BigDecimal discount;
    BigDecimal total;
    List<OrderDetailDTO> order_list;

    public OrderDTO(String order_id, LocalDate date, String cust_id, BigDecimal discount, BigDecimal total) {
        this.order_id = order_id;
        this.date = date;
        this.cust_id = cust_id;
        this.discount = discount;
        this.total = total;
    }
}
