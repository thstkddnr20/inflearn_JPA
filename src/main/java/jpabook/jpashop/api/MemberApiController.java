package jpabook.jpashop.api;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.stream.Collectors;

@RestController // @ResponseBody, @Controller 동시
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    @GetMapping("/api/v1/members") // Entity를 그대로 반환하는 형식, 필요없는 정보들이 모두 노출됨
    public List<Member> membersV1(){
        return memberService.findMembers();
    }

    //회원 조회
    @GetMapping("/api/v2/members")
    public Result memberV2(){
        List<Member> findMembers = memberService.findMembers();
        List<MemberDto> collect = findMembers
                .stream()
                .map(m -> new MemberDto(m.getName()))
                .collect(Collectors.toList()); //List Member를 Name만 가져와서 List MemberDto로 바꿈

        return new Result(collect);
    }

    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) { //@RequestBody : Json으로 온 Body를 Member에 매핑해서 넣어준다 (JSON데이터를 멤버로 바꿔서 넣어줌)


        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }
     /**V1 버전의 문제점 : API 바인딩 값을 엔티티를 파라미터로 그대로 받아서 사용 (DTO를 따로 만들지 않은 것이 문제)*/

    //회원 등록
    @PostMapping("/api/v2/members") // PostMapping으로 받아온 {name = ""} JSON값을 request(Dto)에 저장 후 member객체에 넣어준 형태
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request) {

        Member member = new Member();
        member.setName(request.getName());

        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    /**
     * V2 버전으로 V1의 문제를 해결
     */

    //회원 수정
    @PutMapping("/api/v2/members/{id}") //회원 수정
    public UpdateMemberResponse updateMemberV2(
            @PathVariable("id") Long id,
            @RequestBody @Valid UpdateMemberRequest request){

        memberService.update(id, request.getName());
        Member findMember = memberService.findOne(id);

        return new UpdateMemberResponse(findMember.getId(), findMember.getName()); //api에 반환 되는 값
    }











    @Data
    static class UpdateMemberRequest{
        @NotEmpty
        private String name; // name이라는 JSON값 전달 시 저장 되는 공간
    }

    @Data
    @AllArgsConstructor
    static class UpdateMemberResponse{
        private Long id;
        private String name;
    }


    @Data
    static class CreateMemberRequest { //Dto를 통해 API 스펙 자체가 name만 받게 되어있구나를 알 수 있다.
        @NotEmpty
        private String name;
    }

    @Data
    static class CreateMemberResponse{
        private Long id;

        public CreateMemberResponse(Long id) {
            this.id = id;
        }
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class MemberDto {
        private String name;
    }
}
