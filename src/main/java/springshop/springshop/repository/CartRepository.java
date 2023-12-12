package springshop.springshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import springshop.springshop.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Cart findByMemberId(Long memberId);

}
