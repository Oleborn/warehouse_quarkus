package devforge.model.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "expected_item")
public class ExpectedItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public UUID id;

    @Column(name = "name", nullable = false)
    public String name;

    @Column(precision = 12, scale = 3, nullable = false)
    public BigDecimal quantity;

    @ManyToOne
    @JoinColumn(name = "expected_delivery_id", nullable = false)
    public ExpectedDelivery expectedDelivery;
}