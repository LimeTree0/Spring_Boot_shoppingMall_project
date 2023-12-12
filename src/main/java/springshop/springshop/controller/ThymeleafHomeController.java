package springshop.springshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/thymeleaf")
public class ThymeleafHomeController {
    @GetMapping
    public String Home() {
        return "thymeleafHome";
    }
}
