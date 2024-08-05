package lk.ijse.gdse68.javaeepos.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Order {

    private String order_id;
    private LocalDate date;
    private String cust_id;
    private BigDecimal discount;
    private BigDecimal total;
}
