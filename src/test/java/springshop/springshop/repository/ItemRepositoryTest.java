package springshop.springshop.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;
import springshop.springshop.constant.ItemSellStatus;
import springshop.springshop.entity.Item;
import springshop.springshop.entity.QItem;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @PersistenceContext
    EntityManager em;

    @BeforeAll
    public static void before(@Autowired ItemRepository itemRepository) {

        createItemList(itemRepository);
    }

    @Test
    @DisplayName("상품 저장 테스트")
    public void createItemTest() {
        // given
        Item item = new Item();
        item.setItemNm("테스트 상품");
        item.setPrice(10000);
        item.setItemDetail("테스트 상품 상세 설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);
        item.setRegTime(LocalDateTime.now());
        item.setUpdateTime(LocalDateTime.now());

        // when
        itemRepository.save(item);

        // then
        Optional<Item> findItem = itemRepository.findById(item.getId());
        assertThat(findItem.isPresent()).isTrue();
        assertThat(findItem.get().getId()).isEqualTo(item.getId());
        assertThat(findItem.get().getItemNm()).isEqualTo(item.getItemNm());
    }

    @Test
    @DisplayName("상품명 조회 테스트")
    public void findByItemNmTest() {
        // given


        // when
        List<Item> itemList = itemRepository.findByItemNm("테스트 상품1");

        assertThat(itemList.get(0).getItemNm()).isEqualTo("테스트 상품1");

    }

    @Test
    @DisplayName("상품명, 상품상세 설명 or 테스트")
    public void findByItemNmOrItemDetailTest() {

        List<Item> findItems = itemRepository.findByItemNmOrItemDetail("테스트 상품1", "테스트 상품 상세 설명5");

        boolean success = true;

        for (Item findItem : findItems) {
            if (!findItem.getItemNm().equals("테스트 상품1") &&
                    !findItem.getItemDetail().equals("테스트 상품 상세 설명5")) {
                success = false;
                break;
            }
        }

        assertThat(success).isTrue();
        assertThat(findItems.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("가격 LessThan 테스트")
    public void findByPriceLessThanTest() {
        List<Item> findItems = itemRepository.findByPriceLessThan(10005);

        boolean success = true;

        for (Item findItem : findItems) {
            if (!(findItem.getPrice() < 10005)) {
                success = false;
                break;
            }
        }

        assertThat(success).isTrue();
    }

    @Test
    @DisplayName("가격 내림차순 조회 테스트")
    public void findByPriceLessThanOrderByPriceDesc() {
        List<Item> findItems = itemRepository.findByPriceLessThanOrderByPriceDesc(10005);

        boolean success = true;
        int i = 4;

        for (Item findItem : findItems) {
            log.info("findItem = {}", findItem);
            if (findItem.getPrice() != 10000 + i) {
                success = false;
                break;
            }

            i--;
        }

        assertThat(success).isTrue();
    }

    @Test
    @DisplayName("@Query를 이용한 상품 조회 테스트")
    public void findByItemDetailTest() {
        List<Item> findItems = itemRepository.findByItemDetail("테스트 상품 상세 설명");

        boolean success = true;

        int prePrice = Integer.MAX_VALUE;
        for (Item findItem : findItems) {
            if (!findItem.getItemDetail().contains("테스트 상품 상세 설명") ||
                    findItem.getPrice() > prePrice) {
                success = false;
                break;
            }

            prePrice = findItem.getPrice();
        }

        assertThat(success).isTrue();
    }

    @Test
    @DisplayName("querydsl 조회 테스트1")
    public void queryDslTest() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QItem qItem = QItem.item;

        JPAQuery<Item> query = queryFactory.selectFrom(qItem)
                .where(qItem.itemSellStatus.eq(ItemSellStatus.SELL))
                .where(qItem.itemDetail.like("%" + "테스트 상품 상세 설명" + "%"))
                .orderBy(qItem.price.desc());

        List<Item> findItems = query.fetch();

        boolean success = true;

        int prePrice = Integer.MAX_VALUE;
        for (Item findItem : findItems) {
            if (!findItem.getItemDetail().contains("테스트 상품 상세 설명") ||
                    findItem.getItemSellStatus() != ItemSellStatus.SELL ||
                    findItem.getPrice() > prePrice) {
                success = false;
                break;
            }

            prePrice = findItem.getPrice();
        }

        assertThat(success).isTrue();
    }

    @Test
    @DisplayName("querydsl 조회 테스트2")
    public void queryDslTest2() {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QItem item = QItem.item;

        String itemDetail = "테스트 상품 상세 설명";
        int price = 10003;
        String itemSelStatus = "SELL";

        booleanBuilder.and(item.itemDetail.like("%" + itemDetail + "%"));
        booleanBuilder.and(item.price.gt(price));

        if (StringUtils.equals(itemSelStatus, ItemSellStatus.SELL)) {
            booleanBuilder.and(item.itemSellStatus.eq(ItemSellStatus.SELL));
        }

        Pageable pageable = PageRequest.of(0, 5);

        Page<Item> itemPagingResult = itemRepository.findAll(booleanBuilder, pageable);
        log.info("total elements = {}", itemPagingResult.getTotalElements());

        List<Item> resultItemList = itemPagingResult.getContent();
        for (Item resultItem : resultItemList) {
            log.info("resultItem = {}", resultItem);
        }
    }

    public static void createItemList(ItemRepository itemRepository) {

        for (int i = 1; i <= 5; i++) {
            Item item = new Item();
            item.setItemNm("테스트 상품" + i);
            item.setPrice(10000 + i);
            item.setItemDetail("테스트 상품 상세 설명" + i);
            item.setItemSellStatus(ItemSellStatus.SELL);
            item.setStockNumber(100);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());

            itemRepository.save(item);
        }

        for (int i = 6; i <= 10; i++) {
            Item item = new Item();
            item.setItemNm("테스트 상품" + i);
            item.setPrice(10000 + i);
            item.setItemDetail("테스트 상품 상세 설명" + i);
            item.setItemSellStatus(ItemSellStatus.SOLD_OUT);
            item.setStockNumber(0);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());

            itemRepository.save(item);
        }
    }
}