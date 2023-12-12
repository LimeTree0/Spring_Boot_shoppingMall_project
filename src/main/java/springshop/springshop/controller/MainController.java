package springshop.springshop.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import springshop.springshop.dto.ItemSearchDto;
import springshop.springshop.dto.MainItemDto;
import springshop.springshop.service.ItemService;

import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MainController {

    private final ItemService itemService;
    @GetMapping("/")
    public String main(ItemSearchDto itemSearchDto, @RequestParam("page") Optional<Integer> page,
        Model model) {
        log.info("page : {}", page);
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 2);
        Page<MainItemDto> items = itemService.getMainPage(itemSearchDto, pageable);

        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 2);

        return "main";
    }
}
