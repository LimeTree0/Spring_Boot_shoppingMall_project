package springshop.springshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import springshop.springshop.dto.CartDetailDto;
import springshop.springshop.entity.CartItem;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    CartItem findByCartIdAndItemId(Long cartId, Long itemId);

    @Query("select new springshop.springshop.dto.CartDetailDto(ci.id, i.itemNm, i.price, ci.count, im.imgUrl) " +
            "from CartItem ci, ItemImg im " +
            "join ci.item i " +
            "where ci.cart.id = :cartId " +
    "and im.item.id = ci.item.id " +
    "and im.repimgYn = 'Y' " +
            "order by ci.regTime desc")
    List<CartDetailDto> findCartDetailDtoList(@Param("cartId") Long cartId);
}
