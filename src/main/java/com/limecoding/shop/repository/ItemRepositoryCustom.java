package com.limecoding.shop.repository;

import com.limecoding.shop.dto.ItemSearchDto;
import com.limecoding.shop.dto.MainItemDto;
import com.limecoding.shop.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemRepositoryCustom {
    Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable);

    Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable);
}
