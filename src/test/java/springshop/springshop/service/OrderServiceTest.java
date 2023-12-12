package springshop.springshop.service;

import jakarta.persistence.EntityNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import springshop.springshop.constant.ItemSellStatus;
import springshop.springshop.constant.OrderStatus;
import springshop.springshop.dto.OrderDto;
import springshop.springshop.entity.Item;
import springshop.springshop.entity.Member;
import springshop.springshop.entity.Order;
import springshop.springshop.entity.OrderItem;
import springshop.springshop.repository.ItemRepository;
import springshop.springshop.repository.MemberRepository;
import springshop.springshop.repository.OrderRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    MemberRepository memberRepository;

    public Item saveItem() {
        Item item = new Item();
        item.setItemNm("테스트 상품");
        item.setPrice(10000);
        item.setItemDetail("테스트 상품 상세 설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);
        return itemRepository.save(item);
    }

    public Member saveMember() {
        Member member = new Member();
        member.setEmail("test@test.com");

        return memberRepository.save(member);
    }

    @Test
    @DisplayName("주문 테스트")
    public void order() {
        Item item = saveItem();
        Member member = saveMember();

        OrderDto orderDto = new OrderDto();
        orderDto.setCount(10);
        orderDto.setItemId(item.getId());

        Long orderId = orderService.order(orderDto, member.getEmail());

        Order order = orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);

        List<OrderItem> orderItems = order.getOrderItems();
        int totalPrice = orderDto.getCount() * item.getPrice();
        Assertions.assertThat(order.getTotalPrice()).isEqualTo(totalPrice);
    }

    @Test
    @DisplayName("주문 취소 테스트")
    public void cancelOrder() {
        Item item = saveItem();
        Member member = saveMember();

        OrderDto orderDto = new OrderDto();
        orderDto.setCount(10);
        orderDto.setItemId(item.getId());
        Long orderId = orderService.order(orderDto, member.getEmail());

        Order order = orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);
        orderService.cancelOrder(orderId);

        Assertions.assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.CANCEL);
        Assertions.assertThat(item.getStockNumber()).isEqualTo(100);
    }
}