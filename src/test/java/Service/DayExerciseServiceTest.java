package Service;

import app.config.HibernateConfig;
import app.dtos.DayDTO;
import app.dtos.DayExerciseDTO;
import app.exceptions.ApiException;
import app.populator.DBPopulator;
import app.services.DayExerciseService;
import app.services.DayService;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DayExerciseServiceTest {

    private EntityManagerFactory emf;
    private DayExerciseService service;
    private DayService dayService;

    private int testDayId;
    private int testExerciseId;

    @BeforeAll
    void setup() {
        emf = HibernateConfig.getEntityManagerFactoryForTest();

        DBPopulator populator = new DBPopulator(emf);
        populator.populate();

        service = new DayExerciseService(emf);
        dayService = new DayService(emf);

        // Grab a dynamic Day ID
        testDayId = dayService.getAll().get(0).getId();

        // Grab an Exercise ID dynamically
        testExerciseId = getFirstExerciseIdFromDB();
    }

    // Helpers to fetch dynamic IDs
    private int getFirstExerciseIdFromDB() {
        try (var em = emf.createEntityManager()) {
            return em.createQuery("SELECT e.id FROM Exercise e", Integer.class)
                    .setMaxResults(1)
                    .getSingleResult();
        }
    }

    @BeforeEach
    void cleanDayExercises() {
        List<DayExerciseDTO> existing = service.getExercisesForDay(testDayId);
        existing.forEach(e -> service.removeExerciseFromDay(testDayId, e.getExerciseId()));
    }

    @Test
    void addExerciseToDay_createsSuccessfully() {
        DayExerciseDTO dto = new DayExerciseDTO();
        dto.setSets(3);
        dto.setReps(10);
        dto.setDurationSeconds(60);

        DayExerciseDTO result = service.addExerciseToDay(dto, testDayId, testExerciseId);

        assertThat(result, is(notNullValue()));
        assertThat(result.getSets(), equalTo(3));
        assertThat(result.getReps(), equalTo(10));
        assertThat(result.getExerciseId(), equalTo(testExerciseId));

        // Verify persisted
        List<DayExerciseDTO> persisted = service.getExercisesForDay(testDayId);
        assertThat(persisted, hasItem(allOf(
                hasProperty("dayId", equalTo(testDayId)),
                hasProperty("exerciseId", equalTo(testExerciseId)),
                hasProperty("sets", equalTo(3)),
                hasProperty("reps", equalTo(10)),
                hasProperty("durationSeconds", equalTo(60))
        )));
    }

    @Test
    void addExerciseToDay_throwsWhenDayNotFound() {
        DayExerciseDTO dto = new DayExerciseDTO();
        dto.setSets(3);
        dto.setReps(10);
        dto.setDurationSeconds(60);

        ApiException ex = Assertions.assertThrows(ApiException.class,
                () -> service.addExerciseToDay(dto, 9999, testExerciseId));
        assertThat(ex.getStatusCode(), equalTo(404));
        assertThat(ex.getMessage(), containsString("Day not found"));
    }

    @Test
    void getExercisesForDay_returnsList() {
        DayExerciseDTO dto = new DayExerciseDTO();
        dto.setSets(2);
        dto.setReps(5);
        dto.setDurationSeconds(30);
        service.addExerciseToDay(dto, testDayId, testExerciseId);

        List<DayExerciseDTO> exercises = service.getExercisesForDay(testDayId);
        assertThat(exercises, is(not(empty())));
    }

    @Test
    void updateDayExercise_updatesCorrectly() {
        DayExerciseDTO dto = new DayExerciseDTO();
        dto.setSets(2);
        dto.setReps(5);
        dto.setDurationSeconds(30);
        service.addExerciseToDay(dto, testDayId, testExerciseId);

        DayExerciseDTO updateDto = new DayExerciseDTO();
        updateDto.setSets(5);
        updateDto.setReps(15);
        updateDto.setDurationSeconds(120);

        DayExerciseDTO updated = service.updateDayExercise(testDayId, testExerciseId, updateDto);

        assertThat(updated.getSets(), equalTo(5));
        assertThat(updated.getReps(), equalTo(15));
        assertThat(updated.getDurationSeconds(), equalTo(120));

        DayExerciseDTO persisted = service.getExercisesForDay(testDayId).get(0);
        assertThat(persisted.getSets(), equalTo(5));
    }

    @Test
    void removeExerciseFromDay_deletesSuccessfully() {
        DayExerciseDTO dto = new DayExerciseDTO();
        dto.setSets(2);
        dto.setReps(5);
        dto.setDurationSeconds(30);
        service.addExerciseToDay(dto, testDayId, testExerciseId);

        service.removeExerciseFromDay(testDayId, testExerciseId);

        List<DayExerciseDTO> exercises = service.getExercisesForDay(testDayId);
        assertThat(exercises.stream()
                .noneMatch(e -> e.getExerciseId() == testExerciseId), is(true));
    }
}
