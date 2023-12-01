package springshop.springshop.repository;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import springshop.springshop.constant.ItemSellStatus;
import springshop.springshop.entity.Item;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;
    
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

        boolean success =true;

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
        List<Item> findItems = itemRepository.findByPriceLessThanOrderByAsc(10005);

        boolean success = true;
        for (Item findItem : findItems) {

        }
    }

    public static void createItemList(ItemRepository itemRepository) {

        for (int i = 1; i <= 10; i++) {
            Item item = new Item();
            item.setItemNm("테스트 상품" + i);
            item.setPrice(10000);
            item.setItemDetail("테스트 상품 상세 설명" + i);
            item.setItemSellStatus(ItemSellStatus.SELL);
            item.setStockNumber(100);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());

            itemRepository.save(item);
        }
    }
}