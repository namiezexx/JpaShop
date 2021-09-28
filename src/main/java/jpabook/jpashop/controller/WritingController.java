package jpabook.jpashop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class WritingController {

    @GetMapping("/writings/new")
    public String createForm(Model model) {
        model.addAttribute("writingForm", new MemberForm());
        return "writings/createWritingForm";
    }
}
