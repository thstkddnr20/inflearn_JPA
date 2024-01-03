package jpabook.jpashop.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.*;

@Entity
@Table(name = "orders")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // Order order = new Order()의 스타일로 생성하는것을 막아준다 생성메서드를 사용해서 개발할 수 있게해준다
@BatchSize(size =  100)
public class Order {

    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = LAZY) // XToOne 연관관계에 LAZY 설정 잊지않기
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL) // orderItems와 delivery는 order에서만 사용되므로 cascade 사용에 적절하다
    @BatchSize(size = 100)
    //cascade : Entity당 각각 persist호출을 해줘야하는데 persist(order)을 해주면 orderItems도 자동으로 persist가 된다
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING) //Enum 타입 Enumerated 쓰는거 까먹지 않기, STRING 쓰는 것 인지하기
    private OrderStatus status; //주문상태 [ORDER, CANCEL]

    //연관관계 편의 메서드 (핵심적으로 컨트롤 하는 부분이 가지고 있는게 좋다)
    public void setMember(Member member) { // Member객체를 가져와서
        this.member = member; // Order의 member로 지정 후
        member.getOrders().add(this); // Member안의 orders 리스트에 Order을 집어넣는다
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }
    //== 생성 메서드(서비스)==// 생성하는 지점 바꾸려면 여기만 바꾸면 되므로 편리
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    //== 비즈니스 로직 ==//
    /** 주문 취소 **/
    public void cancel(){
        if (delivery.getStatus() == DeliveryStatus.COMP) {
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
        }

        this.setStatus(OrderStatus.CANCEL);
        for (OrderItem orderItem : orderItems) { // Order한번 주문 할 때 오더아이템이 여러개일 수 있으므로 각각에 cancel을 날려준다
            orderItem.cancel();
        }
    }

    //== 조회 로직 ==//
    /** 전체 주문 가격 조회 */
    public int getTotalPrice(){
        int totalPrice = 0;
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }





}
