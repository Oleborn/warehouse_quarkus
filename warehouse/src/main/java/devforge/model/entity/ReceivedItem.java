package devforge.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "received_item")
public class ReceivedItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "receipt_id", nullable = false)
    public Receipt receipt;

    @ManyToOne
    @JoinColumn(name = "expected_item_id")
    public ExpectedItem expectedItem;

    public BigDecimal quantity;

    protected ReceivedItem() {
    }
}