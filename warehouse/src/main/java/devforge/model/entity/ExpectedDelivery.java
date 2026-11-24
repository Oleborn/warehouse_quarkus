package devforge.model.entity;

import devforge.dictionary.ExpectedStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "expected_delivery")
public class ExpectedDelivery {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public UUID id;

    @Column(nullable = false, unique = true)
    public String number;

    public String supplier;

    @Column(name = "expected_date")
    public LocalDate expectedDate;

    @Enumerated(EnumType.STRING)
    public ExpectedStatus status;

    @OneToMany(
            mappedBy = "expectedDelivery",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    public List<ExpectedItem> items;

}