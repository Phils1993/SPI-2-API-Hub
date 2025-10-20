package app.daos;

import app.Config.HibernateConfig;
import app.entities.Day;
import app.exceptions.ApiException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DayDAO implements IDAO<Day, Integer>{
    private static EntityManagerFactory emf;

    private static DayDAO instance;

    public static DayDAO getInstance(EntityManagerFactory _emf) {
        if(instance == null){
            emf = _emf;
            instance = new DayDAO();
        }
        return instance;
    }

    @Override
    public Day create(Day day) {
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            em.persist(day);
            em.getTransaction().commit();
            return day;
        } catch(Exception ex){
            throw new ApiException(500, "no day created" + ex.getMessage());
        }
    }

    @Override
    public Day getById(int id) {
        try(EntityManager em = emf.createEntityManager()){
            return em.createQuery("SELECT d from Day d LEFT JOIN FETCH d.dayExercises de LEFT JOIN FETCH de.exercise " +
                    "where d.id = :id", Day.class )
                    .setParameter("id", id)
                    .getSingleResult();
        }  catch(Exception ex){
            throw new ApiException(500, "no day id found" + ex.getMessage());
        }
    }

    @Override
    public List<Day> getAll() {
        try(EntityManager em = emf.createEntityManager()){
            return em.createQuery("SELECT d FROM Day d LEFT JOIN FETCH d.dayExercises de " +
                            "LEFT JOIN FETCH de.exercise order by d.id"
                    ,Day.class).getResultList();
        }catch(Exception ex){
            throw new ApiException(500, "no days found");
        }
    }

    @Override
    public Day update(int id, Day day) {
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            Day foundDay = em.find(Day.class, id);
            if (foundDay == null) throw new ApiException(404, "Day not found");
            em.merge(foundDay);
            em.getTransaction().commit();
            return foundDay;
        } catch(Exception ex){
            throw new ApiException(500, "no day updated");
        }
    }

    @Override
    public void deleteById(int id) {
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            Day foundDay = em.find(Day.class, id);
            if (foundDay == null) throw new ApiException(404, "Day not found");
            em.remove(foundDay);
        }  catch(Exception ex){
            throw new ApiException(500, "no day deleted");
        }
    }
}
