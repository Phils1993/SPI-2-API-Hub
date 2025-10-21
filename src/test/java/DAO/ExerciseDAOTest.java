package DAO;

import app.config.HibernateConfig;
import app.daos.ExerciseDAO;
import app.entities.Exercise;
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
class ExerciseDAOTest {

    private static EntityManagerFactory emf;
    private ExerciseDAO exerciseDAO;

    @BeforeAll
    void setupClass() {
        emf = HibernateConfig.getEntityManagerFactoryForTest();
        exerciseDAO = ExerciseDAO.getInstance(emf);
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
    void testGetAllExercises() {
        List<Exercise> exercises = exerciseDAO.getAll();
        assertThat(exercises, is(not(empty())));
        assertThat(exercises.size(), greaterThanOrEqualTo(3));
    }

    @Test
    void testGetExerciseById() {
        Exercise ex = exerciseDAO.getAll().get(0);
        Exercise found = exerciseDAO.getById(ex.getId());
        assertThat(found, is(notNullValue()));
        assertThat(found.getName(), equalTo(ex.getName()));
    }

    @Test
    void testCreateExercise() {
        Exercise ex = new Exercise();
        ex.setName("Lunges");
        ex.setDescription("Leg exercise");
        ex.setMuscleGroup("Legs");
        ex.setEquipment("None");
        ex.setDifficulty(Difficulty.EASY);

        Exercise created = exerciseDAO.create(ex);
        assertThat(created.getId(), is(notNullValue()));
        Exercise found = exerciseDAO.getById(created.getId());
        assertThat(found.getName(), equalTo("Lunges"));
    }

    @Test
    void testUpdateExercise() {
        Exercise existing = exerciseDAO.getAll().get(0);
        existing.setName("UpdatedName");
        Exercise updated = exerciseDAO.update(existing.getId(), existing);
        assertThat(updated.getName(), equalTo("UpdatedName"));
    }

    @Test
    void testDeleteExercise() {
        Exercise ex = new Exercise();
        ex.setName("Temp");
        Exercise created = exerciseDAO.create(ex);
        exerciseDAO.deleteById(created.getId());
        Assertions.assertThrows(ApiException.class, () -> exerciseDAO.getById(created.getId()));
    }
}