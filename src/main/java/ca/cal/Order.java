package ca.cal;


import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Version
    private int version = 0;

    private String orderNumber;

    @OneToMany(mappedBy = "order")
    private Set<OrderItem> items = new HashSet<>();



}
