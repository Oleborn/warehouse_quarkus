package devforge.repository;

import devforge.model.entity.Receipt;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

@ApplicationScoped
public class ReceiptRepository implements PanacheRepositoryBase<Receipt, UUID> {

    public Receipt findByNumber(String number) {
        return find("number", number).firstResult();
    }
}