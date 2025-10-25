package Service;

import PopulatorTest.PopulatorTest;
import app.config.HibernateConfig;
import app.dtos.DayExerciseDTO;
import app.exceptions.ApiException;
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
    private DayExerciseService dayExerciseService;
    private DayService dayService;

    private int testDayId;
    private int testExerciseId;

    @BeforeAll
    void setup() {
        emf = HibernateConfig.getEntityManagerFactoryForTest();

        PopulatorTest populatorTest = new PopulatorTest(emf);
        populatorTest.populate();

        dayExerciseService = new DayExerciseService(emf);
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
        List<DayExerciseDTO> existing = dayExerciseService.getExercisesForDay(testDayId);
        existing.forEach(e -> dayExerciseService.removeExerciseFromDay(testDayId, e.getExerciseId()));
    }

    @Test
    void addExerciseToDay_createsSuccessfully() {
        DayExerciseDTO dto = new DayExerciseDTO();
        dto.setSets(3);
        dto.setReps(10);
        dto.setDurationSeconds(60);

        DayExerciseDTO result = dayExerciseService.addExerciseToDay(dto, testDayId, testExerciseId);

        assertThat(result, is(notNullValue()));
        assertThat(result.getSets(), equalTo(3));
        assertThat(result.getReps(), equalTo(10));
        assertThat(result.getExerciseId(), equalTo(testExerciseId));

        // Verify persisted
        List<DayExerciseDTO> persisted = dayExerciseService.getExercisesForDay(testDayId);
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
                () -> dayExerciseService.addExerciseToDay(dto, 9999, testExerciseId));
        assertThat(ex.getStatusCode(), equalTo(404));
        assertThat(ex.getMessage(), containsString("Day not found"));
    }

    @Test
    void addExerciseToDay_throwsWhenExerciseNotFound() {
        DayExerciseDTO dto = new DayExerciseDTO();
        dto.setSets(3);
        dto.setReps(10);
        dto.setDurationSeconds(60);

        ApiException ex = Assertions.assertThrows(ApiException.class,
                () -> dayExerciseService.addExerciseToDay(dto, testDayId, 9999));
        assertThat(ex.getStatusCode(), equalTo(404));
        assertThat(ex.getMessage(), containsString("Exercise not found"));
    }

    @Test
    void getExercisesForDay_returnsList() {
        DayExerciseDTO dto = new DayExerciseDTO();
        dto.setSets(2);
        dto.setReps(5);
        dto.setDurationSeconds(30);
        dayExerciseService.addExerciseToDay(dto, testDayId, testExerciseId);

        List<DayExerciseDTO> exercises = dayExerciseService.getExercisesForDay(testDayId);
        assertThat(exercises, is(not(empty())));
    }

    @Test
    void getAllDayExercises_returnsAll() {
        DayExerciseDTO dto = new DayExerciseDTO();
        dto.setSets(1);
        dto.setReps(8);
        dto.setDurationSeconds(45);
        dayExerciseService.addExerciseToDay(dto, testDayId, testExerciseId);

        List<DayExerciseDTO> all = dayExerciseService.getAllDayExercises();
        assertThat(all, is(not(empty())));
        assertThat(all.stream().anyMatch(e -> e.getDayId() == testDayId), is(true));
    }

    @Test
    void updateDayExercise_updatesCorrectly() {
        DayExerciseDTO dto = new DayExerciseDTO();
        dto.setSets(2);
        dto.setReps(5);
        dto.setDurationSeconds(30);
        dayExerciseService.addExerciseToDay(dto, testDayId, testExerciseId);

        DayExerciseDTO updateDto = new DayExerciseDTO();
        updateDto.setSets(5);
        updateDto.setReps(15);
        updateDto.setDurationSeconds(120);

        DayExerciseDTO updated = dayExerciseService.updateDayExercise(testDayId, testExerciseId, updateDto);

        assertThat(updated.getSets(), equalTo(5));
        assertThat(updated.getReps(), equalTo(15));
        assertThat(updated.getDurationSeconds(), equalTo(120));

        DayExerciseDTO persisted = dayExerciseService.getExercisesForDay(testDayId).get(0);
        assertThat(persisted.getSets(), equalTo(5));
    }

    @Test
    void removeExerciseFromDay_deletesSuccessfully() {
        DayExerciseDTO dto = new DayExerciseDTO();
        dto.setSets(2);
        dto.setReps(5);
        dto.setDurationSeconds(30);
        dayExerciseService.addExerciseToDay(dto, testDayId, testExerciseId);

        dayExerciseService.removeExerciseFromDay(testDayId, testExerciseId);

        List<DayExerciseDTO> exercises = dayExerciseService.getExercisesForDay(testDayId);
        assertThat(exercises.stream()
                .noneMatch(e -> e.getExerciseId() == testExerciseId), is(true));
    }

    @Test
    void removeExerciseFromDay_throwsWhenNotExists() {
        ApiException ex = Assertions.assertThrows(ApiException.class,
                () -> dayExerciseService.removeExerciseFromDay(testDayId, 9999));
        assertThat(ex.getStatusCode(), equalTo(404));
        assertThat(ex.getMessage(), containsString("DayExercise not found"));
    }
}
