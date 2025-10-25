package Service;

import PopulatorTest.PopulatorTest;
import app.config.HibernateConfig;
import app.dtos.ExerciseDTO;
import app.eums.Difficulty;
import app.services.ExerciseService;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ExerciseServiceTest {

    private EntityManagerFactory emf;
    private ExerciseService exerciseService;
    private int testExerciseId;

    @BeforeAll
    void setup() {
        emf = HibernateConfig.getEntityManagerFactoryForTest();

        // Populate DB with some exercises
        PopulatorTest populator = new PopulatorTest(emf);
        populator.populate();

        exerciseService = new ExerciseService(emf);

        // Take the ID directly from the populator
        testExerciseId = populator.ex1.getId();
    }

    @AfterAll
    void tearDown() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }

    @Test
    void getById_returnsCorrectExercise() {
        ExerciseDTO dto = exerciseService.getById(testExerciseId);

        assertThat(dto, is(notNullValue()));
        assertThat(dto.getId(), equalTo(testExerciseId));
        assertThat(dto.getName(), equalTo("Push Up")); // seeded by PopulatorTest
    }

    @Test
    void getAll_returnsListOfExercises() {
        List<ExerciseDTO> all = exerciseService.getAll();

        assertThat(all, is(not(empty())));
        assertThat(all.stream().map(ExerciseDTO::getName).toList(),
                hasItems("Push Up", "Squat", "Plank")); // seeded by PopulatorTest
    }

    @Test
    void update_updatesExerciseSuccessfully() {
        ExerciseDTO dto = new ExerciseDTO();
        dto.setName("Updated Name");
        dto.setDescription("Updated Description");
        dto.setMuscleGroup("Arms");
        dto.setEquipment("Dumbbell");
        dto.setDifficulty(Difficulty.HARD);

        ExerciseDTO updated = exerciseService.update(dto, testExerciseId);

        assertThat(updated.getName(), equalTo("Updated Name"));
        assertThat(updated.getDescription(), equalTo("Updated Description"));
        assertThat(updated.getMuscleGroup(), equalTo("Arms"));
        assertThat(updated.getEquipment(), equalTo("Dumbbell"));
        assertThat(updated.getDifficulty(), equalTo(Difficulty.HARD));
    }

    @Test
    void delete_removesExercise() {
        //  Create a new exercise properly
        ExerciseDTO dto = new ExerciseDTO();
        dto.setName("Temp Exercise");
        dto.setDescription("To be deleted");
        dto.setMuscleGroup("Legs");
        dto.setEquipment("None");
        dto.setDifficulty(Difficulty.EASY);

        // creates a new exercise to delete
        ExerciseDTO created = exerciseService.create(dto);

        // Delete it
        exerciseService.delete(created.getId());

        // Verify it no longer exists
        Assertions.assertThrows(Exception.class, () -> exerciseService.getById(created.getId()));
    }
}

