package DAO;

import app.config.HibernateConfig;
import app.daos.DayDAO;
import app.daos.WeekDAO;
import app.entities.Day;
import app.entities.Week;
import app.eums.Difficulty;
import app.exceptions.ApiException;
import app.populator.DBPopulator;
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

    @BeforeAll
    void setupClass() {
        emf = HibernateConfig.getEntityManagerFactoryForTest();
        dayDAO = DayDAO.getInstance(emf);
        weekDAO = WeekDAO.getInstance(emf);
    }

    @AfterAll
    void tearDown() {
        if (emf != null && emf.isOpen()) emf.close();
    }

    @BeforeEach
    void setup() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.createQuery("DELETE FROM DayExercise").executeUpdate();
        em.createQuery("DELETE FROM Day").executeUpdate();
        em.createQuery("DELETE FROM Week").executeUpdate();
        em.createQuery("DELETE FROM Exercise").executeUpdate();
        em.getTransaction().commit();
        em.close();

        new DBPopulator(emf).populate();
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
