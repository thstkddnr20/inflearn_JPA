package jpabook.jpashop;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {

    @GetMapping("hello")// localhost:8080/hello로 가는 역할
    public String hello(Model model){
        model.addAttribute("data", "hello!!!");
        return "hello"; //화면 이름 hello뒤에 .html이 자동으로 붙는 관례가 있다

    }
}
