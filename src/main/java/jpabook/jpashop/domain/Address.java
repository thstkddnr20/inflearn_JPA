package jpabook.jpashop.domain;

import lombok.Getter;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotEmpty;

@Embeddable
@Getter
public class Address {

    private String city;
    private String street;
    private String zipcode;

    protected Address() { // 기본 생성자 필요; public을 하면 접근가능하므로 상속을 할 일이 크게 없으니 protected로 해두었다
    }

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode; // 값 타입은 이뮤터블하게 설계되어야 하므로 생성자로 엮어놓고 setter를 만들어놓지 않으면 변경 불가능하다.
    }
}
