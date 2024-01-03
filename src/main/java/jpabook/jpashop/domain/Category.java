package jpabook.jpashop.domain;

import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.*;

@Entity
@Getter
@Setter
public class Category {

    @Id
    @GeneratedValue
    @Column(name = "category_id")
    private Long id; // 실무에서 id로 모두 통일한것을 사용하였을때 구분하기 어려우므로 category_id로 사용하였다. 객체 id의 이름도 바꿔주어도된다.

    private String name;

    @ManyToMany // 실무에서 사용 불가
    @JoinTable(name = "category_item", joinColumns = @JoinColumn(name = "category_id"), inverseJoinColumns = @JoinColumn(name = "item_id") // 외래키, 반대쪽 엔티티의 외래키
    ) // 다대다를 일대다 다대일로 풀어내는 중간테이블을 둬야함

    private List<Item> items = new ArrayList<>();

    @ManyToOne(fetch = LAZY) // ManytoOne과 아래의 OneToMany로 자기 자신에게 양방향으로 걸어준 모습
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private List<Category> child = new ArrayList<>();

    //연관관계 편의 메서드
    private void addChildCategory(Category child) {
        this.child.add(child);
        child.setParent(this);
    }

}
