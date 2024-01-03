package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryDto;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


/**
 * xToOne(ManytoOne, OnetoOne)
 * Order
 * Order -> Member
 * Order -> Delivery
 */

@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;
    private final OrderSimpleQueryRepository orderSimpleQueryRepository;

    @GetMapping("/api/v1/simple-orders") //엔티티를 불러옴으로써 Order안의 모든 객체들이 노출되었다.
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        return all;
    }

    @GetMapping("/api/v2/simple-orders")  //Dto를 만들어서 엔티티를 한번 감싸서 필요한 내용만 전달
    public List<SimpleOrderDto> ordersV2() {
        //ORDER가 2개 조회됨
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());

        //ORDER가 2개 이므로 루프가 2번 돌아서 Member와 Delivery 쿼리가 2x2 = 4개가 나간다. 총 5개
        List<SimpleOrderDto> result = orders.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());

        return result;
    }

    /**
     * V1과 V2의 문제점
     * 쿼리가 굉장히 많이 나가게 된다
     */

    @GetMapping("/api/v3/simple-orders") //findAllWithMemberDelivery에서 fetch join을 사용하여 DB쿼리를 한방에 날려 해결
    public List<SimpleOrderDto> ordersV3() {
        List<Order> orders = orderRepository.findAllWithMemberDelivery(); // 엔티티를 불러와서 DTO로 변환 후 처리

        List<SimpleOrderDto> result = orders.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());

        return result;
    }

    @GetMapping("/api/v4/simple-orders") //DTO로 직접 전달하는 방식 쿼리 양이 V3보다 줄어 최적화 되었다 V3와 V4는 서로 장단점이 있다.
    public List<OrderSimpleQueryDto> ordersV4(){
        return orderSimpleQueryRepository.findOrderDtos();
    }
    /** V3과 V4는 성능면에서 그리 크게 차이가 나는것이 아니므로 V3사용 권장 */

    @Data
    static class SimpleOrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName();
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();
        }
    }

}
