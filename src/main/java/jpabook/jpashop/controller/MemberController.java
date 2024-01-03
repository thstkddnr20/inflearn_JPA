package jpabook.jpashop.controller;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members/new") // get은 폼 화면을 열어보는 것이고 (안의 /members/new는 그저 사이트 이름을 나타낼뿐)
    public String createForm(Model model) {
        model.addAttribute("memberForm", new MemberForm()); // 빈 껍데기인 MemberForm을 뷰에서 객체에 접근 가능
        return "members/createMemberForm";
    }

    @PostMapping("/members/new") // post는 데이터를 받아오는 것
    public String create(@Valid MemberForm form, BindingResult result) { // @Valid로 유효성 검사를 한다, validate한거 다음에 bindingresult가 있으면 오류가 담겨서 코드가 실행된다

        if (result.hasErrors()) {
            return "members/createMemberForm";
        }
        Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());

        Member member = new Member();
        member.setName(form.getName());
        member.setAddress(address);

        memberService.join(member);
        return "redirect:/"; // 바탕화면으로 돌아가기
    }

    @GetMapping("/members")
    public String list(Model model) {
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);
        return "members/memberList"; //간단하니 member객체를 그대로 뿌렸지만, Dto를 따로 만들어서 필요한 정보만을 뿌리는 것이 선호된다
    }
}
/** 데이터는 MemberController에서 MemberForm으로 넘기고 Member로 넘어간다 */