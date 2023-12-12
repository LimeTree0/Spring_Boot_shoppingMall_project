package springshop.springshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import springshop.springshop.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
