package Service;

import app.config.HibernateConfig;
import app.daos.DayDAO;
import app.dtos.DayDTO;
import app.entities.Day;
import app.populator.DBPopulator;
import app.services.DayExerciseService;
import app.services.DayService;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DayServiceTest {

    private EntityManagerFactory emf;
    private DayService service;

    @BeforeAll
    void setup() {
        emf = HibernateConfig.getEntityManagerFactoryForTest();

        // Populate test database
        DBPopulator populator = new DBPopulator(emf);
        populator.populate();

        service = new DayService(emf);
    }

    @Test
    void createDay_createsSuccessfully() {
        DayDTO dto = new DayDTO();
        dto.setDayName("TestDay");
        dto.setWorkoutType("Cardio");
        dto.setTotalWorkoutTime(45);

        DayDTO created = service.create(dto, 1);
        assertThat(created.getId(), is(notNullValue()));
        assertThat(created.getDayName(), equalTo("TestDay"));
    }

    @Test
    void getAll_returnsList() {
        List<DayDTO> days = service.getAll();
        assertThat(days, is(not(empty())));
    }

    @Test
    void updateDay_updatesSuccessfully() {
        // Fetch a day dynamically
        DayDTO existingDay = service.getAll().get(0);

        DayDTO dto = new DayDTO();
        dto.setDayName("UpdatedDay");

        DayDTO updated = service.update(dto, existingDay.getId());
        assertThat(updated.getDayName(), equalTo("UpdatedDay"));
    }

    @Test
    void deleteDay_removesSuccessfully() {
        service.delete(1);
        Assertions.assertThrows(RuntimeException.class, () -> service.getById(1));
    }
}
