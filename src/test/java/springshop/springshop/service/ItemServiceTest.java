package springshop.springshop.service;

import jakarta.persistence.EntityNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import springshop.springshop.constant.ItemSellStatus;
import springshop.springshop.dto.ItemFormDto;
import springshop.springshop.entity.Item;
import springshop.springshop.entity.ItemImg;
import springshop.springshop.repository.ItemImgRepository;
import springshop.springshop.repository.ItemRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ItemServiceTest {

    @Autowired
    ItemService itemService;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    ItemImgRepository itemImgRepository;

    List<MultipartFile> createMultipartFiles() {
        ArrayList<MultipartFile> multipartFileList = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            String path = "C:/shop/item/";
            String imageName = "image" + i + ".jpg";
            MockMultipartFile multipartFile =
                    new MockMultipartFile(path, imageName,
                            "image/jpg", new byte[]{1, 2, 3, 4});

            multipartFileList.add(multipartFile);
        }

        return multipartFileList;
    }

    @Test
    @DisplayName("상품 등록 테스트")
    @WithMockUser(username = "admin", roles = "ADMIN")
    void saveItem() throws IOException {
        ItemFormDto itemFormDto = new ItemFormDto();
        itemFormDto.setItemNm("테스트 상품");
        itemFormDto.setItemSellStatus(ItemSellStatus.SELL);
        itemFormDto.setItemDetail("테스트 상품입니다.");
        itemFormDto.setPrice(1000);
        itemFormDto.setStockNumber(100);
        List<MultipartFile> multipartFileList = createMultipartFiles();
        Long itemId = itemService.saveItem(itemFormDto, multipartFileList);

        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);
        Item item = itemRepository.findById(itemId)
                .orElseThrow(EntityNotFoundException::new);

        assertThat(itemFormDto.getItemNm()).isEqualTo(item.getItemNm());
        assertThat(itemFormDto.getItemSellStatus()).isEqualTo(item.getItemSellStatus());
        assertThat(itemFormDto.getItemDetail()).isEqualTo(item.getItemDetail());
        assertThat(itemFormDto.getPrice()).isEqualTo(item.getPrice());
        assertThat(itemFormDto.getStockNumber()).isEqualTo(item.getStockNumber());
        assertThat(multipartFileList.get(0).getOriginalFilename()).isEqualTo(itemImgList.get(0).getOriImgName());
    }
}