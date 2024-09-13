package com.personal.project.api.entity.product;

import com.personal.project.api.entity.user.User;
import jakarta.persistence.Table;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;


@Table(name = Product.TABLE_NAME, schema = Product.TABLE_SCHEMA)
@Entity(name = "product")


@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Product {

    public static final String TABLE_NAME = "product";
    public static final String TABLE_SCHEMA = "sch_application";

    @Id
    //@Setter(AccessLevel.NONE)
    @Column(name = "id", unique = true)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "name", length = 100)
    @NotBlank()
    private String name;

    @Column(name = "price_in_cents")
    @NotNull()
    @DecimalMax("1000000")
    private Integer price_in_cents;

    @NotNull
    @Column(name = "active")
    private Boolean active;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;

    public Product(String name, Integer price_in_cents) {
         this.name = name;
         this.price_in_cents = price_in_cents;
    }

}