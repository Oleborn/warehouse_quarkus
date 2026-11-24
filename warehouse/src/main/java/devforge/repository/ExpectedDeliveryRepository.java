package devforge.repository;

import devforge.dictionary.ExpectedStatus;
import devforge.model.entity.ExpectedDelivery;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class ExpectedDeliveryRepository implements PanacheRepositoryBase<ExpectedDelivery, UUID> {

    public ExpectedDelivery findByNumber(String number) {
        return find("number", number).firstResult();
    }

    public List<ExpectedDelivery> findByStatus(ExpectedStatus status) {
        return list("status", status);
    }
}