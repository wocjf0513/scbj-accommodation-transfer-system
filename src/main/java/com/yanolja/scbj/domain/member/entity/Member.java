package com.yanolja.scbj.domain.member.entity;

import com.yanolja.scbj.domain.payment.entity.PaymentHistory;
import com.yanolja.scbj.domain.prdouct.entity.Product;
import com.yanolja.scbj.global.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("사용자 식별자")
    private Long id;

    @Column(nullable = false, unique = true)
    @Comment("사용자 이메일")
    private String email;

    @Column(nullable = false)
    @Comment("사용자 비밀번호")
    private String password;

    @Column(length = 50, nullable = false)
    @Comment("사용자 이름")
    private String name;

    @Column(length = 50, nullable = false)
    @Comment("사용자 전화번호")
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Comment("사용자 권한")
    private Authority authority;

    @OneToMany
    @JoinColumn(name = "product_id")
    private List<Product> productList = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "member_id")
    private PaymentHistory paymentHistory;

    @Column(nullable = false)
    @ColumnDefault(value = "false")
    private boolean alarmStatus;

    @Builder
    private Member(Long id, String email, String password, String name, String phone,
        Authority authority, List<Product> productList) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.authority = authority;
        this.productList = productList;
    }

    public void updatePassword(String encodedPassword) {
        this.password = encodedPassword;
    }

    public void toggleAlarmStatus() {
        this.alarmStatus = !alarmStatus;
    }

}
