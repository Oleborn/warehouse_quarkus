package devforge.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "receipt")
public class Receipt {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public UUID id;

    @Column(nullable = false, unique = true)
    public String number;

    @ManyToOne(optional = false)
    @JoinColumn(
            name = "expected_delivery_number",
            referencedColumnName = "number",
            nullable = false
    )
    public ExpectedDelivery expectedDelivery;

    @Column(name = "received_at", nullable = false)
    public LocalDate receivedAt;

    protected Receipt() {
    }
}