package Service;

import PopulatorTest.PopulatorTest;
import app.config.HibernateConfig;
import app.dtos.WeekDTO;
import app.services.WeekService;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class WeekServiceTest {

    private EntityManagerFactory emf;
    private WeekService weekService;
    private int testWeekId;

    @BeforeAll
    void init() {
        emf = HibernateConfig.getEntityManagerFactoryForTest();
        weekService = new WeekService(emf);
    }

    @BeforeEach
    void setup() {
        // Reset DB before each test
        PopulatorTest populator = new PopulatorTest(emf);
        populator.populate();

        // Grab a dynamic Week ID (Week 1)
        testWeekId = getFirstWeekIdFromDB();
    }

    private int getFirstWeekIdFromDB() {
        try (var em = emf.createEntityManager()) {
            return em.createQuery("SELECT w.id FROM Week w ORDER BY w.id", Integer.class)
                    .setMaxResults(1)
                    .getSingleResult();
        }
    }

    @AfterAll
    void tearDown() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }

    @Test
    void create_createsNewWeek() {
        WeekDTO dto = new WeekDTO();
        dto.setWeekNumber(99);

        WeekDTO created = weekService.create(dto);

        assertThat(created, is(notNullValue()));
        assertThat(created.getWeekNumber(), equalTo(99));
    }

    @Test
    void getById_returnsCorrectWeek() {
        WeekDTO dto = weekService.getById(testWeekId);

        assertThat(dto, is(notNullValue()));
        assertThat(dto.getId(), equalTo(testWeekId));
        assertThat(dto.getWeekNumber(), greaterThan(0));
    }

    @Test
    void getAll_returnsListOfWeeks() {
        List<WeekDTO> all = weekService.getAll();

        assertThat(all, is(not(empty())));
        // Always contains seeded weeks 1 and 2
        assertThat(all.stream().map(WeekDTO::getWeekNumber).toList(),
                hasItems(1, 2));
    }

    @Test
    void update_updatesWeekSuccessfully() {
        WeekDTO updateDto = new WeekDTO();
        updateDto.setWeekNumber(42);

        WeekDTO updated = weekService.update(updateDto, testWeekId);

        assertThat(updated.getWeekNumber(), equalTo(42));

        WeekDTO persisted = weekService.getById(testWeekId);
        assertThat(persisted.getWeekNumber(), equalTo(42));
    }

    @Test
    void update_throwsWhenWeekNotFound() {
        WeekDTO updateDto = new WeekDTO();
        updateDto.setWeekNumber(77);

        Assertions.assertThrows(RuntimeException.class,
                () -> weekService.update(updateDto, 9999));
    }

    @Test
    void delete_removesWeek() {
        // Create a new week first
        WeekDTO dto = new WeekDTO();
        dto.setWeekNumber(123);
        WeekDTO created = weekService.create(dto);

        // Delete it
        weekService.delete(created.getId());

        // Verify it no longer exists
        Assertions.assertThrows(Exception.class,
                () -> weekService.getById(created.getId()));
    }
}
