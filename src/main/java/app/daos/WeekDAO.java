package app.daos;

import app.entities.Week;
import app.exceptions.ApiException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class WeekDAO implements IDAO<Week,Integer> {
    private static EntityManagerFactory emf;
    private static WeekDAO instance;

    public static WeekDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new WeekDAO();
        }
        return instance;
    }

    @Override
    public Week create(Week week) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(week);
            em.getTransaction().commit();
            return week;
        } catch (Exception ex) {
            throw new ApiException(500, "Error creating week: " + ex.getMessage());
        }
    }

    @Override
    public Week getById(int id) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery(
                            "SELECT w FROM Week w LEFT JOIN FETCH w.days d LEFT JOIN FETCH d.dayExercises de LEFT JOIN FETCH de.exercise " +
                                    "WHERE w.id = :id", Week.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (Exception ex) {
            throw new ApiException(500, "Week not found: " + ex.getMessage());
        }
    }

    @Override
    public List<Week> getAll() {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery(
                            "SELECT w FROM Week w LEFT JOIN FETCH w.days d LEFT JOIN FETCH d.dayExercises de LEFT JOIN FETCH de.exercise e " +
                                    "ORDER BY w.weekNumber, d.id", Week.class)
                    .getResultList();
        } catch (Exception ex) {
            throw new ApiException(500, "Error fetching weeks: " + ex.getMessage());
        }
    }

    @Override
    public Week update(int id, Week updatedWeek) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Week existing = em.find(Week.class, id);
            if (existing == null) throw new ApiException(404, "Week not found");

            existing.setWeekNumber(updatedWeek.getWeekNumber());
            em.merge(existing);
            em.getTransaction().commit();
            return existing;
        } catch (Exception ex) {
            throw new ApiException(500, "Error updating week: " + ex.getMessage());
        }
    }

    @Override
    public void deleteById(int id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Week existing = em.find(Week.class, id);
            if (existing == null) throw new ApiException(404, "Week not found");
            em.remove(existing);
            em.getTransaction().commit();
        } catch (Exception ex) {
            throw new ApiException(500, "Error deleting week: " + ex.getMessage());
        }
    }
}
