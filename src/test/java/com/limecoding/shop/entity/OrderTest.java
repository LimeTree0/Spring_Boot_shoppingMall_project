package com.limecoding.shop.entity;

import com.limecoding.shop.constant.ItemSellStatus;
import com.limecoding.shop.repository.ItemRepository;
import com.limecoding.shop.repository.MemberRepository;
import com.limecoding.shop.repository.OrderItemRepository;
import com.limecoding.shop.repository.OrderRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private MemberRepository memberRepository;

    @PersistenceContext
    EntityManager entityManager;

    public Item createItem() {
        Item item = new Item();
        item.setItemNm("테스트 상품");
        item.setPrice(10000);
        item.setItemDetail("상세 설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);

        return item;
    }

    @Test
    @DisplayName("영속성 전이 테스트")
    public void cascadeTest() {
        Order order = new Order();

        for(int i = 0; i < 3; i++) {
            Item item = this.createItem();
            itemRepository.save(item);
            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item);
            orderItem.setCount(10);
            orderItem.setOrderPrice(1000);
            orderItem.setOrder(order);
            order.getOrderItems().add(orderItem);
        }

        // 트랜젝션 종료 후가 아닌 바로 DB에 반영
        orderRepository.saveAndFlush(order);
        // 영속성 컨텍스트 초기화 -> DB에서 가져옴
        // 먼저 영속성 컨텍스트에서 찾아보고 없으면 실제 DB에서 가져옴
        entityManager.clear();

        Order savedOrder = orderRepository.findById(order.getId())
                .orElseThrow(() -> new EntityNotFoundException("해당 엔티티가 없습니다"));

        assertThat(savedOrder.getOrderItems().size()).isEqualTo(3);
    }

    public Order createOrder() {
        Order order = new Order();

        for (int i = 0; i < 3; i++) {
            Item item = createItem();
            itemRepository.save(item);

            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item);
            orderItem.setCount(10);
            orderItem.setOrderPrice(100);
            orderItem.setOrder(order);
            order.getOrderItems().add(orderItem);
        }

        Member member = new Member();
        memberRepository.save(member);
        orderRepository.save(order);

        return order;
    }

    @Test
    @DisplayName("고아 객체 테스트")
    public void orphanRemovalTest() {

        // given
        Order order = createOrder();
        OrderItem orderItem = order.getOrderItems().get(0);

        //when
        order.getOrderItems().remove(0);
        entityManager.flush();

        //then
        assertThat(orderItem).isNotSameAs(order.getOrderItems().get(0));
    }

    @Test
    @DisplayName("지연 로딩 테스트")
    public void lazyLoading() {
        Order order = this.createOrder();
        Long orderItemId = order.getOrderItems().get(0).getId();
        System.out.println("orderItemId = " + orderItemId);
        entityManager.flush();
        entityManager.clear();

        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new EntityNotFoundException("해당 엔티티가 없습니다"));

        System.out.println("Order class = " + orderItem.getOrder().getClass());
        System.out.println("=============================================");
        orderItem.getOrder().getOrderDate();
        System.out.println("=============================================");

    }
}