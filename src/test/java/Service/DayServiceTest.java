package Service;

import app.config.HibernateConfig;
import app.dtos.DayDTO;
import app.exceptions.ApiException;
import app.populator.DBPopulator;
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

        DBPopulator populator = new DBPopulator(emf);
        populator.populate();

        service = new DayService(emf);
    }

    @BeforeEach
    void cleanDays() {
        // Optionally delete dynamically added days here
    }

    // Helper to fetch first Week ID dynamically
    private int getFirstWeekIdFromDB() {
        try (var em = emf.createEntityManager()) {
            return em.createQuery("SELECT w.id FROM Week w", Integer.class)
                    .setMaxResults(1)
                    .getSingleResult();
        }
    }

    @Test
    void createDay_createsSuccessfully() {
        DayDTO dto = new DayDTO();
        dto.setDayName("TestDay");
        dto.setWorkoutType("Cardio");
        dto.setTotalWorkoutTime(45);

        int weekId = getFirstWeekIdFromDB();

        DayDTO created = service.create(dto, weekId);
        assertThat(created.getId(), is(notNullValue()));
        assertThat(created.getDayName(), equalTo("TestDay"));

        // Verify persisted
        DayDTO fetched = service.getById(created.getId());
        assertThat(fetched.getDayName(), equalTo("TestDay"));
    }

    @Test
    void getAll_returnsList() {
        List<DayDTO> days = service.getAll();
        assertThat(days, is(not(empty())));
    }

    @Test
    void updateDay_updatesSuccessfully() {
        DayDTO existingDay = service.getAll().get(0);

        DayDTO dto = new DayDTO();
        dto.setDayName("UpdatedDay");

        DayDTO updated = service.update(dto, existingDay.getId());
        assertThat(updated.getDayName(), equalTo("UpdatedDay"));

        DayDTO fetched = service.getById(existingDay.getId());
        assertThat(fetched.getDayName(), equalTo("UpdatedDay"));
    }

    @Test
    void deleteDay_removesSuccessfully() {
        int weekId = getFirstWeekIdFromDB();

        DayDTO day = service.create(new DayDTO(){{
            setDayName("ToDelete");
            setWorkoutType("Test");
        }}, weekId);

        service.delete(day.getId());
        ApiException ex = Assertions.assertThrows(ApiException.class,
                () -> service.getById(day.getId()));
        assertThat(ex.getStatusCode(), equalTo(404));
    }
}