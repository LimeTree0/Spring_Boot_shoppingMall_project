package com.limecoding.shop.dto;

import com.limecoding.shop.constant.ItemSellStatus;
import com.limecoding.shop.entity.Item;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.springframework.ui.ModelMap;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ItemFormDto {

    private Long id;                        // 상품 코드

    @NotBlank(message = "상품명은 필수 입력 값입니다.")
    private String itemNm;                  // 상품명

    @NotNull(message = "가격은 필수 입력 값입니다")
    private int price;                      // 가격

    @NotNull(message = "재고는 필수 입력 값입니다")
    private int stockNumber;                // 재고 수량

    @NotBlank(message = "상품 상세 설명은 필수 입력 값입니다.")
    private String itemDetail;              // 상품 상세 설명

    private ItemSellStatus itemSellStatus;  // 상품 판매 상태

    private List<ItemImgDto> itemImgDtoList = new ArrayList<>();

    private List<Long> itemImgIds = new ArrayList<>();

    private static ModelMapper modelMapper = new ModelMapper();

    public Item createItem() {
        return modelMapper.map(this, Item.class);
    }

    public static ItemFormDto of(Item item) {
        return modelMapper.map(item, ItemFormDto.class);
    }
    }
