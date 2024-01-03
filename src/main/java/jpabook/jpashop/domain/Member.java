package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    @Embedded// 데이터베이스에 저장될때 Member 테이블은 Address클래스의 필드들을 컬럼으로 가지게 된다.
    private Address address; //Address를 Member에 내장

    @JsonIgnore //@JsonIgnore JSON 정보에서 빠지게 됨 But, 많은 조회 시스템을 만들때 엔티티에 이러한 제한을 주게되면 다른 서비스를 만들기 힘들다..
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();
}
