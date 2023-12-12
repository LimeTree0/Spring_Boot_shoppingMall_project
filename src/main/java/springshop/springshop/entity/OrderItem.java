package springshop.springshop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.antlr.v4.runtime.atn.SemanticContext;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class OrderItem extends BaseEntity{

    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @Getter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice; // 주문 가격

    private int count;      // 수량

    public static OrderItem createOrderItem(Item item, int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setCount(count);
        orderItem.setOrderPrice(item.getPrice());

        item.removeStock(count);
        return orderItem;
    }

    public int getTotalPrice() {
        return getOrderPrice() * count;
    }

    public void cancel() {
        this.getItem().addStock(count);
    }

}
