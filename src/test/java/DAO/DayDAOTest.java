package DAO;

import PopulatorTest.PopulatorTest;
import app.config.HibernateConfig;
import app.daos.DayDAO;
import app.daos.WeekDAO;
import app.entities.Day;
import app.entities.Week;
import app.eums.Difficulty;
import app.exceptions.ApiException;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DayDAOTest {

    private static EntityManagerFactory emf;
    private DayDAO dayDAO;
    private WeekDAO weekDAO;
    private PopulatorTest populatorTest;

    @BeforeAll
    void setupAll() {
        emf = HibernateConfig.getEntityManagerFactoryForTest();
        dayDAO = new DayDAO(emf);
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

        // Populate database after truncation:
        populatorTest = new PopulatorTest(emf);
        populatorTest.populate();

    }

    @Test
    void testGetAllDays() {
        List<Day> days = dayDAO.getAll();
        assertThat(days, is(not(empty())));
    }

    @Test
    void testGetDayById() {
        Day day = dayDAO.getAll().get(0);
        Day found = dayDAO.getById(day.getId());
        assertThat(found, is(notNullValue()));
        assertThat(found.getDayExercises(), is(not(empty())));
    }

    @Test
    void testCreateDayWithWeek() {
        Week week = weekDAO.getAll().get(0);
        Day newDay = new Day();
        newDay.setDayName("Friday");
        newDay.setWorkoutType("Cardio");
        newDay.setDifficulty(Difficulty.EASY);
        newDay.setTotalWorkoutTime(30);

        Day created = dayDAO.Create(newDay, week.getId());
        assertThat(created.getId(), is(notNullValue()));
        assertThat(created.getWeek().getId(), equalTo(week.getId()));
    }

    @Test
    void testUpdateDay() {
        Day day = dayDAO.getAll().get(0);
        day.setDayName("UpdatedDay");
        Day updated = dayDAO.update(day.getId(), day);
        assertThat(updated.getDayName(), equalTo("UpdatedDay"));
    }

    @Test
    void testDeleteDay() {
        Day day = dayDAO.getAll().get(0);
        dayDAO.deleteById(day.getId());
        Assertions.assertThrows(ApiException.class, () -> dayDAO.getByIdOrThrow(day.getId()));
    }
}
