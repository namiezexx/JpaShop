package jpabook.jpashop;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {

	@GetMapping("hello")
	public String hell(Model model) {
		model.addAttribute("data", "hello!!!"); // model에 data라는 이름으로 hello라는 문자열을 세팅한다.
		return "hello";
	}
}
