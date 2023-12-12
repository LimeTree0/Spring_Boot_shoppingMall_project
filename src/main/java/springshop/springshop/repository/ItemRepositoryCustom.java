package springshop.springshop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import springshop.springshop.dto.ItemSearchDto;
import springshop.springshop.dto.MainItemDto;
import springshop.springshop.entity.Item;

public interface ItemRepositoryCustom {

    Page<Item> getAdminItemlistPage(ItemSearchDto itemSearchDto, Pageable pageable);

    Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable);
}
