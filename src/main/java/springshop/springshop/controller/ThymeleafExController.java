package springshop.springshop.controller;

import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import springshop.springshop.constant.ItemSellStatus;
import springshop.springshop.dto.ItemDto;
import springshop.springshop.entity.Item;

import java.sql.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Controller
@RequestMapping("/thymeleaf")
public class ThymeleafExController {

    @GetMapping("/ex01")
    public String thymeleafExample01(Model model) {
        model.addAttribute("data", "타임리프 예제입니다");

        return "thymeleafEx/thymeleafEx01";
    }

    @GetMapping("/ex02")
    public String thymeleafExample02(Model model) {
        ItemDto itemDto = new ItemDto();

        itemDto.setItemNm("테스트 상품1");
        itemDto.setItemDetail("상품 상세 설명");
        itemDto.setPrice(10000);
        itemDto.setRegTime(LocalDateTime.now());

        model.addAttribute("itemDto", itemDto);

        return "thymeleafEx/thymeleafEx02";
    }

    @GetMapping("/ex03")
    public String thymeleafExample03(Model model) {
        ArrayList<ItemDto> itemDtoList = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            ItemDto itemDto = new ItemDto();
            itemDto.setItemNm("테스트 상품" + i);
            itemDto.setPrice(10000 + i);
            itemDto.setItemDetail("테스트 상품 상세 설명" + i);
            itemDto.setRegTime(LocalDateTime.now());
            itemDto.setUpdateTime(LocalDateTime.now());

            itemDtoList.add(itemDto);
        }

        model.addAttribute("itemDtoList", itemDtoList);
        return "thymeleafEx/thymeleafEx03";
    }

    @GetMapping("/ex04")
    public String thymeleafExample04(Model model) {
        ArrayList<ItemDto> itemDtoList = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            ItemDto itemDto = new ItemDto();
            itemDto.setItemNm("테스트 상품" + i);
            itemDto.setItemDetail("상품 상세 설명" + i);
            itemDto.setPrice(1000 * i);
            itemDto.setRegTime(LocalDateTime.now());

            itemDtoList.add(itemDto);
        }

        model.addAttribute("itemDtoList", itemDtoList);
        return "thymeleafEx/thymeleafEx04";
    }

    @GetMapping("/ex07")
    public String thymeleafExample07() {
        return "thymeleafEx/thymeleafEx07";
    }
}
