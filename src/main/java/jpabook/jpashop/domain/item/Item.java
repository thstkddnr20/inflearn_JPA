package jpabook.jpashop.domain.item;

import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) //Item 테이블에 다 때려박는것 Book, Movie, Album 안에 있는 것들도 다 들어간다.
@DiscriminatorColumn(name = "dtype")
@Getter @Setter
@BatchSize(size = 100)
public abstract class Item { // 상속하고 확장하기 위한 기반 클래스이다. BOOK, ALBUM, MOVIE에 상속된 부모클래스

    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    /** stock 증가 */
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    public void removeStock(int quantity) {
        int restStock = this.stockQuantity - quantity;
        if (restStock < 0){
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity = restStock;
        // 세터를 가지고 건드리기보다는 addstock removestock을 만들어서 객체지향적으로 풀어나간다
    }
}
