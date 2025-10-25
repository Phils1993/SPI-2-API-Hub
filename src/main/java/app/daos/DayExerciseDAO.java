package app.daos;

import app.entities.DayExercise;
import app.entities.DayExerciseKey;
import app.exceptions.ApiException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class DayExerciseDAO implements IDAO<DayExercise, DayExerciseKey> {

    private final EntityManagerFactory emf;

    public DayExerciseDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    /*
    @Override
   public DayExercise create(DayExercise dayExercise) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            // Attach managed entities
            Day managedDay = em.find(Day.class, dayExercise.getDay().getId());
            Exercise managedExercise = em.find(Exercise.class, dayExercise.getExercise().getId());

            dayExercise.setDay(managedDay);
            dayExercise.setExercise(managedExercise);

            em.persist(dayExercise);

            // Force Hibernate to initialize lazy proxies
            managedExercise.getName();
            managedDay.getId();

            em.getTransaction().commit();
            return dayExercise;
        }
    }

     */
    @Override
    public DayExercise create(DayExercise dayExercise) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            // ✅ Reattach detached entities (important!)
            if (!em.contains(dayExercise.getDay())) {
                dayExercise.setDay(em.merge(dayExercise.getDay()));
            }
            if (!em.contains(dayExercise.getExercise())) {
                dayExercise.setExercise(em.merge(dayExercise.getExercise()));
            }

            // ✅ Persist DayExercise safely
            em.persist(dayExercise);
            em.getTransaction().commit();

            return dayExercise;
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public DayExercise getById(int id) {
        throw new UnsupportedOperationException("Use getByCompositeKey instead for DayExercise");
    }

    public DayExercise getByCompositeKey(DayExerciseKey key) {
        try (EntityManager em = emf.createEntityManager()) {
            DayExercise dayExercise = em.find(DayExercise.class, key);
            if (dayExercise == null) {
                throw new ApiException(404, "DayExercise not found");
            }
            return dayExercise;
        }
    }

    @Override
    public List<DayExercise> getAll() {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery("SELECT de FROM DayExercise de ORDER BY de.id.dayId", DayExercise.class)
                    .getResultList();
        } catch (Exception ex) {
            throw new ApiException(500, "Error fetching day-exercises: " + ex.getMessage());
        }
    }

    public List<DayExercise> getExercisesByDayId(int dayId) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery(
                            "SELECT de FROM DayExercise de JOIN FETCH de.exercise WHERE de.day.id = :dayId",
                            DayExercise.class)
                    .setParameter("dayId", dayId)
                    .getResultList();
        } catch (Exception ex) {
            throw new ApiException(500, "Error fetching exercises for day: " + ex.getMessage());
        }
    }

    @Override
    public DayExercise update(int id, DayExercise updated) {
        throw new UnsupportedOperationException("Use updateByKey(DayExerciseKey, DayExercise) instead");
    }

    public DayExercise updateByKey(DayExerciseKey key, DayExercise updated) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            DayExercise existing = em.find(DayExercise.class, key);
            if (existing == null) throw new ApiException(404, "DayExercise not found");

            existing.setSets(updated.getSets());
            existing.setReps(updated.getReps());
            existing.setDurationSeconds(updated.getDurationSeconds());


            em.getTransaction().commit();
            return existing;
        } catch (Exception ex) {
            throw new ApiException(500, "Error updating day-exercise: " + ex.getMessage());
        }
    }

    @Override
    public void deleteById(int id) {
    }

    public void deleteByKey(DayExerciseKey key) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            DayExercise existing = em.find(DayExercise.class, key);
            if (existing == null) throw new ApiException(404, "DayExercise not found");
            em.remove(existing);
            em.getTransaction().commit();
        } catch (ApiException ae) {
            throw ae;
        } catch (Exception ex) {
            throw new ApiException(500, "Error deleting day-exercise: " + ex.getMessage());
        }
    }
}
