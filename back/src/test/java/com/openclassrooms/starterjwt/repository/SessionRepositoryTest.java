package com.openclassrooms.starterjwt.repository;

import com.openclassrooms.starterjwt.models.Session;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
        "spring.datasource.driverClassName=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",

        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect",

        // ultra important : empêche Hibernate d'ajouter des trucs MySQL style engine=InnoDB
        "spring.jpa.properties.hibernate.dialect.storage_engine=",

        // pour debug si tu veux voir le DDL
        "spring.jpa.show-sql=true"
})

class SessionRepositoryTest {

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void shouldSaveAndFindById() {
        Session session = new Session()
                .setName("Yoga morning")
                .setDate(new Date())
                .setDescription("A simple session")
                .setUsers(new ArrayList<>());

        Session saved = sessionRepository.save(session);
        entityManager.flush();
        entityManager.clear();

        assertNotNull(saved.getId());

        Session found = sessionRepository.findById(saved.getId()).orElse(null);
        assertNotNull(found);
        assertEquals("Yoga morning", found.getName());
    }

    @Test
    void shouldDeleteById() {
        Session session = new Session()
                .setName("Session to delete")
                .setDate(new Date())
                .setDescription("Will be deleted")
                .setUsers(new ArrayList<>());

        Session saved = sessionRepository.save(session);
        Long id = saved.getId();

        sessionRepository.deleteById(id);
        entityManager.flush();

        assertFalse(sessionRepository.findById(id).isPresent());
    }
}
