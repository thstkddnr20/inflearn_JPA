package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {
    
    private final EntityManager em;

    public void save(Member member) { // Member 저장
        em.persist(member);
    }

    public Member findOne(Long id) { // id값으로 Member조회
        return em.find(Member.class, id);
    }

    public List<Member> findAll() { // 목록 전체 조회
        return em.createQuery("select m from Member m", Member.class).getResultList();
    }

    public List<Member> findByName(String name) { // 이름에 의한 조회
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }
}
