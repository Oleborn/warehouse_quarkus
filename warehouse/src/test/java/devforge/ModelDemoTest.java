package devforge;

import devforge.dictionary.ExpectedStatus;
import devforge.model.entity.ExpectedDelivery;
import devforge.model.entity.ExpectedItem;
import devforge.repository.ExpectedDeliveryRepository;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 Позволяет тестировать приложение не изолированно, а практически в тех же условиях, в которых работают его компоненты: с CDI-контейнером, внедрением зависимостей, конфигурацией, Hibernate ORM, подключением к базе данных и другими интеграциями Quarkus.

 Что делает аннотация:
 1. Запускает тестовое окружение Quarkus перед выполнением тестов.
 2. Инициализирует CDI-контейнер, благодаря чему работает @Inject.
 3. Загружает конфигурацию приложения с учётом профиля %test.
 4. Инициализирует необходимые расширения Quarkus, например Hibernate ORM и Panache.
 5. Обеспечивает работу интеграций, включая Dev Services, которые могут автоматически запустить тестовую базу данных.
 6. После завершения тестов выполняет очистку тестового окружения.
 */
@QuarkusTest
class ModelDemoTest {

    @Inject
    DataSource dataSource;

    @Inject
    EntityManager entityManager;

    @Inject
    ExpectedDeliveryRepository repository;

    @Test
    void liquibaseMigrationsAreApplied() throws SQLException {

        try (Connection connection = dataSource.getConnection();

             Statement statement = connection.createStatement();

             ResultSet resultSet = statement.executeQuery(
                 """
                 SELECT
                     to_regclass('public.expected_delivery') IS NOT NULL
                     AND to_regclass('public.expected_item') IS NOT NULL
                     AND to_regclass('public.receipt') IS NOT NULL
                     AND to_regclass('public.received_item') IS NOT NULL
                     AND EXISTS (SELECT 1 FROM databasechangelog)
                 """
             )
        ) {

            assertTrue(resultSet.next());
            assertTrue(resultSet.getBoolean(1));
        }
    }

    @Test
    /**
     * Используется для тестирования операций с базой данных, когда необходимо проверить INSERT, UPDATE и DELETE, но при этом не оставлять изменения в тестовой базе.
     *
     * Что делает аннотация:
     * 1. Начинает транзакцию перед выполнением тестового метода.
     * 2. Позволяет выполнять операции с Hibernate ORM и Panache внутри этой транзакции.
     * 3. Обеспечивает фиксацию изменений внутри транзакции при вызове flush(), чтобы SQL-запросы действительно отправлялись в базу.
     * 4. После завершения тестового метода выполняет rollback вместо commit.
     */
    @TestTransaction
    void shouldPerformCrudOperations() {

        // CREATE

        ExpectedDelivery delivery = new ExpectedDelivery();
        delivery.number = "DEL-" + UUID.randomUUID();
        delivery.supplier = "Tech Supplier";
        delivery.expectedDate = LocalDate.now().plusDays(7);
        delivery.status = ExpectedStatus.values()[0];

        ExpectedItem item1 = new ExpectedItem();
        item1.name = "Keyboard";
        item1.quantity = new BigDecimal("10.000");
        item1.expectedDelivery = delivery;

        ExpectedItem item2 = new ExpectedItem();
        item2.name = "Mouse";
        item2.quantity = new BigDecimal("20.000");
        item2.expectedDelivery = delivery;

        delivery.items = List.of(item1, item2);

        repository.persistAndFlush(delivery);

        // READ

        entityManager.clear();

        ExpectedDelivery found = repository.findById(delivery.id);

        assertNotNull(found);
        assertEquals(delivery.number, found.number);
        assertEquals("Tech Supplier", found.supplier);

        // Проверяем, что дочерние сущности сохранились
        assertEquals(2, found.items.size());

        assertTrue(found.items.stream()
                .anyMatch(item -> item.name.equals("Keyboard")));

        assertTrue(found.items.stream()
                .anyMatch(item -> item.name.equals("Mouse")));

        // Проверяем собственный метод репозитория
        List<ExpectedDelivery> deliveries =
                repository.findByStatus(delivery.status);

        assertTrue(deliveries.stream()
                .anyMatch(d -> d.id.equals(delivery.id)));

        // UPDATE

        found.supplier = "New Tech Supplier";

        entityManager.flush();
        entityManager.clear();

        ExpectedDelivery updated = repository.findById(delivery.id);

        assertNotNull(updated);
        assertEquals("New Tech Supplier", updated.supplier);

        // DELETE

        repository.delete(updated);
        entityManager.flush();
        entityManager.clear();

        ExpectedDelivery deleted = repository.findById(delivery.id);

        assertNull(deleted);
    }
}