package DAO;

import PopulatorTest.PopulatorTest;
import app.config.HibernateConfig;
import app.daos.DayDAO;
import app.daos.DayExerciseDAO;
import app.daos.ExerciseDAO;
import app.entities.Day;
import app.entities.DayExercise;
import app.entities.DayExerciseKey;
import app.entities.Exercise;
import app.exceptions.ApiException;
import app.populator.DBPopulator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DayExerciseDAOTest {

    private static EntityManagerFactory emf;
    private DayExerciseDAO dayExerciseDAO;
    private DayDAO dayDAO;
    private ExerciseDAO exerciseDAO;

    @BeforeAll
    void setupAll() {
        emf = HibernateConfig.getEntityManagerFactoryForTest();
        dayExerciseDAO = new DayExerciseDAO(emf);
        dayDAO = new DayDAO(emf);
        exerciseDAO = new ExerciseDAO(emf);

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
    void testGetExercisesByDayId() {
        Day day = dayDAO.getAll().get(0);
        List<DayExercise> exercises = dayExerciseDAO.getExercisesByDayId(day.getId());
        assertThat(exercises, is(not(empty())));
        assertThat(exercises.get(0).getDay().getId(), equalTo(day.getId()));
    }

    @Test
    void testCreateAndGetByKey() {
        Day day = dayDAO.getAll().get(0);
        Exercise ex = exerciseDAO.getAll().stream()
                .filter(e -> dayExerciseDAO.getExercisesByDayId(day.getId())
                        .stream()
                        .noneMatch(de -> de.getExercise().getId() == e.getId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No free exercise found"));

        DayExercise de = new DayExercise();
        de.setDay(day);
        de.setExercise(ex);
        de.setSets(3);
        de.setReps(10);
        de.setDurationSeconds(60);
        de.setId(new DayExerciseKey(day.getId(), ex.getId()));

        DayExercise created = dayExerciseDAO.create(de);
        DayExercise found = dayExerciseDAO.getByCompositeKey(created.getId());

        assertThat(found, is(notNullValue()));
        assertThat(found.getSets(), equalTo(3));
    }

    @Test
    void testUpdateByKey() {
        DayExercise de = dayExerciseDAO.getAll().get(0);
        de.setReps(99);
        DayExercise updated = dayExerciseDAO.updateByKey(de.getId(), de);
        assertThat(updated.getReps(), equalTo(99));
    }

    @Test
    void testDeleteByKey() {
        DayExercise de = dayExerciseDAO.getAll().get(0);
        dayExerciseDAO.deleteByKey(de.getId());
        Assertions.assertThrows(ApiException.class,
                () -> dayExerciseDAO.getByCompositeKey(de.getId()));
    }
}
