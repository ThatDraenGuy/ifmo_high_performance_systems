package ru.draen.hps.common.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import ru.draen.hps.app.operator.dao.OperatorRepository;
import ru.draen.hps.domain.Operator;
import ru.draen.hps.domain.Operator_;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
        "spring.jpa.properties.hibernate.default_schema=test_schema_adeletablerepository",
        "spring.liquibase.default-schema=test_schema_adeletablerepository"
})
@DataJpaTest
@Sql(value = {
        "/operator/setup.sql"
}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@ComponentScan(basePackageClasses = OperatorRepository.class)
public class ADeletableRepositoryTest {
    @Autowired
    ADeletableRepository<Operator, Long> deletableRepository;

    @PersistenceContext
    EntityManager entityManager;

    @Test
    void deleteTest() {
        assertAll(
                () -> assertTrue(deletableRepository.delete((root, cq, cb) -> cb.equal(root.get(Operator_.id), 1L))),
                () -> assertNotNull(entityManager.find(Operator.class, 1L).getDelDate())
        );
    }
}
