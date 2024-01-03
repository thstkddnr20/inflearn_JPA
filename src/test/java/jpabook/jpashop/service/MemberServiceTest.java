package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;


import static org.junit.Assert.*;

@RunWith(SpringRunner.class) // JUnit 실행할때 스프링이랑 엮어 실행
@SpringBootTest
@Transactional /** 중요 !!!! 테스트케이스에서 Transactional은 기본적으로 commit을 안하고 rollback을 하므로 Rollback value = false를 주었다
 테스트는 반복해야 되는 것이므로 DB에 데이터가 남아있으면 안된다.*/
public class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @Test
    public void 회원가입() throws Exception {
        //given
        Member member = new Member();
        member.setName("kim");

        //when
        Long savedId = memberService.join(member);

        //then
        assertEquals(member, memberRepository.findOne(savedId));
    }

    @Test(expected = IllegalStateException.class) // try catch를 써서 예외를 잡지않고, expected를 사용하여 코드를 줄였다
    public void 중복_회원_예외() throws Exception {
        //given
        Member member1 = new Member();
        member1.setName("kim");
        Member member2 = new Member();
        member2.setName("kim");

        //when
        memberService.join(member1);
        memberService.join(member2);


        //then
        Assert.fail("예외가 발생해야 한다."); //Assert.fail은 이 코드로 오면 안됨을 뜻함

    }

}