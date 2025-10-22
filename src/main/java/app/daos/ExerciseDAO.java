package app.daos;

import app.entities.Exercise;
import app.exceptions.ApiException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class ExerciseDAO implements IDAO<Exercise,Integer>{
    private final EntityManagerFactory emf;

    public ExerciseDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }


    @Override
    public Exercise create(Exercise exercise) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(exercise);
            em.getTransaction().commit();
            return exercise;
        } catch (Exception ex) {
            throw new ApiException(500, "Error creating exercise: " + ex.getMessage());
        }
    }

    @Override
    public Exercise getById(int id) {
        try (EntityManager em = emf.createEntityManager()) {
            Exercise exercise = em.find(Exercise.class, id);
            if (exercise == null) throw new ApiException(404, "Exercise not found" + id);
            return exercise;
        }
    }

    @Override
    public List<Exercise> getAll() {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery("SELECT e FROM Exercise e ORDER BY e.id", Exercise.class)
                    .getResultList();
        } catch (Exception ex) {
            throw new ApiException(500, "Error fetching exercises: " + ex.getMessage());
        }
    }

    @Override
    public Exercise update(int id, Exercise updatedExercise) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Exercise existing = em.find(Exercise.class, id);
            if (existing == null) throw new ApiException(404, "Exercise not found");

            existing.setName(updatedExercise.getName());
            existing.setDescription(updatedExercise.getDescription());
            existing.setMuscleGroup(updatedExercise.getMuscleGroup());
            existing.setEquipment(updatedExercise.getEquipment());
            existing.setDifficulty(updatedExercise.getDifficulty());

            em.merge(existing);
            em.getTransaction().commit();
            return existing;
        } catch (Exception ex) {
            throw new ApiException(500, "Error updating exercise: " + ex.getMessage());
        }
    }

    @Override
    public void deleteById(int id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Exercise existing = em.find(Exercise.class, id);
            if (existing == null) throw new ApiException(404, "Exercise not found");
            em.remove(existing);
            em.getTransaction().commit();
        } catch (Exception ex) {
            throw new ApiException(500, "Error deleting exercise: " + ex.getMessage());
        }
    }
}
