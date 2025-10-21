package Service;

import app.config.HibernateConfig;
import app.dtos.DayExerciseDTO;
import app.entities.DayExerciseKey;
import app.exceptions.ApiException;
import app.populator.DBPopulator;
import app.services.DayExerciseService;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DayExerciseServiceTest {

    private EntityManagerFactory emf;
    private DayExerciseService service;

    @BeforeAll
    void setup() {
        emf = HibernateConfig.getEntityManagerFactoryForTest();

        // Populate test database
        DBPopulator populator = new DBPopulator(emf);
        populator.populate();

        service = new DayExerciseService(emf);
    }


    @Test
    void addExerciseToDay_createsSuccessfully() {
        DayExerciseDTO dto = new DayExerciseDTO();
        dto.setSets(3);
        dto.setReps(10);
        dto.setDurationSeconds(60);

        DayExerciseDTO result = service.addExerciseToDay(dto, 1, 1);

        assertThat(result, is(notNullValue()));
        assertThat(result.getSets(), equalTo(3));
        assertThat(result.getReps(), equalTo(10));
    }

    @Test
    void addExerciseToDay_throwsWhenDayNotFound() {
        DayExerciseDTO dto = new DayExerciseDTO();
        dto.setSets(3);
        dto.setReps(10);
        dto.setDurationSeconds(60);

        ApiException ex = Assertions.assertThrows(ApiException.class,
                () -> service.addExerciseToDay(dto, 999, 1));
        assertThat(ex.getStatusCode(), equalTo(404));
    }

    @Test
    void getExercisesForDay_returnsList() {
        List<DayExerciseDTO> exercises = service.getExercisesForDay(1);
        assertThat(exercises, is(not(empty())));
    }

    @Test
    void updateDayExercise_updatesCorrectly() {
        DayExerciseDTO dto = new DayExerciseDTO();
        dto.setSets(5);
        dto.setReps(15);
        dto.setDurationSeconds(120);

        DayExerciseDTO updated = service.updateDayExercise(1, 1, dto);
        assertThat(updated.getSets(), equalTo(5));
        assertThat(updated.getReps(), equalTo(15));
        assertThat(updated.getDurationSeconds(), equalTo(120));
    }

    @Test
    void removeExerciseFromDay_deletesSuccessfully() {
        service.removeExerciseFromDay(1, 1);
        List<DayExerciseDTO> exercises = service.getExercisesForDay(1);
        assertThat(exercises.stream()
                .noneMatch(e -> e.getDayId() == 1 && e.getExerciseId() == 1), is(true));
    }
}
