package DAO;

import PopulatorTest.PopulatorTest;
import app.config.HibernateConfig;
import app.daos.WeekDAO;
import app.entities.Week;
import app.exceptions.ApiException;
import app.populator.DBPopulator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class WeekDAOTest {

    private static EntityManagerFactory emf;
    private WeekDAO weekDAO;
    private PopulatorTest populatorTest;

    @BeforeAll
    void setupAll() {
        emf = HibernateConfig.getEntityManagerFactoryForTest();
        weekDAO = new WeekDAO(emf);

        // TRUNCATE all relevant tables before tests
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.createNativeQuery("TRUNCATE TABLE day_exercise RESTART IDENTITY CASCADE").executeUpdate();
            em.createNativeQuery("TRUNCATE TABLE day RESTART IDENTITY CASCADE").executeUpdate();
            em.createNativeQuery("TRUNCATE TABLE week RESTART IDENTITY CASCADE").executeUpdate();
            em.createNativeQuery("TRUNCATE TABLE exercise RESTART IDENTITY CASCADE").executeUpdate();
            em.getTransaction().commit();
        }

        // Populate database after truncation
        populatorTest = new PopulatorTest(emf);
        populatorTest.populate();
    }

    @Test
    void testGetAllWeeks() {
        List<Week> weeks = weekDAO.getAll();

        assertThat(weeks, is(not(empty())));
        assertThat(weeks.size(), greaterThanOrEqualTo(2));
        assertThat(weeks.get(0).getWeekNumber(), equalTo(1));
    }

    @Test
    void testGetWeekById() {
        List<Week> allWeeks = weekDAO.getAll();
        assertThat(allWeeks, is(not(empty())));

        Week week = weekDAO.getById(allWeeks.get(0).getId());

        assertThat(week, is(notNullValue()));
        assertThat(week.getDays(), is(not(empty())));
        assertThat(
                week.getDays().stream().anyMatch(day -> !day.getDayExercises().isEmpty()),
                is(true)
        );
    }

    @Test
    void testCreateWeek() {
        Week newWeek = new Week();
        newWeek.setWeekNumber(99);

        Week created = weekDAO.create(newWeek);
        assertThat(created.getId(), is(notNullValue()));

        Week found = weekDAO.getById(created.getId());
        assertThat(found.getWeekNumber(), equalTo(99));
    }

    @Test
    void testUpdateWeek() {
        Week existing = weekDAO.getAll().get(0);
        existing.setWeekNumber(10);
        Week updated = weekDAO.update(existing.getId(), existing);

        assertThat(updated.getWeekNumber(), equalTo(10));
    }

    @Test
    void testDeleteWeek() {
        Week week = weekDAO.create(new Week(77));
        weekDAO.deleteById(week.getId());

        Assertions.assertThrows(ApiException.class, () -> weekDAO.getById(week.getId()));
    }
}

